package com.gocaspi.taskfly.user;


import org.bson.types.ObjectId;



public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String team;
    private String listId;

    private ObjectId id;
    private String userId;


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
    public String getUserId(){return this.userId;}
    public String getUserIdString(){
        return this.id.toString();
    }
    public String getFirstName(){ return this.firstName;}
    public String getLastName(){ return  this.lastName;}
    public String getEmail(){ return this.email;}
    public String getListId(){ return this.listId;}
    public String getTeam(){ return this.team;}
    public ObjectId getId() {return this.id;}
    public String getPassword(){return this.password;}
    public void setUserId(String str){ this.userId = str;}
    public void setFirstName(String str){ this.firstName = str;}
    public void setLastName(String str){ this.lastName = str;}
    public void setEmail(String str){ this.email = str;}
    public void setListId(String str){ this.listId = str;}
    public void setTeam(String str){ this.team = str;}
    public void setId(ObjectId id){this.id = id;}

    public void setPassword(String password) {
        this.password = password;
    }
    //   public void setId(ObjectId str){ this.id = str;}


}