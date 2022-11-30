package com.gocaspi.taskfly.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
    private Userbody body;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String srole;
@Id
    private String id;

public static class Userbody implements java.io.Serializable{
    private String team;
    private String listId;
    private String userId;



     public Userbody(String team,String listId,String userId){

         this.team = team;
         this.listId = listId;
         this.userId = userId;
     }

    public void setListId(String str){ this.listId = str;}
    public void setTeam(String str){ this.team = str;}
    public void setUserId(String str){this.userId=str;}
    public String getUserId(){return this.userId;}
    public String getListId(){ return this.listId;}
    public String getTeam(){ return this.team;}

}
    public User(String firstName, String lastName, String email, String password ,String srole,Userbody body) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.srole = srole;
        this.body = body;
    }
public void setBody(Userbody body){
    this.body=body;
}
public Userbody getBody(){
    return this.body;
}
    public String getFirstName(){ return this.firstName;}
    public String getLastName(){ return  this.lastName;}
    public String getEmail(){ return this.email;}
    public String getId() {return this.id;}




    public String getPassword(){return this.password;}




    public void setFirstName(String str){ this.firstName = str;}
    public void setLastName(String str){ this.lastName = str;}
    public void setEmail(String str){ this.email = str;}
   public void setId(String id){this.id = id;}

    public void setPassword(String password) {
        this.password = password;
    }
    public String getSrole() {
        return srole;
    }

    public void setSrole(String srole) {
        this.srole = srole;
    }



}