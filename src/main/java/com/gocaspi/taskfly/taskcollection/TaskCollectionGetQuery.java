package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;

import java.util.List;

/**
 * This Class is used to construct the return of an MongoDB Lookup Operation since it contains a list of tasks
 */
public class TaskCollectionGetQuery extends TaskCollection {
    private List<Task> tasks;


    /**
     * A fully featured constructor for the TaskCollectionGetQuery Class
     * @param name to give the TaskCollection a name
     * @param teamID a foreign identifier of an TeamManagement Object to assign this TaskCollection to a Team
     * @param id identifier for the TaskCollection
     * @param ownerID a foreign identifier of a User Object, who then can manage the collection
     * @param tasks a list of tasks that have been added due to the MongoDB Lookup Operation
     */
    public TaskCollectionGetQuery(String name, String teamID, String id, String ownerID, List<Task> tasks, List<String> members) {
        this.setId(id);
        this.setName(name);
        this.setTeamID(teamID);
        this.setOwnerID(ownerID);
        this.setMembers(members);
        this.tasks = tasks;
    }

    /**
     * This method returns all tasks of the TaskCollectionGetQuery Object
     * @return tasks of the TaskCollectionGetQuery
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * This method sets all tasks of the TaskCollectionGetQuery Object
     * @param tasks that should be set
     */

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}