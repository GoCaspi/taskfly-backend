package com.gocaspi.taskfly.models;

import com.gocaspi.taskfly.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class UserDetailTest {
    String mockid = "123";
    String mockEmail = "desc1";
    String mockUsername = "topic1";
    String mockPassword = "team1";
    String mockSrole = "prio1";

    User mockUser = new User(mockUsername,mockEmail,mockPassword,mockSrole);

    class Testcase_setString {
        final String newText;
        Testcase_setString (String newText){
            this.newText = newText ;
        }
    }
    Testcase_setString [] testcase = new Testcase_setString[]{
            new Testcase_setString("abc"),
            new Testcase_setString(null),
            new Testcase_setString(""),
    };
    class Testcase_getSting{
        final String expected ;

        Testcase_getSting(String expected){
            this.expected = expected;
        }
    }
    Testcase_getSting [] testcases_get = new Testcase_getSting[]{
            new Testcase_getSting(""),
            new Testcase_getSting(null),
            new Testcase_getSting(""),
    };

    @Test
     void getId(){
        User t = mockUser;
        for (Testcase_setString tc : testcase){
            t.setUserId(tc.newText);
            assertEquals(t.getUserId(),tc.newText);

        }
    }

}
