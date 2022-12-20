package com.gocaspi.taskfly.task;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

/**
 * this class defines the functions defined on TaskRepositoryCustom. You can mainly find mongo aggregation pipelines here for more complex queries.
 */
public class TaskRepositoryImpl implements TaskRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String LISTIDOBJ = "listIdObj";
    private static final String ADDFIELDSKEY = "$addFields";
    private static final String DEADLINEDIFF = "deadlineDiff";
    private static final String RESULT_TEAM_ID = "result.teamID";
    private static final String RESULT_MEMBERS = "result.members";
    private static final String USER_ID = "userId";

    /**
     * the constructor takes an mongoTemplate as an input which contains the mongodb database connection
     * @param mongoTemplate
     */
    @Autowired
    public TaskRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * this function returns a mongo aggregation which converts listId fields into Object ID's for later use
     * @return a addFields MongoDB aggregation
     */
    private AggregationOperation addConvertedIDField(){
        return aggregationOperation -> {
            var toString = new Document("$toObjectId", "$listId");
            var id = new Document(LISTIDOBJ, toString);
            return new Document(ADDFIELDSKEY, id);
        };
    }

    /**
     * this function returns a mongo aggregation which gets the size of a members array and to evaluate it later
     * @return a addFields MongoDB aggregation
     */
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

    /**
     * this function returns a mongo aggregation which unwinds the result Object from the Lookup Pipeline, to access the data later within the aggregation pipeline
     * @return a unwind MongoDB aggregation
     */
    private AggregationOperation unwindResult(){
        return aggregationOperation -> new Document("$unwind", "$result");
    }

    /**
     * this function returns a mongo aggregation which calculates the difference between two dates and writes the result into a new field
     * @return a addFields MongoDB aggregation
     */
    private AggregationOperation addDateDifferenceFields(){
        return aggregationOperation -> new Document(ADDFIELDSKEY,
                new Document(DEADLINEDIFF,
                        new Document("$dateDiff",
                                new Document("startDate", "$$NOW")
                                        .append("endDate", "$deadline")
                                        .append("unit", "hour")))
                        .append("currentDate", "$$NOW"));
    }

    /**
     * this function executes a mongodb aggregation pipeline which returns all tasks which aren't shared to the user in any way.
     * - the task is located within a list, but the list have no members and also no assigned team
     * - the userID of the task matches the userid which is being passed to the function
     * @param userid the identifier of the user of interest
     * @return a list of Tasks which aren't visible to anyone else.
     */
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

    /**
     * this function executes a mongodb aggregation pipeline which returns all tasks which are shared and visible to the
     * requested user.
     * - the owner is another user than the requested user, and the user saved within the members.
     * - the task is located within a taskCollection where other users are listed within the member array or is the owner of the taskCollection
     * - the taskCollection is shared to a team where the user is a member of
     * @param userid returns all tasks the user have been assigned to
     * @return a list of tasks which are assigned to the user and are visible to other users
     */
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

    /**
     * this function executes a mongo aggregation pipeline which returns all tasks that are assigned to an user and the deadline is due
     * within the next week starting from the current date.
     * @param userid assigned tasks for this user
     * @return a list of tasks which have been scheduled within the next 7 days.
     */
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
