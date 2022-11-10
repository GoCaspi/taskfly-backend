package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskCollectionRepository extends MongoRepository<TaskCollection, String>, TaskCollectionRepositoryCustom {


}
