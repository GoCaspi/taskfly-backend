package com.gocaspi.taskfly.task;

import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Interface for the TaskRepository that extends the MongoRepository
 */
public interface TaskRepository extends MongoRepository<Task, String> {

}
