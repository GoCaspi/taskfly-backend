package com.gocaspi.taskfly.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Objects;


@Document
public class User {
    private Userbody body;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Indexed(unique = true)
    private String email;
    @NotBlank
    private String password;
    private String srole;
    private boolean reseted;
@Id
    private String id;

public static class Userbody{
    private String team;

     public Userbody(String team){
         this.team = team;
     }

    public Userbody(){
        this.team = "";
    }
    public void setTeam(String str){ this.team = str;}
    public String getTeam(){ return this.team;}

}
    /**
     * Constractor for User
     * @param firstName firstName for the user
     * @param lastName lastName for the user
     * @param email email for the user
     * @param password password for the user
     * @param srole Role(WRITE/READ) for the user
     * @param body body for the user
     * @param reseted reseted for the user
     */
    public User (String firstName, String lastName, String email, String password, String srole, Userbody body, Boolean reseted) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.srole = srole;
        this.body = Objects.requireNonNullElse(body, new Userbody());
        this.reseted = Objects.requireNonNullElse(reseted, false);
    }
public void setBody(Userbody body){
    this.body=body;
}
public Userbody getBody(){
    return this.body;
}
    public String getFirstName(){ return this.firstName;}
    public void setFirstName(String str){ this.firstName = str;}
    /**
     * returns the lastName of the user
     *
     * @return String, lastName of the user
     */
    public String getLastName(){ return  this.lastName;}
    /**
     * sets the lastName of a user to a new lastName
     *
     * @param str, new value of user-field: lastName
     */
    public void setLastName(String str){ this.lastName = str;}
    /**
     * returns the email of the user
     *
     * @return String, email of the user
     */
    public String getEmail(){ return this.email;}
    /**
     * returns the team of the user
     *
     * @return String, team of the user
     */
    public String getTeam(){ return this.body.team;}
    /**
     * returns the id of the user
     *
     * @return String, id of the user
     */
    public String getId() {return this.id;}
    /**
     * returns the password of the user
     *
     * @return String, password of the user
     */
    public String getPassword(){return this.password;}
    /**
     * sets the email of a user to a new email
     *
     * @param str, new value of user-field: email
     */
    public void setEmail(String str){ this.email = str;}
    /**
     * sets the team of a user to a new team
     *
     * @param str, new value of user-field: team
     */
    public void setTeam(String str){ this.body.team = str;}
    /**
     * sets the id of a user to a new id
     *
     * @param id, new value of user-field: id
     */
    public void setId(String id){this.id = id;}
    /**
     * sets the password of a user to a new password
     *
     * @param password, new value of user-field: password
     */

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getReseted(){
        return this.reseted;
    }
    public void setReseted(Boolean b){
        this.reseted = b;
    }
    public String getSrole() {
        return srole;
    }

    public void setSrole(String srole) {
        this.srole = srole;
    }



}