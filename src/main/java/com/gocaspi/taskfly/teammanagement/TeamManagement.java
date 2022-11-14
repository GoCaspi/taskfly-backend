package com.gocaspi.taskfly.teammanagement;

import org.springframework.data.annotation.Id;

public class TeamManagement {

    private String teamName;
    private String[] members;
    private String userID;
    @Id
    private String id;

    public TeamManagement(String userID, String teamName, String[] members, String id){
        this.userID = userID;
        this.teamName = teamName;
        this.members = members;
        this.id = id;
    }
    public void setUserID(String str){
        this.userID = str;
    }
    public void setTeamName(String str){
        this.teamName = str;
    }
    public void setMembers(String[] str) { this.members = str; }
    public void setId(String str) { id = str; }

    public String getUserID(){
        return this.userID;
    }
    public String getTeamName(){
        return this.teamName;
    }
    public String[] getMembers(){
        return this.members;
    }
    public String getId(){return id;}

}
