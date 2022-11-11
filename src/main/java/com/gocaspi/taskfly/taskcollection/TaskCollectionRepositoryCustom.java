package com.gocaspi.taskfly.taskcollection;

import java.util.List;

public interface TaskCollectionRepositoryCustom {
    List<TaskCollectionGetQuery> findByOwnerID(String ownerID);
    TaskCollectionGetQuery findByID(String id);
}
