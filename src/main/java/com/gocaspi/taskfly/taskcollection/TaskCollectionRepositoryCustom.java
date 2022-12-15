package com.gocaspi.taskfly.taskcollection;

import java.util.List;

public interface TaskCollectionRepositoryCustom {
    List<TaskCollectionGetQuery> findByOwnerID(String ownerID);
    List<TaskCollectionGetQuery> findByTeamID(String teamID);
    List<TaskCollectionGetQuery> findByUserID(String userID);
    TaskCollectionGetQuery findByID(String id);
    Boolean hasAccessToCollection(String userid, String collectionID);

}