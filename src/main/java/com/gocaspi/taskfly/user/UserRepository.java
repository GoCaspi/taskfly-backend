package com.gocaspi.taskfly.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Interface for the UserRepository that extends the MongoRepository
 */
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{email: '?0'}")
    User findByEmail(String email);


    List<User> findUserByEmail(String lastName);
}
