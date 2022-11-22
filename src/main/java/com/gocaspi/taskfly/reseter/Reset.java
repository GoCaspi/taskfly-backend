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

    public byte[] hashStr(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(
                str.getBytes(StandardCharsets.UTF_8));
    }
}
