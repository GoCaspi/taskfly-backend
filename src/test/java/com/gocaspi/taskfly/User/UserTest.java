package com.gocaspi.taskfly.User;

import junit.framework.TestCase;
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
    ObjectId mockObject_Id = new ObjectId();
    User mockUser = new User(mockUserIds, mockListId, mockFistName, mockTeam, mockLastName, mockEmail, mockPassword, mockObject_Id);
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
    public void getUserIdString(){
        User t = mockUser;
        for (Testcase_getString tc : testcases_get){
            t.setTopic(tc.expected);
            String actual = t.getUserIdString();
            assertEquals(actual,t.getUserIdString());
        }
    }
    @Test
    public void setTopic() {
        User t = mockUser;
        for (Testcase_setString tc : testcases){
            t.setTopic(tc.newText);
            assertEquals(t.getTopic(),tc.newText);
        }
    }
}
