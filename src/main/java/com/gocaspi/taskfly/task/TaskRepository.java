package com.gocaspi.taskfly.task;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Interface for the TaskRepository that extends the MongoRepository
 */
public interface TaskRepository extends MongoRepository<Task, String>, TaskRepositoryCustom {
    /**
     *
     * @param userid of the user
     * @param priority priority of user
     * @return all tasks where the priority is set to true to a specific user
     */
    List<Task> getTaskByUserIdAndBody_HighPriority(String userid, Boolean priority);

    /**
     *
     * @param userid of the user
     * @return all tasks for the searched user id
     */
    List<Task> getTasksByUserId(String userid);
}

