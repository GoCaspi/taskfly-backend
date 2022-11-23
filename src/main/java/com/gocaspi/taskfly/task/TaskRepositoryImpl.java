package com.gocaspi.taskfly.task;

import com.mongodb.internal.operation.AggregateOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public TaskRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Task> findPrivateTasksByUserID(String userid){
        MatchOperation matchOperation = Aggregation.match(new Criteria("userId").is(userid));


    }


}
