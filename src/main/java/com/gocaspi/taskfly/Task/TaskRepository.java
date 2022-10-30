package com.gocaspi.taskfly.Task;

import org.springframework.data.mongodb.repository.MongoRepository;



public interface TaskRepository extends MongoRepository<Task, String> {

}
