package com.gocaspi.taskfly.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface userRepository extends MongoRepository<user, String> {

}
