package com.gocaspi.taskfly.user;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

 class UserTest {
    String mockFistName = "müller";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";

    ObjectId mockObject_Id = new ObjectId();
      User mockUser = new User(mockLastName,mockEmail,mockPassword,mockFistName,mockTeam);
    static class Testcase_setString{
        final String newText;

        Testcase_setString(String newText) {
            this.newText = newText;
        }
    }
    Testcase_setString[] testcases = new Testcase_setString[]{
            new Testcase_setString("abc"),
            new Testcase_setString(null),
            new Testcase_setString(""),
    };
    static class Testcase_getString{
        final String expected;

        Testcase_getString(String expected) {
            this.expected = expected;
        }
    }
    Testcase_getString[] testcases_get = new Testcase_getString[]{
            new Testcase_getString("abc"),
            new Testcase_getString(null),
            new Testcase_getString(""),
    };


    @Test
     void setUserId(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setUserId(tc.newText);
            assertEquals(t.getUserId(),tc.newText);
        }
    }
    @Test
    void setUsername(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setUsername(tc.newText);
            assertEquals(t.getUsername(),tc.newText);
        }
    }
    @Test
    void setfirtname(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setFirstName(tc.newText);
            assertEquals(t.getFirstName(),tc.newText);
        }
    }
    @Test
    void setLastname(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setLastName(tc.newText);
            assertEquals(t.getLastName(),tc.newText);
        }
    }
    @Test
    void setemail(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setEmail(tc.newText);
            assertEquals(t.getEmail(),tc.newText);
        }
    }
    @Test
    void setlistid(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setListId(tc.newText);
            assertEquals(t.getListId(),tc.newText);
        }
    }
    @Test
    void setteam(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setTeam(tc.newText);
            assertEquals(t.getTeam(),tc.newText);
        }
    }
    @Test
    void setid(){
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setId(tc.newText);
            assertEquals(t.getId(),tc.newText);
        }
    }
    @Test
     void getId(){
        User t = mockUser;

        String oId = new String("");
        t.setId(oId);
        String actual = t.getId();
        assertEquals(oId,actual);
    }
    @Test
    void getUserId(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setUserId(tc.expected);
            String actual = t.getUserId();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getfirstname(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setFirstName(tc.expected);
            String actual = t.getFirstName();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getlastname(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setLastName(tc.expected);
            String actual = t.getLastName();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getemail(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setEmail(tc.expected);
            String actual = t.getEmail();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getpassword(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setPassword(tc.expected);
            String actual = t.getPassword();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getteam(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setTeam(tc.expected);
            String actual = t.getTeam();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getlsitid(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setListId(tc.expected);
            String actual = t.getListId();
            assertEquals(actual,tc.expected);
        }
    }
}
