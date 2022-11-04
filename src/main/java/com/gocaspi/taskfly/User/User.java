package com.gocaspi.taskfly.User;


import lombok.Data;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String team;
    public String listId;
    private ObjectId _id;
    private String userId;
    private String topic;

    public User(String firstName, String lastName, String email, String password, String team, String listId,String userId,ObjectId _id) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
        this._id=_id;
        this.topic = topic;

    }
    public String getUserIdString(){
        return this._id.toString();
    }
    public String getUserId(){
        return this.userId;
    }
    public void setTopic(String topic){
        this.topic = topic;
    }

}