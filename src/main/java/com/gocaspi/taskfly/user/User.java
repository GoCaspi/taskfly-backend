package com.gocaspi.taskfly.user;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String team;
    private String listId;

    private ObjectId id;
    private String userId;
    private String topic;

    public User(String firstName, String lastName, String email, String password, String team, String listId, String userId, ObjectId id) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
        this.id = id;

    }
    public String getUserIdString(){
        return this.id.toString();
    }

    public void setTopic(String topic){
        this.topic = topic;
    }

}