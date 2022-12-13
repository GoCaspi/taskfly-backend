package com.gocaspi.taskfly.taskcollection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TaskCollectionRepositoryImpl implements TaskCollectionRepositoryCustom {
    public static final String TEAM_ID_OBJ = "teamIDObj";
    public static final String COND = "$cond";
    public static final String OWNER_ID = "ownerID";
    public static final String RESULT = "result";
    private final MongoTemplate mongoTemplate;

    private static final String OBJECTID = "objId";
    private static final String LISTID = "listId";
    private static final String COLLECTIONNAME = "taskCollection";
    private static final String FOREIGNCOLLECTION = "task";
    private static final String LOOKUPFIELD = "tasks";
    private static final String REMOVE_KEY = "$$REMOVE";
    private static final String ADD_FIELDS_KEY = "$addFields";
    private static final String TEAM_ID = "$teamID";
    private static final String TEAM_RESULT = "teamResult";
    private static final String TO_STRING_KEY = "$toString";
    @Autowired
    public TaskCollectionRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    private AggregationOperation addConvertedIDandTeamIDFields(){
        return aggregationOperation ->
            new Document(ADD_FIELDS_KEY,
                    new Document("tcIDObj",
                            new Document(TO_STRING_KEY, "$_id"))
                            .append(TEAM_ID_OBJ,
                                    new Document(COND, Arrays.asList(new Document("$ifNull", Arrays.asList(TEAM_ID, REMOVE_KEY)),
                                            new Document(COND, Arrays.asList(new Document("$ne", Arrays.asList(TEAM_ID, "")),
                                                    new Document("$toObjectId", TEAM_ID), REMOVE_KEY)), REMOVE_KEY))));
    }

    private AggregationOperation addConvertedIDField(){
        return aggregationOperation -> {
            var toString = new Document(TO_STRING_KEY, "$_id");
            var id = new Document(OBJECTID, toString);
            return new Document(ADD_FIELDS_KEY, id);
        };
    }

    private AggregationOperation addTeamIDandConvertedIDField(){
        return aggreationOperation -> new Document(ADD_FIELDS_KEY,
                new Document("listIdObj",
                        new Document(TO_STRING_KEY, "$_id"))
                        .append(TEAM_ID_OBJ,
                                new Document(COND, Arrays.asList(new Document("$ifNull", Arrays.asList(TEAM_ID, REMOVE_KEY)),
                                        new Document(COND, Arrays.asList(new Document("$ne", Arrays.asList(TEAM_ID, "")),
                                                new Document("$toObjectId", TEAM_ID), REMOVE_KEY)), REMOVE_KEY))));
    }
    @Override
    public List<TaskCollectionGetQuery> findByOwnerID(String userID){
        var match = Aggregation.match(new Criteria(OWNER_ID).is(userID));

        var lookupOperation = LookupOperation.newLookup()
                .from(FOREIGNCOLLECTION)
                .localField(OBJECTID)
                .foreignField(LISTID)
                .as(LOOKUPFIELD);
        var aggregation = Aggregation.newAggregation(match, addConvertedIDField(), lookupOperation);
        return mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollectionGetQuery.class).getMappedResults();
    }

    @Override
    public TaskCollectionGetQuery findByID(String collectionId){
        var match = Aggregation.match(new Criteria("_id").is(collectionId));

        var lookupOperation = LookupOperation.newLookup()
                .from(FOREIGNCOLLECTION)
                .localField(OBJECTID)
                .foreignField(LISTID)
                .as(LOOKUPFIELD);
        var aggregation = Aggregation.newAggregation(match, addConvertedIDField(), lookupOperation);
        return mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollectionGetQuery.class).getUniqueMappedResult();
    }

    @Override
    public List<TaskCollectionGetQuery> findByTeamID(String teamID){
        MatchOperation match = Aggregation.match(new Criteria("teamID").is(teamID));

        var lookupOperation = LookupOperation.newLookup()
                .from(FOREIGNCOLLECTION)
                .localField(OBJECTID)
                .foreignField(LISTID)
                .as(LOOKUPFIELD);
        var aggregation = Aggregation.newAggregation(match, addConvertedIDField(), lookupOperation);
        return mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollectionGetQuery.class).getMappedResults();
    }

    @Override
    public List<TaskCollectionGetQuery> findByUserID(String userID){
        var teamManagementLookup = LookupOperation.newLookup()
                .from("teamManagement")
                .localField(TEAM_ID_OBJ)
                .foreignField("_id")
                .as(TEAM_RESULT);
        var taskLookup = LookupOperation.newLookup()
                .from(FOREIGNCOLLECTION)
                .localField("listIdObj")
                .foreignField(LISTID)
                .as(LOOKUPFIELD);
        var matchOperation = Aggregation.match(
                new Criteria().orOperator(
                        Criteria.where(OWNER_ID).is(userID),
                        new Criteria().andOperator(
                                Criteria.where(TEAM_RESULT).exists(true),
                                Criteria.where(TEAM_RESULT).not().size(0),
                                new Criteria().orOperator(
                                        Criteria.where("teamResult.ownerID").is(userID),
                                        Criteria.where("teamResult.members").is(userID)
                                )
                        ),
                        Criteria.where("members").is(userID)

                )
        );
        var aggregation = Aggregation.newAggregation(addTeamIDandConvertedIDField(), teamManagementLookup, taskLookup, matchOperation);
        return mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollectionGetQuery.class).getMappedResults();

    }


    public Boolean hasAccessToCollection(String userID, String collectionID){
        MatchOperation matchTCID = Aggregation.match(new Criteria("tcIDObj").is(collectionID));
        var lookupOperation = LookupOperation.newLookup()
                .from("teamManagement")
                .localField(TEAM_ID_OBJ)
                .foreignField("_id")
                .as(RESULT);
        var matchOperation = Aggregation.match(
                new Criteria().orOperator(
                        Criteria.where(OWNER_ID).is(userID),
                        new Criteria().andOperator(
                                Criteria.where(RESULT).exists(true),
                                Criteria.where(RESULT).not().size(0),
                                new Criteria().orOperator(
                                        Criteria.where("result.userID").is(userID),
                                        Criteria.where("result.members").is(userID)
                                )
                        ),
                        Criteria.where("members").is(userID)

                )
        );
        var aggregation = Aggregation.newAggregation(addConvertedIDandTeamIDFields(), matchTCID, lookupOperation, matchOperation);
        TaskCollection tc = mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollection.class).getUniqueMappedResult();
        return !Objects.isNull(tc);

    }

}