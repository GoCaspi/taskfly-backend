package com.gocaspi.taskfly.teammanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Interface for the TeamManagementRepository that extends the MongoRepository
 */
public interface TeamManagementRepository extends MongoRepository<TeamManagement, String> {
    List<TeamManagement> findByUserID(String s);
    List<TeamManagement> findByMembersContaining(String s);
    boolean existsByUserID(String userID);
    boolean existsByMembers(String members);
}
