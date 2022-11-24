package com.gocaspi.taskfly.task;

import com.mongodb.internal.operation.AggregateOperation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public TaskRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    private AggregationOperation addConvertedIDField(){
        return aggregationOperation -> {
            var toString = new Document("$toObjectId", "$listId");
            var id = new Document("listIdObj", toString);
            return new Document("$addFields", id);
        };
    }

    private AggregationOperation addSizeField(){
        return aggregationOperation -> new Document("$addFields",
                 new Document("count",
                         new Document("$cond",
                                 new Document("if",
                                         new Document("$isArray", "$result.members"))
                                         .append("then",
                                                 new Document("$size", "$result.members"))
                                         .append("else", 0L))));

    }

    private AggregationOperation unwindResult(){
        return aggregationOperation -> new Document("$unwind", "$result");
    }

    @Override
    public List<Task> findPrivateTasksByUserID(String userid){
        var lookupOperation = LookupOperation.newLookup()
                .from("taskCollection")
                .localField("listIdObj")
                .foreignField("_id")
                .as("result");
        var matchOperation1 = Aggregation.match(
                new Criteria().andOperator(
                        new Criteria().orOperator(
                                Criteria.where("result.teamID").exists(false),
                                Criteria.where("result.teamID").is("")
                        ),
                        new Criteria().orOperator(
                                Criteria.where("result.members").size(0),
                                Criteria.where("result.members").exists(false)
                        )

                )
        );
        var matchOperation2 = Aggregation.match(new Criteria("userId").is(userid));
        var aggregation = Aggregation.newAggregation(addConvertedIDField(), lookupOperation, matchOperation1, matchOperation2);
        return mongoTemplate.aggregate(aggregation, "task", Task.class).getMappedResults();
    }

    @Override
    public List<Task> findSharedTasksByUserID(String userid){
        var lookupOperation = LookupOperation.newLookup()
                .from("taskCollection")
                .localField("listIdObj")
                .foreignField("_id")
                .as("result");
        var matchOperation1 = Aggregation.match(
                new Criteria().orOperator(
                        new Criteria().andOperator(
                                Criteria.where("result.teamID").exists(true),
                                Criteria.where("result.teamID").ne("")
                        ),
                        new Criteria().andOperator(
                                Criteria.where("count").gt(0),
                                Criteria.where("result.members").exists(true)
                        )

                )
        );
        var matchOperation2 = Aggregation.match(new Criteria("userId").is(userid));
        var aggregation = Aggregation.newAggregation(addConvertedIDField(), lookupOperation, unwindResult(), addSizeField(), matchOperation1, matchOperation2);
        return mongoTemplate.aggregate(aggregation, "task", Task.class).getMappedResults();
    }


}
