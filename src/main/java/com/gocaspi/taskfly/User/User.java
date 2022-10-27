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
    private String email;
    private String password;

    public User (String firstName, String lastName,String email,String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email=email;
        this.password=password;
    }
}
