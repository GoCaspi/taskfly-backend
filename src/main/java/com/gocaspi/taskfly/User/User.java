package com.gocaspi.taskfly.User;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;

    public User (String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
