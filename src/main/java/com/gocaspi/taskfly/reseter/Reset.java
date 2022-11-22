package com.gocaspi.taskfly.reseter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Reset {

    private String userId;
    private String password;
    private String email;

    public Reset(String userId,String password, String email){
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public byte[] hashStr(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(
                str.getBytes(StandardCharsets.UTF_8));
    }
}
