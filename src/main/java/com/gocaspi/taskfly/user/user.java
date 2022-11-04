package com.gocaspi.taskfly.user;


import lombok.Data;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class user {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String team;
    public String listId;
    private ObjectId id;
    private String userId;
    private String topic;

    public user(String firstName, String lastName, String email, String password, String team, String listId, String userId, ObjectId id) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
        this.id=id;

    }
    public String getUserIdString(){
        return this.id.toString();
    }
    public String getUserId(){
        return this.userId;
    }
    public void setTopic(String topic){
        this.topic = topic;
    }

}