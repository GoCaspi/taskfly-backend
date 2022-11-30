package com.gocaspi.taskfly.task;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String LISTIDOBJ = "listIdObj";
    private static final String ADDFIELDSKEY = "$addFields";
    private static final String DEADLINEDIFF = "deadlineDiff";
    private static final String RESULT_TEAM_ID = "result.teamID";
    private static final String RESULT_MEMBERS = "result.members";
    private static final String USER_ID = "userId";


    @Autowired
    public TaskRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    private AggregationOperation addConvertedIDField(){
        return aggregationOperation -> {
            var toString = new Document("$toObjectId", "$listId");
            var id = new Document(LISTIDOBJ, toString);
            return new Document(ADDFIELDSKEY, id);
        };
    }

    private AggregationOperation addSizeField(){
        return aggregationOperation ->     new Document(ADDFIELDSKEY,
                new Document("count",
                        new Document("$cond",
                                new Document("if",
                                        new Document("$isArray", "$result.members"))
                                        .append("then",
                                                new Document("$size",
                                                        new Document("$ifNull", Arrays.asList("$result.members", Arrays.asList()))))
                                        .append("else", 0L))));

    }

    private AggregationOperation unwindResult(){
        return aggregationOperation -> new Document("$unwind", "$result");
    }

    private AggregationOperation addDateDifferenceFields(){
        return aggregationOperation -> new Document(ADDFIELDSKEY,
                new Document(DEADLINEDIFF,
                        new Document("$dateDiff",
                                new Document("startDate", "$$NOW")
                                        .append("endDate", "$deadline")
                                        .append("unit", "hour")))
                        .append("currentDate", "$$NOW"));
    }

    @Override
    public List<Task> findPrivateTasksByUserID(String userid){
        var lookupOperation = LookupOperation.newLookup()
                .from("taskCollection")
                .localField(LISTIDOBJ)
                .foreignField("_id")
                .as("result");
        var matchOperation1 = Aggregation.match(
                new Criteria().andOperator(
                        new Criteria().orOperator(
                                Criteria.where(RESULT_TEAM_ID).exists(false),
                                Criteria.where(RESULT_TEAM_ID).is("")
                        ),
                        new Criteria().orOperator(
                                Criteria.where(RESULT_MEMBERS).size(0),
                                Criteria.where(RESULT_MEMBERS).exists(false)
                        )

                )
        );
        var matchOperation2 = Aggregation.match(new Criteria(USER_ID).is(userid));
        var aggregation = Aggregation.newAggregation(addConvertedIDField(), lookupOperation, matchOperation1, matchOperation2);
        return mongoTemplate.aggregate(aggregation, "task", Task.class).getMappedResults();
    }

    @Override
    public List<Task> findSharedTasksByUserID(String userid){
        var lookupOperation = LookupOperation.newLookup()
                .from("taskCollection")
                .localField(LISTIDOBJ)
                .foreignField("_id")
                .as("result");
        var matchOperation1 = Aggregation.match(
                new Criteria().orOperator(
                        new Criteria().andOperator(
                                Criteria.where(RESULT_TEAM_ID).exists(true),
                                Criteria.where(RESULT_TEAM_ID).ne("")
                        ),
                        new Criteria().andOperator(
                                Criteria.where("count").gt(0),
                                Criteria.where(RESULT_MEMBERS).exists(true)
                        )

                )
        );
        var matchOperation2 = Aggregation.match(new Criteria(USER_ID).is(userid));
        var aggregation = Aggregation.newAggregation(addConvertedIDField(), lookupOperation, unwindResult(), addSizeField(), matchOperation1, matchOperation2);
        return mongoTemplate.aggregate(aggregation, "task", Task.class).getMappedResults();
    }

    @Override
    public List<Task> findTasksScheduledForOneWeekByUserID(String userid){
        var matchOperation1 = Aggregation.match(new Criteria(USER_ID).is(userid));
        var matchOperation2 = Aggregation.match(new Criteria().andOperator(
                Criteria.where(DEADLINEDIFF).gt(0),
                Criteria.where(DEADLINEDIFF).lt(168)
        ));
        var aggregation = Aggregation.newAggregation(addDateDifferenceFields(), matchOperation1, matchOperation2);
        return mongoTemplate.aggregate(aggregation, "task", Task.class).getMappedResults();
    }


}
