package com.gocaspi.taskfly.task;

import java.util.List;

public interface TaskRepositoryCustom {
    /**
     *
     * @param userid of the user
     * @return the private task from the user
     */
    List<Task> findPrivateTasksByUserID(String userid);

    /**
     *
     * @param userid of the user
     * @return the tasks that can be processed by several users
     */
    List<Task> findSharedTasksByUserID(String userid);

    /**
     *
     * @param userid of the user
     * @return shows the tasks of a user that are scheduled for a week
     */
    List<Task> findTasksScheduledForOneWeekByUserID(String userid);
}
