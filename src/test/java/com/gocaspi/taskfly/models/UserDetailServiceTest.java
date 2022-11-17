package com.gocaspi.taskfly.models;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class UserDetailServiceTest {
    UserDetailsServiceImpl mockUser = new UserDetailsServiceImpl();

    class Testcase_setString{
        final String newText;

        Testcase_setString(String newText) {
            this.newText = newText;
        }
    }
    UserDetailServiceTest.Testcase_setString[] testcases = new UserDetailServiceTest.Testcase_setString[]{
            new UserDetailServiceTest.Testcase_setString("abc"),
            new UserDetailServiceTest.Testcase_setString(null),
            new UserDetailServiceTest.Testcase_setString(""),
    };
    class Testcase_getString{
        final String expected;

        Testcase_getString(String expected) {
            this.expected = expected;
        }
    }
    UserDetailServiceTest.Testcase_getString[] testcases_get =new UserDetailServiceTest.Testcase_getString[]{
            new UserDetailServiceTest.Testcase_getString("abc"),
            new UserDetailServiceTest.Testcase_getString(null),
            new UserDetailServiceTest.Testcase_getString(""),
    };
   /* @Test
    void loadUserByUsername(){
        UserDetailsServiceImpl t = mockUser;
        for (UserDetailServiceTest.Testcase_getString tc : testcases_get){
            t.loadUserByUsername(tc.expected);
            String actual = t.loadUserByUsername();
            assertEquals(actual,t.loadUserByUsername(actual));
        }
    }*/

}
