package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.annotation.Id;

import java.util.List;

public class TaskCollection {
    @Id
    private String id;
    private String name;
    private List<String> members;
    private String teamID;
    private String ownerID;
    private List<String> taskIDs;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public List<String> getTaskIDs() {
        return taskIDs;
    }

    public void setTaskIDs(List<String> taskIDs) {
        this.taskIDs = taskIDs;
    }




}
