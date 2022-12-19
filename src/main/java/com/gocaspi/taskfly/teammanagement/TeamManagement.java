package com.gocaspi.taskfly.teammanagement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Class for TeamManagement
 */
@Document
public class TeamManagement{
@NotBlank
    private String teamName;
@NotNull(message = "Please enter your Team Members")
private String[] members;
@NotBlank
private String userID;
    @Id
    private String id;
    /**
     * Constractor for TeamManagement
     *
     * @param userID userID of the teamManagement
     * @param teamName teamName of the teamManagement
     * @param members members of the teamManagement
     * @param id id of the teamManagement
     */
    public  TeamManagement(String userID, String teamName, String[] members, String id) {
        this.userID = userID;
        this.teamName = teamName;
        this.members = members;
        this.id = id;
    }
    /**
     * Empty Constractor TeamManagement for testing
     */
    public TeamManagement(){}
    /**
     * sets the userID of a Team to a new userID
     *
     * @param userID, new value of team-field: userID
     */
    public void setUserID(String userID){
        this.userID = userID;
    }
    /**
     * sets the teamName of a Team to a new teamName
     *
     * @param teamName, new value of team-field: teamName
     */
    public void setTeamName(String teamName){
        this.teamName = teamName;
    }
    /**
     * sets the members of a Team to a new members
     *
     * @param members, new value of team-field: members
     */
    public void setMembers(String[] members) { this.members = members; }
    /**
     * sets the id of a Team to an id
     *
     * @param id, new value of team-field: id
     */
    public void setId(String id) { this.id = id; }
    /**
     * returns the UserID of the team
     *
     * @return String, UserID of the team
     */
    public String getUserID(){
        return this.userID;
    }
    /**
     * returns the team Name of the team
     *
     * @return String, teamName of the team
     */
    public String getTeamName(){
        return this.teamName;
    }
    /**
     * returns an array of members that are assigned to that team
     *
     * @return String[], members that are assigned to that team
     */
    public String[] getMembers(){
        return this.members;
    }
    /**
     * returns the UserID of the team
     *
     * @return String, UserID of the team
     */
    public String getId(){return id;}

}
