package com.gocaspi.taskfly.user;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    String mockFistName = "topic1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    String mocksrole ="ADMIN";

     User.Userbody mockbody =new User.Userbody("mockTeam");
     User mockUser = new User(mocksrole, mockFistName, mockLastName, mockEmail, mockPassword,mockbody,true);
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
     void getId(){
        User t = mockUser;

        String oId = new String("");
        t.setId(oId);
        String actual = t.getId();
        assertEquals(oId,actual);
    }
  @Test
    void getpassword(){
        User t = mockUser;
        for (TestcasegetString tc : testcases_get){
            t.setPassword(tc.expected);
            String actual = t.getPassword();
            assertEquals(actual,tc.expected);
        }
  }

    @Test
    void getemail(){
        User t = mockUser;
        for (TestcasegetString tc : testcases_get){
            t.setEmail(tc.expected);
            String actual = t.getEmail();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getfirstname(){
        User t = mockUser;
        for (TestcasegetString tc : testcases_get){
            t.setFirstName(tc.expected);
            String actual = t.getFirstName();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getlastname(){
        User t = mockUser;
        for (TestcasegetString tc : testcases_get){
            t.setLastName(tc.expected);
            String actual = t.getLastName();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getteam(){
        User.Userbody t = mockbody;
        for (TestcasegetString tc : testcases_get){
            t.setTeam(tc.expected);
            String actual = t.getTeam();
            assertEquals(actual,tc.expected);
        }
    }
    @Test
    void getid(){
        User t = mockUser;
        for (TestcasegetString tc : testcases_get){
            t.setId(tc.expected);
            String actual = t.getId();
            assertEquals(actual,tc.expected);
        }
    }

    @Test
    void setteam(){
        User.Userbody t = mockbody;
        for (TestcasesetString tc : testcases){
            t.setTeam(tc.newText);
            assertEquals(t.getTeam(),tc.newText);
        }
    }
    @Test
    void setfirstname(){
        User t = mockUser;
        for (TestcasesetString tc : testcases){
            t.setFirstName(tc.newText);
            assertEquals(t.getFirstName(),tc.newText);
        }
    }
    @Test
    void setlastname(){
        User t = mockUser;
        for (TestcasesetString tc : testcases){
            t.setLastName(tc.newText);
            assertEquals(t.getLastName(),tc.newText);
        }
    }
    @Test
    void setid(){
        User t = mockUser;
        for (TestcasesetString tc : testcases){
            t.setId(tc.newText);
            assertEquals(t.getId(),tc.newText);
        }
    }
    @Test
    void setEmail(){
        User t = mockUser;
        for (TestcasesetString tc : testcases){
            t.setEmail(tc.newText);
            assertEquals(t.getEmail(),tc.newText);
        }
    }
    @Test
    void setsrole(){
        User t = mockUser;
        for (TestcasesetString tc : testcases){
            t.setSrole(tc.newText);
            assertEquals(t.getSrole(),tc.newText);
        }
    }
    @Test
    void setpassword(){
        User t = mockUser;
        for (TestcasesetString tc : testcases){
            t.setPassword(tc.newText);
            assertEquals(t.getPassword(),tc.newText);
        }
    }
     @Test
     void setBody() {
         User t = mockUser;
         User.Userbody mockbody =  new User.Userbody("mockTeam");
         t.setBody(mockbody);
         User.Userbody actual = t.getBody();
         assertEquals(actual,mockbody);
     }

}


