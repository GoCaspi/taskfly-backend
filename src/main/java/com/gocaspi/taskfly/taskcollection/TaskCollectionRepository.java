package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Interface of the TaskCollectionRepository that extends the MongoRepository
 */
public interface TaskCollectionRepository extends MongoRepository<TaskCollection, String>, TaskCollectionRepositoryCustom {

}
