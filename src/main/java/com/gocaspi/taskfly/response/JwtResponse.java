package com.gocaspi.taskfly.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type ="Bearer";
    private String id;
    private String username;
    private String email;
    private List<String> srole;
    public JwtResponse(String accessToken, String id, String username, String email, List<String> srole) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.srole = srole;
    }
    public String getAccessToken(){
        return token;
    }
    public void setAccessToken(String accessToken){
        this.token = accessToken;
    }
    public String getTokenType(){
        return type;
    }
    public void setTokenType(String tokenType){
        this.type=tokenType;
    }
    public String getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }


}
