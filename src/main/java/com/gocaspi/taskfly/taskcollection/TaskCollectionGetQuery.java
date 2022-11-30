package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;

import java.util.List;
public class TaskCollectionGetQuery {
    private String name;
    private String teamID;
    private String id;
    private String ownerID;
    private List<Task> tasks;



    public TaskCollectionGetQuery(String name, String teamID, String id, String ownerID, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.teamID = teamID;
        this.ownerID = ownerID;
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

}