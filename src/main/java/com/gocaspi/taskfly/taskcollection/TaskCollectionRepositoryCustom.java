package com.gocaspi.taskfly.taskcollection;

import java.util.List;
/**
 * Interface for the TaskCollectionRepositoryCustom
 */
public interface TaskCollectionRepositoryCustom {
    /**
     *
     * @param ownerID taskcollection of the owner
     * @return the taskcollection to the found owner ID
     */
    List<TaskCollectionGetQuery> findByOwnerID(String ownerID);

    /**
     *
     * @param teamID taskcollections of the team
     * @return the taskcollections to the found team ID
     */
    List<TaskCollectionGetQuery> findByTeamID(String teamID);

    /**
     *
     * @param id id of the taskcollection
     * @return the taskcollection to the found taskcollection ID
     */
    TaskCollectionGetQuery findByID(String id);

}