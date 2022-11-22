package com.gocaspi.taskfly.task;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> getTaskByUserIdAndBody_Priority(String userid, Boolean priority);
}

