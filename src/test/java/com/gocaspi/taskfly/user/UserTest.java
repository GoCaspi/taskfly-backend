package com.gocaspi.taskfly.user;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    ObjectId mockObject_Id = new ObjectId();
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
    public void getId(){
        User t = mockUser;

        String oId = new String("");
        t.setId(oId);
        String actual = t.getId();
        assertEquals(oId,actual);
    }

}
