package com.gocaspi.taskfly.user;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {
    private final MongoTemplate mongoTemplate;

    public UserRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List findUserByEmail(String email){
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
