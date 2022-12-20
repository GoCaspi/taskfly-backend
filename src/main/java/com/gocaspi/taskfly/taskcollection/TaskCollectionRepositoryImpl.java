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


    /**
     * a helper function for constructing the addFields Query and converting the taskcollectionID to a string and the teamid to an object id;
     * @return the finished addFields Aggregation Query
     */
    private AggregationOperation addConvertedIDandTeamIDFields(){
        return aggregationOperation ->
            new Document(ADD_FIELDS_KEY,
                    new Document(OBJECTID,
                            new Document(TO_STRING_KEY, "$_id"))
                            .append(TEAM_ID_OBJ,
                                    new Document(COND, Arrays.asList(new Document("$ifNull", Arrays.asList(TEAM_ID, REMOVE_KEY)),
                                            new Document(COND, Arrays.asList(new Document("$ne", Arrays.asList(TEAM_ID, "")),
                                                    new Document("$toObjectId", TEAM_ID), REMOVE_KEY)), REMOVE_KEY))));
    }

    /**
     * a utility function that simply converts a local _id to a string.
     * @return the finished addFields Aggregation Query
     */
    private AggregationOperation addConvertedIDField(){
        return aggregationOperation -> {
            var toString = new Document(TO_STRING_KEY, "$_id");
            var id = new Document(OBJECTID, toString);
            return new Document(ADD_FIELDS_KEY, id);
        };
    }

    /**
     * a mongo aggregation pipeline that searches all task collections with the ownerid
     * set to the supplied userID and appends all tasks from the corresponding lists to the result as an array.
     * @param userID the user who is being looked for
     * @return A list of all TaskCollections where the user has been assigned
     */
    @Override
    public List<TaskCollectionGetQuery> findByOwnerID(String userID){
        var match = Aggregation.match(new Criteria(OWNER_ID).is(userID));

        var lookupOperation = LookupOperation.newLookup()
                .from(FOREIGNCOLLECTION)
                .localField(OBJECTID)
                .foreignField(LISTID)
                .as(LOOKUPFIELD);
        var aggregation = Aggregation.newAggregation(addConvertedIDField(), match, lookupOperation);
        return mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollectionGetQuery.class).getMappedResults();
    }

    /**
     * a mongo aggregation pipeline that searches all task collections with the id
     * set to the supplied id
     * @param collectionId the id for the taskcollection
     * @return returns the taskcollection with the matching id
     */
    @Override
    public TaskCollectionGetQuery findByID(String collectionId){
        var match = Aggregation.match(new Criteria("_id").is(collectionId));

        var lookupOperation = LookupOperation.newLookup()
                .from(FOREIGNCOLLECTION)
                .localField(OBJECTID)
                .foreignField(LISTID)
                .as(LOOKUPFIELD);
        var aggregation = Aggregation.newAggregation( addConvertedIDField(), match, lookupOperation);
        return mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollectionGetQuery.class).getUniqueMappedResult();
    }

    /**
     * a mongo aggregation pipeline that searches all task collections for the supplied teamid
     * @param teamID the teamid to look for
     * @return a list of all task collections that have been assigned to the teamid
     */
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

    /**
     * a mongo aggregation pipeline that searches all tasks which matches to the userid and returns all matching taskCollections
     * @param userID the userid to look for
     * @return a list of all task collections that have been assigned to the user
     */
    @Override
    public List<TaskCollectionGetQuery> findByUserID(String userID){
        var teamManagementLookup = LookupOperation.newLookup()
                .from("teamManagement")
                .localField(TEAM_ID_OBJ)
                .foreignField("_id")
                .as(TEAM_RESULT);
        var taskLookup = LookupOperation.newLookup()
                .from(FOREIGNCOLLECTION)
                .localField(OBJECTID)
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
        var aggregation = Aggregation.newAggregation(addConvertedIDandTeamIDFields(), teamManagementLookup, taskLookup, matchOperation);
        return mongoTemplate.aggregate(aggregation, COLLECTIONNAME, TaskCollectionGetQuery.class).getMappedResults();

    }

    /**
     * a mongo aggregation pipeline that checks if a user has access to a task collection and returns a boolean as a result.
     * @param userID the userid of the user which wants to access the collection
     * @param collectionID the collectionid that the user wants to access
     * @return true if the user is allowed the access the task collection and false if not.
     */
    public Boolean hasAccessToCollection(String userID, String collectionID){
        MatchOperation matchTCID = Aggregation.match(new Criteria(OBJECTID).is(collectionID));
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