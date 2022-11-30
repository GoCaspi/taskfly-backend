package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class TaskCollection {


    @Id
    private String id;
    @NotBlank(message = "{name.notblank}")
    private String name;
    private String teamID;
    @NotBlank(message = "{ownerid.notblank}")
    private String ownerID;
    private List<String> members;

    public TaskCollection(String id, String name, String teamID, String ownerID, List<String> members) {
        this.id = id;
        this.name = name;
        this.teamID = teamID;
        this.ownerID = ownerID;
        this.members = members;
    }

    public TaskCollection() {

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

    public List<String> getMembers() {
        return this.members;
    }

    public void setMembers(List<String> members){
        this.members = members;
    }
}
