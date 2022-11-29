package com.gocaspi.taskfly.reset;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

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

    public String hashStr(String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();

    }



}
