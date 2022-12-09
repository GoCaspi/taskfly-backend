package com.gocaspi.taskfly.taskcollection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

public class TaskCollectionRepositoryImpl implements TaskCollectionRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    private static final String OBJECTID = "objId";
    private static final String LISTID = "listId";
    private static final String COLLECTIONNAME = "taskCollection";
    private static final String FOREIGNCOLLECTION = "task";
    private static final String LOOKUPFIELD = "tasks";
    @Autowired
    public TaskCollectionRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    private AggregationOperation addConvertedIDandTeamIDFields(){
        return aggregationOperation -> {
            return new Document("$addFields",
                    new Document("tcIDObj",
                            new Document("$toString", "$_id"))
                            .append("teamIDObj",
                                    new Document("$cond", Arrays.asList(new Document("$ifNull", Arrays.asList("$teamID", "$$REMOVE")),
                                            new Document("$cond", Arrays.asList(new Document("$ne", Arrays.asList("$teamID", "")),
                                                    new Document("$toObjectId", "$teamID"), "$$REMOVE")), "$$REMOVE"))));
        };
    }

    private AggregationOperation addConvertedIDField(){
        return aggregationOperation -> {
            var toString = new Document("$toString", "$_id");
            var id = new Document(OBJECTID, toString);
            return new Document("$addFields", id);
        };
    }
    @Override
    public List<TaskCollectionGetQuery> findByOwnerID(String userID){
        var match = Aggregation.match(new Criteria("ownerID").is(userID));

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
    public Boolean hasAccessToCollection(String userid, String collectionID){
        MatchOperation matchTCID = Aggregation.match(new Criteria("tcIDObj").is(collectionID));
        var lookupOperation = LookupOperation.newLookup()
                .from("teamManagement")
                .localField("teamIDObj")
                .foreignField("_id")
                .as("result");
        var matchOperation1 = Aggregation.match(
                new Criteria().orOperator(
                        new Criteria().andOperator(
                                Criteria.where("result.").exists(true),
                                Criteria.where(RESULT_TEAM_ID).ne("")
                        ),
                        new Criteria().andOperator(
                                Criteria.where("count").gt(0),
                                Criteria.where(RESULT_MEMBERS).exists(true)
                        )

                )
        );
        var matchOperation2 = Aggregation.match(new Criteria(USER_ID).is(userid));

    }

}