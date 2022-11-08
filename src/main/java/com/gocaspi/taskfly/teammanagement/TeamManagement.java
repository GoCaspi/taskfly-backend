package com.gocaspi.taskfly.teammanagement;

import org.bson.types.ObjectId;

public class TeamManagement {

    private String userID;

    private String teamID;

    private String teamName;

    private ObjectId[] objectId;

    public TeamManagement(String userID, String teamID, String teamName, ObjectId[] objectId){
        this.userID = userID;
        this.teamID = teamID;
        this.teamName = teamName;
        this.objectId = objectId;
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
    public void setObjectId(ObjectId[] str) { this.objectId = str; }

    public String getUserID(){
        return this.userID;
    }
    public String getTeamID(){
        return this.teamID;
    }
    public String getTeamName(){
        return this.teamName;
    }
    public ObjectId[] getObjectId(){
        return this.objectId;
    }

}
