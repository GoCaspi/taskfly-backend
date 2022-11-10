package com.gocaspi.taskfly.teammanagement;

public class TeamManagement {

    private String teamName;
    private String[] members;
    private String userID;
    private String ID;

    public TeamManagement(String userID, String teamName, String[] members, String ID){
        this.userID = userID;
        this.teamName = teamName;
        this.members = members;
        this.ID = ID;
    }
    public void setUserID(String str){
        this.userID = str;
    }
    public void setTeamName(String str){
        this.teamName = str;
    }
    public void setMembers(String[] str) { this.members = str; }
    public void setID(String str) { ID = str; }

    public String getUserID(){
        return this.userID;
    }
    public String getTeamName(){
        return this.teamName;
    }
    public String[] getMembers(){
        return this.members;
    }
    public String getID(){return ID;}

}
