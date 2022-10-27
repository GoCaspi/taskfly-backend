package com.gocaspi.taskfly.Task;

import org.bson.types.ObjectId;


public class Task {
    private String[] userIds;
    private String listId;
    private String topic;
    private String team;
    private String priority;
    private String description;
    private String deadline;
    private ObjectId _id;

    private String taskId;


    public Task(String[] userIds, String listId,String topic, String team, String priority, String description, String deadline, ObjectId _id){
        this.userIds = userIds;
        this.listId = listId;
        this.topic = topic;
        this.team = team;
        this.priority = priority;
        this.description = description;
        this.deadline = deadline;
        this._id = _id;
        this.taskId = getTaskIdString();
    }

    /**
     * returns the id string of the ObjectId of the task
     * @return String, id string of the ObjectId _id
     */
    public String getTaskIdString(){
        return this._id.toString();
    }

    /**
     * sets the description of the task to a provided string (text)
     * @param text, new description of the task
     */
    public void setDescription(String text){
        this.description = text;
    }

    /**
     * returns the description of the task
     * @return String, description of the task
     */
    public String getDescription(){ return this.description; }

    /**
     * sets the userIDs array of a task to the provided String-array (newUserIds)
     * @param newUserIds, new UserIds array of the task
     */
    public void addUserIdToTask(String[] newUserIds){
        this.userIds = newUserIds;
    }

    /**
     * returns an array of user ids that are assigned to that task
     * @return String[], userIds that are assigned to that task
     */
    public String[] getUserIds(){ return this.userIds; }

    /**
     * sets the topic of a task to a new topic (topic)
     * @param topic, new topic of the task
     */
    public void setTopic(String topic){
        this.topic = topic;
    }

    /**
     * returns the topic of the task
     * @return String, topic of the task
     */
    public String getTopic(){ return this.topic; }

    /**
     * sets the team of a task to a new team (team)
     * @param team, new value of the task-field: team
     */
    public void setTeam(String team){
        this.team = team;
    }

    /**
     * returns the team of the task
     * @return String, team of the task
     */
    public String getTeam(){ return this.team; }

    /**
     * sets the deadline of a task to a new deadline
     * @param deadline, new value of task-field: deadline
     */
    public void setDeadline(String deadline){
        this.deadline = deadline;
    }

    /**
     * returns the deadline of the task
     * @return String, deadline of the task
     */
    public String getDeadline(){ return this.deadline; }

    /**
     * returns the priority of that task
     * @return String, priority of the task
     */
    public String getPriority(){ return this.priority; }

    /**
     * sets the priorty value of the task to the given parameter
     * @param priority, String new priority of the task
     */
    public void setPriority(String priority){ this.priority = priority; }

    /**
     * sets the value of the listId of the task to the given input
     * @param newListId, String new listId of the task
     */
    public void setListId(String newListId){ this.listId = newListId; }

    /**
     * returns the value of the listId of the task (id of the list that contains this task)
     * @return String, listId of the task
     */
    public String getListId(){ return this.listId; }
}
