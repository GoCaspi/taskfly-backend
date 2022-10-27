package com.gocaspi.taskfly.Task;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

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

    public String getTaskIdString(){
        return this._id.toString();
    }
}
