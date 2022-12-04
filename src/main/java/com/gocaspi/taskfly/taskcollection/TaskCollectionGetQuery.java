package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;

import java.util.List;
/**
 * Class for TaskCollectionGetQuery
 */
public class TaskCollectionGetQuery {
    private String name;
    private String teamID;
    private String id;
    private String ownerID;
    private List<Task> tasks;
    /**
     * Constractor for TaskCollectionGetQuery
     *
     * @param id id of the TaskCollectionGetQuery
     * @param name name of the TaskCollectionGetQuery
     * @param teamID teamID of the TaskCollectionGetQuery
     * @param ownerID ownerID of the TaskCollectionGetQuery
     * @param tasks tasks of the TaskCollectionGetQuery
     */
    public TaskCollectionGetQuery(String name, String teamID, String id, String ownerID, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.teamID = teamID;
        this.ownerID = ownerID;
        this.tasks = tasks;
    }
    /**
     * returns tasks of the TaskCollectionGetQuery
     *
     * @return List, tasks of the TaskCollectionGetQuery
     */
    public List<Task> getTasks() {
        return tasks;
    }
    /**
     * sets the tasks of a TaskCollection to new tasks
     *
     * @param tasks, new value of TaskCollection-field: tasks
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    /**
     * returns the name of the TaskCollectionGetQuery
     *
     * @return String, name of the TaskCollectionGetQuery
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
     * returns the team ID of the TaskCollectionGetQuery
     *
     * @return String, team ID of the TaskCollectionGetQuery
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
     * returns the id of the TaskCollectionGetQuery
     *
     * @return String, id of the TaskCollectionGetQuery
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
     * returns the Owner ID of the TaskCollectionGetQuery
     *
     * @return String, Ownder ID of the TaskCollectionGetQuery
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

}