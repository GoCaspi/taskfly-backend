package com.gocaspi.taskfly.taskcollection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class TaskCollectionRepositoryImpl implements TaskCollectionRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TaskCollectionRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TaskCollectionGetQuery> findByOwnerID(String userID){
        System.out.println("this should trigger");
        AggregationOperation addFieldsOperation = context -> {
            Document toString = new Document("$toString", "$_id");
            Document id = new Document("objId", toString);
            return new Document("$addFields", id);
        };

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task")
                .localField("objId")
                .foreignField("listId")
                .as("tasks");
        Aggregation aggregation = Aggregation.newAggregation(addFieldsOperation, lookupOperation);
        return mongoTemplate.aggregate(aggregation, "taskCollection", TaskCollectionGetQuery.class).getMappedResults();
    }




}
