package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;

import java.util.List;
public class TaskCollectionGetQuery {
    private List<Task> tasks;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
