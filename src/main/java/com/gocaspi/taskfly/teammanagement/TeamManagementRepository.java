package com.gocaspi.taskfly.teammanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamManagementRepository extends MongoRepository<TeamManagement, String> {

}
