package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
public class TaskCollection {


    @Id
    private String id;
    @NotBlank
    private String name;
    private String teamID;
    @NotBlank
    private String ownerID;

    public TaskCollection(String id, String name, String teamID, String ownerID) {
        this.id = id;
        this.name = name;
        this.teamID = teamID;
        this.ownerID = ownerID;
    }

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
}
