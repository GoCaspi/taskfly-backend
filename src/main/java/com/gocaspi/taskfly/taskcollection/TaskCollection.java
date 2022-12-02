package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Class for TaskCollection
 */
public class TaskCollection {


    @Id
    private String id;
    @NotBlank(message = "{name.notblank}")
    private String name;
    private String teamID;
    @NotBlank(message = "{ownerid.notblank}")
    private String ownerID;
    private List<String> members;
    /**
     * Constractor for TaskCollection
     *
     * @param id id of the TaskCollection
     * @param name name of the TaskCollection
     * @param teamID teamID of the TaskCollection
     * @param ownerID ownerID of the TaskCollection
     * @param members members of the TaskCollection
     */
    public TaskCollection(String id, String name, String teamID, String ownerID, List<String> members) {
        this.id = id;
        this.name = name;
        this.teamID = teamID;
        this.ownerID = ownerID;
        this.members = members;
    }
    /**
     * Empty Constractor TaskCollection for testing
     */
    public TaskCollection() {

    }
    /**
     * returns the id of the TaskCollection
     *
     * @return String, id of the TaskCollection
     */
    public String getId() {
        return id;
    }

    /**
     * sets the id of a TaskCollection to a new id
     *
     * @param id, new value of TaskCollection-field: id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * returns the name of the TaskCollection
     *
     * @return String, name of the TaskCollection
     */
    public String getName() {
        return name;
    }
    /**
     * sets the name of a TaskCollection to a new name
     *
     * @param name, new value of TaskCollection-field: name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * returns the teamID of the TaskCollection
     *
     * @return String, teamID of the TaskCollection
     */
    public String getTeamID() {
        return teamID;
    }
    /**
     * sets the teamID of a TaskCollection to a new teamID
     *
     * @param teamID, new value of TaskCollection-field: teamID
     */
    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }
    /**
     * returns the ownerID of the TaskCollection
     *
     * @return String, ownerID of the TaskCollection
     */
    public String getOwnerID() {
        return ownerID;
    }
    /**
     * sets the ownerID of a TaskCollection to a new ownerID
     *
     * @param ownerID, new value of TaskCollection-field: ownerID
     */
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
    /**
     * returns the members of the TaskCollection
     *
     * @return String, members of the TaskCollection
     */
    public List<String> getMembers() {
        return this.members;
    }

    /**
     * sets the members of a TaskCollection to new members
     *
     * @param members, new value of TaskCollection-field: members
     */
    public void setMembers(List<String> members){
        this.members = members;
    }
}
