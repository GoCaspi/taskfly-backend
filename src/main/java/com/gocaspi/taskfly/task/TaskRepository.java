package com.gocaspi.taskfly.task;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Interface for the TaskRepository that extends the MongoRepository
 */
public interface TaskRepository extends MongoRepository<Task, String>, TaskRepositoryCustom {
    List<Task> getTaskByUserIdAndBody_HighPriority(String userid, Boolean priority);
    List<Task> getTasksByUserId(String userid);
}

