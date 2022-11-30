package com.gocaspi.taskfly.teammanagement;

import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * Interface for the TeamManagementRepository that extends the MongoRepository
 */
public interface TeamManagementRepository extends MongoRepository<TeamManagement, String> {

}
