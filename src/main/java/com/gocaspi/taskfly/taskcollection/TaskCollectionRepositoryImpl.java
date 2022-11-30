package com.gocaspi.taskfly.taskcollection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

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
}