package com.gocaspi.taskfly.task;

import java.util.List;

public interface TaskRepositoryCustom {
    List<Task> findPrivateTasksByUserID(String userid);
    List<Task> findSharedTasksByUserID(String userid);
    List<Task> findTasksScheduledForOneWeekByUserID(String userid);
}
