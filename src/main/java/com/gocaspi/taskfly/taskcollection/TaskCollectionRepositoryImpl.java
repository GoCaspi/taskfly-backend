package com.gocaspi.taskfly.taskcollection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

/**
 * Class for implementing the custom TaskCollectionRepository
 */
public class TaskCollectionRepositoryImpl implements TaskCollectionRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    private static final String OBJECTID = "objId";
    private static final String LISTID = "listId";
    private static final String COLLECTIONNAME = "taskCollection";
    private static final String FOREIGNCOLLECTION = "task";
    private static final String LOOKUPFIELD = "tasks";

    /**
     * Contructor for the TaskCollectionRepositoryImpl
     *
     * @param mongoTemplate, MongoTemplate
     */
    @Autowired
    public TaskCollectionRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    /**
     *
     * @return AggregationOperation
     */
    private AggregationOperation addConvertedIDField(){
        return aggregationOperation -> {
            var toString = new Document("$toString", "$_id");
            var id = new Document(OBJECTID, toString);
            return new Document("$addFields", id);
        };
    }

    /**
     * Returns a List<TaskCollectionGetQuery> that ownerId is assigned to the provided parameter of userId
     *
     * @param userID, String
     * @return List of taskCollectionGetQuery
     */
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

    /**
     * Returns a TaskCollectionGetQuery that is assigned to the Id (collectionId) that is provided as a parameter
     *
     * @param collectionId, String id of the collection
     * @return List of taskCollectionGetQuery
     */
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

    /**
     * Returns a List<TaskCollectionGetQuery> that is assigned to the provided teamId
     *
     * @param teamID, String
     * @return List of taskCollectionGetQuery
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
}