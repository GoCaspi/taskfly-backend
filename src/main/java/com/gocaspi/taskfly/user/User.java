package com.gocaspi.taskfly.user;



import com.gocaspi.taskfly.models.Role;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;


public class User {

    private String username;
    private String lastName;
    private String email;
    private String password;
    private String team;
    private String listId;
@Id
    private String id;
    private String userId;

    private Set<Role> srole = new HashSet<>();


    public User(String username, String email, String password, String team) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
        this.srole = srole;
    }
    public String getUserId(){return this.userId;}

    public String getUsername(){ return this.username;}
    public String getLastName(){ return  this.lastName;}
    public String getEmail(){ return this.email;}
    public String getListId(){ return this.listId;}
    public String getTeam(){ return this.team;}
    public String getId() {return this.id;}
    public String getPassword(){return this.password;}
    public void setUserId(String str){ this.userId = str;}
    public void setUsername(String str){ this.username = str;}
    public void setLastName(String str){ this.lastName = str;}
    public void setEmail(String str){ this.email = str;}
    public void setListId(String str){ this.listId = str;}
    public void setTeam(String str){ this.team = str;}
   public void setId(String id){this.id = id;}

    public void setPassword(String password) {
        this.password = password;
    }
    public Set<Role> getSrole(){

        return srole;
    }



}