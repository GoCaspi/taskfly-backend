package com.gocaspi.taskfly.User;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    @Id
    private String id;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String team;
    public String listId;
    private ObjectId _Id;
    private String userId;

    public User(String firstName, String lastName, String email, String password, String team, String listId,String userId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;

    }
public String getUserIdString(){
        return this._Id.toString();
}
public String getUserId(){
        return this.userId;
}

}
