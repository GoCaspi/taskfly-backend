package com.gocaspi.taskfly.teammanagement;

public class TeamManagement {

    private String userID;

    private String teamID;

    private String teamName;

    private String[] members;

    public TeamManagement(String userID, String teamID, String teamName, String[] members){
        this.userID = userID;
        this.teamID = teamID;
        this.teamName = teamName;
        this.members = members;
    }
    public void setUserID(String str){
        this.userID = str;
    }
    public void setTeamID(String str){
        this.teamID = str;
    }
    public void setTeamName(String str){
        this.teamName = str;
    }
    public void setMembers(String[] str) { this.members = str; }

    public String getUserID(){
        return this.userID;
    }
    public String getTeamID(){
        return this.teamID;
    }
    public String getTeamName(){
        return this.teamName;
    }
    public String[] getMembers(){
        return this.members;
    }

}
