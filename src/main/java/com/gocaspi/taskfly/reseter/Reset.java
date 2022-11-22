package com.gocaspi.taskfly.reseter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Reset {


    private String lastName;
    private String email;

    public Reset(String lastName, String email){
        this.email = email;
        this.lastName = lastName;
    }

    public String getLastName(){
        return this.lastName;
    }
    public String getEmail(){return this.email;}
    public void setLastName(String str){
        this.lastName = str;
    }
    public void setEmail(String str){
        this.email = str;
    }

    public String hashStr(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(
                str.getBytes(StandardCharsets.UTF_8)).toString();
    }
}
