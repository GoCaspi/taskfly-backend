package com.gocaspi.taskfly.user;



import org.springframework.data.annotation.Id;

/**
 * Class for User
 */
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
    /**
     * Constractor for User
     * @param firstName firstName for the user
     * @param lastName lastName for the user
     * @param email email for the user
     * @param password password for the user
     * @param team team for the user
     * @param listId listId for the user
     * @param userId userId for the user
     */

    private boolean reseted;



    public User(String firstName, String lastName, String email, String password, String team, String listId, String userId, Boolean reseted) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
        this.reseted = reseted;
    }

    public boolean getReseted(){
        return this.reseted;
    }
    public void setReseted(Boolean b){
        this.reseted = b;
    }
    /**
     * Empty Constractor Task for testing
     */
    public User(){}
    /**
     * returns the userId of the user
     *
     * @return String, userId of the user
     */
    public String getUserId(){return this.userId;}
    /**
     * returns the firstName of the user
     *
     * @return String, firstName of the user
     */
    public String getFirstName(){ return this.firstName;}
    /**
     * returns the lastName of the user
     *
     * @return String, lastName of the user
     */
    public String getLastName(){ return  this.lastName;}
    /**
     * returns the email of the user
     *
     * @return String, email of the user
     */
    public String getEmail(){ return this.email;}
    /**
     * returns the listId of the user
     *
     * @return String, listId of the user
     */
    public String getListId(){ return this.listId;}
    /**
     * returns the team of the user
     *
     * @return String, team of the user
     */
    public String getTeam(){ return this.team;}
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
     * sets the userId of a user to a new userId
     *
     * @param userId, new value of user-field: userID
     */
    public void setUserId(String userId){ this.userId = userId;}
    /**
     * sets the firstName of a user to a new firstName
     *
     * @param firstName, new value of user-field: firstName
     */
    public void setFirstName(String firstName){ this.firstName = firstName;}
    /**
     * sets the lastName of a user to a new lastName
     *
     * @param lastName, new value of user-field: lastName
     */
    public void setLastName(String lastName){ this.lastName = lastName;}
    /**
     * sets the email of a user to a new email
     *
     * @param email, new value of user-field: email
     */
    public void setEmail(String email){ this.email = email;}
    /**
     * sets the listId of a user to a new listId
     *
     * @param listId, new value of user-field: listId
     */
    public void setListId(String listId){ this.listId = listId;}
    /**
     * sets the team of a user to a new team
     *
     * @param team, new value of user-field: team
     */
    public void setTeam(String team){ this.team = team;}
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



}