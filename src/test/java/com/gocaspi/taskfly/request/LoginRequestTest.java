package com.gocaspi.taskfly.request;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class LoginRequestTest {


    LoginRequest mockUser = new LoginRequest();
    class Testcase_setString{
        final String newText;

        Testcase_setString(String newText) {
            this.newText = newText;
        }
    }
    LoginRequestTest.Testcase_setString[] testcases = new LoginRequestTest.Testcase_setString[]{
            new LoginRequestTest.Testcase_setString("abc"),
            new LoginRequestTest.Testcase_setString(null),
            new LoginRequestTest.Testcase_setString(""),
    };
    class Testcase_getString{
        final String expected;

        Testcase_getString(String expected) {
            this.expected = expected;
        }
    }
    LoginRequestTest.Testcase_getString[] testcases_get =new LoginRequestTest.Testcase_getString[]{
            new LoginRequestTest.Testcase_getString("abc"),
            new LoginRequestTest.Testcase_getString(null),
            new LoginRequestTest.Testcase_getString(""),
    };
    @Test
    void getUsername(){
        LoginRequest t =mockUser;
        for (LoginRequestTest.Testcase_getString tc : testcases_get){
            t.setUsername(tc.expected);
            String actual = t.getUsername();
            assertEquals(actual,t.getUsername());
        }

    }
    @Test
    void getPassword(){
        LoginRequest t = mockUser;
        for(LoginRequestTest.Testcase_getString tc : testcases_get){
            t.setPassword(tc.expected);
            String actual = t.getPassword();
            assertEquals(actual,t.getPassword());
        }
    }
}
