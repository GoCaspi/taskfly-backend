package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.task.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResetRepository extends MongoRepository<Task, String>  {
}
