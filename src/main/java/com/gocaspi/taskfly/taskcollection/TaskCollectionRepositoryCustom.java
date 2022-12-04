package com.gocaspi.taskfly.taskcollection;

import java.util.List;

/**
 * Interface of the TaskCollectionRepositoryCustom
 */
public interface TaskCollectionRepositoryCustom {
    /**
     *  Returns a List<TaskCollectionGetQuery> that is assigned to the provided ownerId
     *
     * @param ownerID, String
     * @return List of TaskColelctionGetQuery
     */
    List<TaskCollectionGetQuery> findByOwnerID(String ownerID);

    /**
     *  Returns a List<TaskCollectionGetQuery> that is assigned to the provided teamId
     *
     * @param teamID, String
     * @return List of TaskColelctionGetQuery
     */
    List<TaskCollectionGetQuery> findByTeamID(String teamID);

    /**
     * Returns the TaskColletionGetQuery that is assigned to the provided id
     *
     * @param id, String
     * @return TaskCollectionGetQuery
     */
    TaskCollectionGetQuery findByID(String id);

}