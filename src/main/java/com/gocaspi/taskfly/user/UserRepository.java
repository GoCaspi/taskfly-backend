package com.gocaspi.taskfly.user;

import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * Interface for the UserRepository that extends the MongoRepository
 */
public interface UserRepository extends MongoRepository<User, String> {

}
