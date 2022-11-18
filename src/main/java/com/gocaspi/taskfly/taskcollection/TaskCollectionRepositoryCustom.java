package com.gocaspi.taskfly.taskcollection;

import java.util.List;

public interface TaskCollectionRepositoryCustom {
    List<TaskCollectionGetQuery> findByOwnerID(String ownerID);
    List<TaskCollectionGetQuery> findByTeamID(String teamID);
    TaskCollectionGetQuery findByID(String id);

}
