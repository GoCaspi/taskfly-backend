package com.gocaspi.taskfly.Task;

import org.bson.types.ObjectId;


public class Task {

    public String[] userIds;
    public String listId;
    public String topic;
    public String team;
    public String priority;
    public String description;
    public String deadline;
    public ObjectId _id;

    public String taskId;



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
     * sets the userIDs array of a task to the provided String-array (newUserIds)
     * @param newUserIds, new UserIds array of the task
     */
    public void addUserIdToTask(String[] newUserIds){
        this.userIds = newUserIds;
    }

    /**
     * sets the topic of a task to a new topic (topic)
     * @param topic, new topic of the task
     */
    public void setTopic(String topic){
        this.topic = topic;
    }

    /**
     * sets the team of a task to a new team (team)
     * @param team, new value of the task-field: team
     */
    public void setTeam(String team){
        this.team = team;
    }

    /**
     * sets the deadline of a task to a new deadline
     * @param deadline, new value of task-field: deadline
     */
    public void setDeadline(String deadline){
        this.deadline = deadline;
    }
}
