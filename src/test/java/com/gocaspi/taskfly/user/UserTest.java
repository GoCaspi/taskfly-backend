package com.gocaspi.taskfly.user;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserTest {
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
    void getUserId(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setUserId(tc.expected);
            String actual = user.getUserId();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void setUserId(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setUserId(tc.newText);
            assertEquals(user.getUserId(),tc.newText);
        }
    }
    @Test
    void getListId(){
        User t = mockUser;
        String oId = new String("");
        t.setListId(oId);
        String actual = t.getListId();
        assertEquals(oId,actual);
    }
    @Test
    void setListId(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setListId(tc.newText);
            assertEquals(user.getListId(),tc.newText);
        }
    }
    @Test
    void getFirstName(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setFirstName(tc.expected);
            String actual = user.getFirstName();
            assertEquals(actual,tc.expected);
        }
    }

    @Test
    void setFirstName(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setFirstName(tc.newText);
            assertEquals(user.getFirstName(),tc.newText);
        }
    }

    @Test
    void getTeam(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setTeam(tc.expected);
            String actual = user.getTeam();
            assertEquals(actual,tc.expected);
        }
    }

    @Test
    void setTeam(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setTeam(tc.newText);
            assertEquals(user.getTeam(),tc.newText);
        }
    }
    @Test
    void getLastName(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setLastName(tc.expected);
            String actual = user.getLastName();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void setLastName(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setLastName(tc.newText);
            assertEquals(user.getLastName(),tc.newText);
        }
    }

    @Test
    void getEmail(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setEmail(tc.expected);
            String actual = user.getEmail();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void setEmail(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setEmail(tc.newText);
            assertEquals(user.getEmail(),tc.newText);
        }
    }

    @Test
    void getPassword(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setPassword(tc.expected);
            String actual = user.getPassword();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void setPassword(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setPassword(tc.newText);
            assertEquals(user.getPassword(),tc.newText);
        }
    }

    @Test
    void getId(){
        User user = mockUser;
        for (UserTest.Testcase_getString tc : testcases_get){
            user.setId(tc.expected);
            String actual = user.getId();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void setId(){
        User user = mockUser;
        for (UserTest.Testcase_setString tc : testcases){
            user.setId(tc.newText);
            assertEquals(user.getId(),tc.newText);
        }
    }

}
