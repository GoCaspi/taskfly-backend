package com.gocaspi.taskfly.user;

import com.gocaspi.taskfly.models.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
 Optional <User> findByEmail(String email);

}
