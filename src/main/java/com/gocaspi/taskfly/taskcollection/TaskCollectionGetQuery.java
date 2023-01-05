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
     * @param tasks a list of tasks that have been added due to the MongoDB Lookup Operation
     */
    public TaskCollectionGetQuery(String id, String name, String teamID, String ownerID, List<String> members, List<Task> tasks){
        this.setId(id);
        this.setName(name);
        this.setTeamID(teamID);
        this.setOwnerID(ownerID);
        this.setMembers(members);
        this.tasks = tasks;
    }
    public TaskCollectionGetQuery(List<Task> tasks) {
        this.tasks = tasks;
    }
    public TaskCollectionGetQuery(TaskCollection tc){
        super(tc);
    }

    public TaskCollectionGetQuery(){

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