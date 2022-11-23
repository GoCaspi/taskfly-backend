package com.gocaspi.taskfly.user;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertEquals;

public class UserTest {
    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    User mockUser = new User(mockUserIds, mockListId, mockFistName, mockTeam, mockLastName, mockEmail, mockPassword);
    class Testcase_setString{
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
    class Testcase_getString{
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
    public void getUserId(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setUserId(tc.expected);
            String actual = t.getUserId();
            assertEquals(actual,t.getUserId());
        }
    }
    @Test
    public void setUserId(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setUserId(tc.expected);
            String actual = t.getUserId();
            assertEquals(actual,t.getUserId());
        }
    }
    @Test
    public void getListId(){
        User t = mockUser;
        String oId = new String("");
        t.setListId(oId);
        String actual = t.getListId();
        assertEquals(oId,actual);
    }
    @Test
    public void setListId(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setListId(tc.newText);
            Assertions.assertEquals(user.getListId(),tc.newText);
        }
    }
    @Test
    public void getFirstName(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setFirstName(tc.expected);
            String actual = user.getFirstName();
            Assertions.assertEquals(actual,tc.expected);
        }
    }

    @Test
    public void setFirstName(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setFirstName(tc.newText);
            Assertions.assertEquals(user.getFirstName(),tc.newText);
        }
    }

    @Test
    public void getTeam(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setTeam(tc.expected);
            String actual = user.getTeam();
            Assertions.assertEquals(actual,tc.expected);
        }
    }

    @Test
    public void setTeam(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setTeam(tc.newText);
            Assertions.assertEquals(user.getTeam(),tc.newText);
        }
    }
    @Test
    public void getLastName(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setLastName(tc.expected);
            String actual = user.getLastName();
            Assertions.assertEquals(actual,tc.expected);
        }
    }
    @Test
    public void setLastName(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setLastName(tc.newText);
            Assertions.assertEquals(user.getLastName(),tc.newText);
        }
    }

    @Test
    public void getEmail(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setEmail(tc.expected);
            String actual = user.getEmail();
            Assertions.assertEquals(actual,tc.expected);
        }
    }
    @Test
    public void setEmail(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setEmail(tc.newText);
            Assertions.assertEquals(user.getEmail(),tc.newText);
        }
    }

    @Test
    public void getPassword(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setPassword(tc.expected);
            String actual = user.getPassword();
            Assertions.assertEquals(actual,tc.expected);
        }
    }
    @Test
    public void setPassword(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setPassword(tc.newText);
            Assertions.assertEquals(user.getPassword(),tc.newText);
        }
    }

    @Test
    public void getId(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setId(tc.expected);
            String actual = user.getId();
            Assertions.assertEquals(actual,tc.expected);
        }
    }
    @Test
    public void setId(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setId(tc.newText);
            Assertions.assertEquals(user.getId(),tc.newText);
        }
    }

}
