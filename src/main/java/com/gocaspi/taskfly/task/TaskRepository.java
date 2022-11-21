package com.gocaspi.taskfly.task;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> getTaskByIdAndBody_Priority(ObjectId id, Boolean priority);
}

