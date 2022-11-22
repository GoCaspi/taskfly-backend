package com.gocaspi.taskfly.reseter;

import com.gocaspi.taskfly.task.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResetRepository extends MongoRepository<Task, String> {
}
