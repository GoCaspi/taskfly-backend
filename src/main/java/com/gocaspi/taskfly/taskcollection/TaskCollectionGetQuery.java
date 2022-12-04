package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;

import java.util.List;

/**
 * Class for the custom query methods TaskCollectionGetQuery
 */
public class TaskCollectionGetQuery {
    private String name;
    private String teamID;
    private String id;
    private String ownerID;
    private List<Task> tasks;


    /**
     * Construtor for the CollectionGetQuery
     *
     * @param name, String name of the collection
     * @param teamID, String teamId of the collection
     * @param id, String id of the collection
     * @param ownerID, id of the creater of the collection
     * @param tasks, List containg the tasks of the collection
     */
    public TaskCollectionGetQuery(String name, String teamID, String id, String ownerID, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.teamID = teamID;
        this.ownerID = ownerID;
        this.tasks = tasks;
    }

    /**
     * Returns the List of Tasks of the collection
     *
     * @return List all tasks assigned to that collection
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets a input of list<Task> to the field of tasks of this taskCollection
     *
     * @param tasks, List that is set to the task field of the collection
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns the name of the collection
     *
     * @return String, name of the collection
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the collection to the given input
     *
     * @param name, String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id of the team that is assigned to the collection
     *
     * @return String, teamId
     */
    public String getTeamID() {
        return teamID;
    }

    /**
     * Sets the field teamId of the collection to the input value
     *
     * @param teamID, String
     */
    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    /**
     * Returns the id of the collection
     *
     * @return id, String
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the collection to the input value
     *
     * @param id, String
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the ownerId of the colelction
     *
     * @return String ownerId
     */
    public String getOwnerID() {
        return ownerID;
    }

    /**
     * Sets the field ownerId of the collection to the input value
     *
     * @param ownerID, String
     */
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

}