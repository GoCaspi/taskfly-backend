package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskCollectionRepository extends MongoRepository<TaskCollection, String>, TaskCollectionRepositoryCustom {


}
