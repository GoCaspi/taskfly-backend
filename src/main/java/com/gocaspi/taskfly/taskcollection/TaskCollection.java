package com.gocaspi.taskfly.taskcollection;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * The Class for TaskCollections. A TaskCollection is a list of Tasks and is used to give tasks a category and share them
 * with other users and teams.
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
     * Fully featured constructor for the TaskCollection Class
     * @param id identifier for the TaskCollection
     * @param name to give the TaskCollection a name
     * @param teamID a foreign identifier of an TeamManagement Object to assign this TaskCollection to a Team
     * @param ownerID a foreign identifier of a User Object, who then can manage the collection
     * @param members a list of foreign identifier of User Objects, who can read and write all tasks of the TaskCollection
     */
    public TaskCollection(String id, String name, String teamID, String ownerID, List<String> members) {
        this.id = id;
        this.name = name;
        this.teamID = teamID;
        this.ownerID = ownerID;
        this.members = members;
    }

    /**
     * Empty Constructor for the TaskCollection Class
     */
    public TaskCollection() {

    }

    /**
     * This method returns the ID of the TaskCollection Object
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * This method sets the ID of the TaskCollection Object
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method returns the name of the TaskCollection Object.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of the TaskCollection Object
     * @param name of the TaskCollection
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the teamID of the TaskCollection Object
     * @return teamID
     */
    public String getTeamID() {
        return teamID;
    }

    /**
     * This method sets the teamID of the TaskCollection Object
     * @param teamID of the TaskCollection
     */
    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    /**
     * This method returns the ownerID of the TaskCollection Object
     * @return ownerID
     */
    public String getOwnerID() {
        return ownerID;
    }

    /**
     * This method sets the ownerID of the TaskCollection Object
     * @param ownerID of the TaskCollection
     */
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    /**
     * This method gets the member list of the TaskCollection Object
     * @return members
     */
    public List<String> getMembers() {
        return this.members;
    }

    /**
     * This method sets the member list of the TaskCollection Object
     * @param members of the TaskCollection
     */
    public void setMembers(List<String> members){
        this.members = members;
    }
}
