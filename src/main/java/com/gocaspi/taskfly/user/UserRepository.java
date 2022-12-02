package com.gocaspi.taskfly.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Interface for the UserRepository that extends the MongoRepository
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     *
     * @param email email of a user
     * @return the found email from the user
     */
    @Query("{email: '?0'}")
    User findByEmail(String email);

    /**
     *
     * @param lastName lastName of a User
     * @return lastName of the user from the found email
     */
    List<User> findUserByEmail(String lastName);
}
