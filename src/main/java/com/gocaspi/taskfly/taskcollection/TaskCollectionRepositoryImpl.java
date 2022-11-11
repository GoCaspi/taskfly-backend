package com.gocaspi.taskfly.taskcollection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class TaskCollectionRepositoryImpl implements TaskCollectionRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TaskCollectionRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    private AggregationOperation addConvertedIDField(){
        return AggregationOperation -> {
            Document toString = new Document("$toString", "$_id");
            Document id = new Document("objId", toString);
            return new Document("$addFields", id);
        };
    }
    @Override
    public List<TaskCollectionGetQuery> findByOwnerID(String userID){
        MatchOperation match = Aggregation.match(new Criteria("ownerID").is(userID));

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task")
                .localField("objId")
                .foreignField("listId")
                .as("tasks");
        Aggregation aggregation = Aggregation.newAggregation(match, addConvertedIDField(), lookupOperation);
        return mongoTemplate.aggregate(aggregation, "taskCollection", TaskCollectionGetQuery.class).getMappedResults();
    }

    @Override
    public TaskCollectionGetQuery findByID(String collectionId){
        MatchOperation match = Aggregation.match(new Criteria("_id").is(collectionId));

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task")
                .localField("objId")
                .foreignField("listId")
                .as("tasks");
        Aggregation aggregation = Aggregation.newAggregation(match, addConvertedIDField(), lookupOperation);
        return mongoTemplate.aggregate(aggregation, "taskCollection", TaskCollectionGetQuery.class).getUniqueMappedResult();
    }
}
