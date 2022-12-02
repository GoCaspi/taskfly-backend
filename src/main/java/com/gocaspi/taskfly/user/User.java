package com.gocaspi.taskfly.user;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class for User
 */
@Document
public class User  {
    private Userbody body;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String srole;
    private boolean reseted;
@Id
    private String id;

    /**
     * Class for Userbody
     */
    public static class Userbody implements java.io.Serializable{
        /**
         * String team of User
         */
        private String team;
        /**
         * Constractor for Userbody
         *
         * @param team team of the User
         */
     public Userbody(String team){
         this.team = team;
     }
        /**
         * sets the team of a user to a new team
         *
         * @param team, new value of user-field: team
         */
    public void setTeam(String team){ this.team = team;}
        /**
         * returns the team of the user
         *
         * @return String, team of the user
         */
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
    public User(String firstName, String lastName, String email, String password ,String srole,Userbody body,Boolean reseted) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.srole = srole;
        this.body = body;
        this.reseted = reseted;
    }
    /**
     * sets the body of Userbody to a new body
     *
     * @param body, new value of the Userbody-field: body
     */
public void setBody(Userbody body){
    this.body=body;
}
    /**
     * returns the body of the Userbody
     *
     * @return String, body of the Userbody
     */
public Userbody getBody(){
    return this.body;
}
    /**
     * returns the firstName of the user
     *
     * @return String, firstName of the user
     */
    public String getFirstName(){ return this.firstName;}
    /**
     * sets the firstName of a user to a new firstName
     *
     * @param firstName, new value of user-field: firstName
     */
    public void setFirstName(String firstName){ this.firstName = firstName;}
    /**
     * returns the lastName of the user
     *
     * @return String, lastName of the user
     */
    public String getLastName(){ return  this.lastName;}
    /**
     * sets the lastName of a user to a new lastName
     *
     * @param lastName, new value of user-field: lastName
     */
    public void setLastName(String lastName){ this.lastName = lastName;}
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
    /**
     * returns true or false if a user has already reseted his password or if the reset has worked
     *
     * @return boolen true or false
     */
    public boolean getReseted(){
        return this.reseted;
    }
    /**
     * sets the reset of a user to a reset true or false
     *
     * @param reseted, new value of user-field: reseted
     */
    public void setReseted(Boolean reseted){
        this.reseted = reseted;
    }
    /**
     * returns whether a user has admin rights or not
     *
     * @return srole of the user
     */
    public String getSrole() {
        return srole;
    }
    /**
     * sets the role of a user to a new role
     *
     * @param srole, new value of user-field: srole
     */
    public void setSrole(String srole) {
        this.srole = srole;
    }



}