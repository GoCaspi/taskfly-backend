package com.gocaspi.taskfly.user;

import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    String mocksrole ="ADMIN";
    User mockUser = new User(mockUserIds,mockFistName,mockLastName, mockEmail, mockPassword,mockTeam,mockListId,mocksrole);
    class TestcasesetString{
        final String newText;

        TestcasesetString(String newText) {
            this.newText = newText;
        }
    }
    TestcasesetString[] testcases = new TestcasesetString[]{
            new TestcasesetString("abc"),
            new TestcasesetString(null),
            new TestcasesetString(""),
    };
    class TestcasegetString{
        final String expected;

        TestcasegetString(String expected) {
            this.expected = expected;
        }
    }
    TestcasegetString[] testcases_get = new TestcasegetString[]{
            new TestcasegetString("abc"),
            new TestcasegetString(null),
            new TestcasegetString(""),
    };
    @Test
    public void getUserId(){
        User t = mockUser;
        for (TestcasegetString tc : testcases_get){
            t.setUserId(tc.expected);
            String actual = t.getUserId();
            assertEquals(actual,t.getUserId());
        }
    }

    @Test
    public void setUserId(){
        User t = mockUser;
        for (TestcasegetString tc : testcases_get){
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
