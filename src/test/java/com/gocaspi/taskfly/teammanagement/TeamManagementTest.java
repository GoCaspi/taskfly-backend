package com.gocaspi.taskfly.teammanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TeamManagementTest {
    String mockUserID = "1";
    String mockTeamName = "gelb";
    String mockID ="test";
    String[] mockMembers = {"1", "3", "7"};

    TeamManagement mockTeamManagement= new TeamManagement(mockUserID, mockTeamName, mockMembers, mockID);
    static class Testcase_setString {
        final String newText;

        Testcase_setString(String newText) {
            this.newText = newText;
        }
    }
    TeamManagementTest.Testcase_setString[] testcases = new TeamManagementTest.Testcase_setString[]{
            new Testcase_setString("abc"),
            new Testcase_setString(null),
            new Testcase_setString(""),
    };

    static class Testcase_getString {
        final String expected;
        Testcase_getString(String expected) {
            this.expected = expected;
        }
    }

    TeamManagementTest.Testcase_getString[] testcases_get = new TeamManagementTest.Testcase_getString[]{
            new Testcase_getString("abc"),
            new Testcase_getString(null),
            new Testcase_getString(""),
    };
    @Test
    void setUserID(){
        TeamManagement team = mockTeamManagement;
        for (Testcase_setString tc : testcases){
            team.setUserID(tc.newText);
            assertEquals(team.getUserID(),tc.newText);
        }
    }


    @Test
    void setTeamName(){
        TeamManagement team = mockTeamManagement;
        for (Testcase_setString tc : testcases){
            team.setTeamName(tc.newText);
            assertEquals(team.getTeamName(),tc.newText);
        }
    }

    @Test
    void setId(){
        TeamManagement team = mockTeamManagement;
        for (Testcase_setString tc : testcases){
            team.setId(tc.newText);
            assertEquals(team.getId(),tc.newText);
        }
    }

    @Test
    void getUserID(){
        TeamManagement team = mockTeamManagement;
        for (Testcase_getString tc : testcases_get){
            team.setUserID(tc.expected);
            String actual = team.getUserID();
            assertEquals(actual,tc.expected);
        }
    }

    @Test
    void getTeamName(){
        TeamManagement team = mockTeamManagement;
        for (Testcase_getString tc : testcases_get){
            team.setTeamName(tc.expected);
            String actual = team.getTeamName();
            assertEquals(actual,tc.expected);
        }
    }

    @Test
    void getId(){
        TeamManagement team = mockTeamManagement;
        for (Testcase_getString tc : testcases_get){
            team.setId(tc.expected);
            String actual = team.getId();
            assertEquals(actual,tc.expected);
        }
    }

    /*@Test
    void setMembers(){
        TeamManagement team = mockTeamManagement;
        for (Testcase_setString tc : testcases){
            team.setMembers(tc.newText);
            assertEquals(team.getMembers(),tc.newText);
        }
    }*/



}

