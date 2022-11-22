package com.gocaspi.taskfly.user;



import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.validation.constraints.Email;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String team;
    private String listId;
    @Id
    private String id;
    private String userId;


    public User(String firstName, String lastName, String email, String password, String team, String listId, String userId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
    }
    public String getUserId(){return this.userId;}

    public String getFirstName(){ return this.firstName;}
    public String getLastName(){ return  this.lastName;}
    public String getEmail(){ return this.email;}
    public String getListId(){ return this.listId;}
    public String getTeam(){ return this.team;}
    public String getId() {return this.id;}
    public String getPassword(){return this.password;}
    public void setUserId(String str){ this.userId = str;}
    public void setFirstName(String str){ this.firstName = str;}
    public void setLastName(String str){ this.lastName = str;}
    public void setEmail(String str){ this.email = str;}
    public void setListId(String str){ this.listId = str;}
    public void setTeam(String str){ this.team = str;}
    public void setId(String id){this.id = id;}
    public void setPassword(String password) {
        this.password = password;
    }

    public String hashStr(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        String check = digest.digest(
                str.getBytes(StandardCharsets.UTF_8)).toString();


        return digest.digest(
                str.getBytes(StandardCharsets.UTF_8)).toString();
    }


}