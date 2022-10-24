package com.gocaspi.taskfly.Task;

import org.springframework.data.annotation.Id;

public class Task {

    public String[] userIds;
    public String listId;
    private String topic;
    private String team;
    private String priority;
    private String description;
    private String deadline;


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
