package com.gocaspi.taskfly.Task;

import org.springframework.data.annotation.Id;

public class Task {

    public String[] userIds;
    public String listId;
    public String topic;
    public String team;
    public String priority;
    public String description;
    public String deadline;


    public Task(String[] userIds, String listId,String topic, String team, String priority, String description, String deadline){
        this.userIds = userIds;
        this.listId = listId;
        this.topic = topic;
        this.team = team;
        this.priority = priority;
        this.description = description;
        this.deadline = deadline;
    }
}
