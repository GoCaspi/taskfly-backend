package com.gocaspi.taskfly.reset;

public class ResetNewPassword {
    private String pwd;
    private String token;

    /**
     * Constructor for UIdAndPwdBody
     *
     * @param pwd String, password input
     * @param token String, userId input
     */
    public ResetNewPassword(String pwd, String token){
        this.token = token;
        this.pwd = pwd;
    }

    /**
     * Sets the field pwd of the UIdAndPwdBody class to the input value
     *
     * @param pwd String, password input
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Returns the value of the field pwd of the UIdAndPwdBody class
     *
     * @return password String
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Sets the field userId of the UIdAndPwdBody class to the input value
     *
     * @param userId String
     */
    public void setToken(String userId) {
        this.token = userId;
    }

    /**
     * Returns the value of the field userId of the UIdAndPwdBody class
     *
     * @return
     */
    public String getToken() {
        return token;
    }
}
