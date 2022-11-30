package com.gocaspi.taskfly.reset;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Class for Reset
 */
public class Reset {


    private String lastName;
    private String email;

    /**
     * Constructor for the Reset class
     *
     * @param lastName String, lastName of the user who asks for the reset
     * @param email String, email of the user who asks for the reset
     */
    public Reset(String lastName, String email){
        this.email = email;
        this.lastName = lastName;
    }

    /**
     * Returns the lastName set to the Reset class
     *
     * @return lastName String
     */
    public String getLastName(){
        return this.lastName;
    }

    /**
     * Returns the email set to the Reset class
     *
     * @return email String
     */
    public String getEmail(){return this.email;}

    /**
     * Sets the lastName of the Reset to the input parameter value
     *
     * @param str String, lastName input
     */
    public void setLastName(String str){
        this.lastName = str;
    }

    /**
     * Sets the email of the Reset to the input parameter value
     *
     * @param str String
     */
    public void setEmail(String str){
        this.email = str;
    }

    /**
     * Hashes a string with the SHA256 algorithm
     *
     * @param str String that should get hashed by SHA256
     * @return hashed String of the input String
     */
    public String hashStr(String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();

    }



}
