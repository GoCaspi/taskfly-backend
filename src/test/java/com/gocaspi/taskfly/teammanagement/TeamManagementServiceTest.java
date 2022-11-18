package com.gocaspi.taskfly.teammanagement;


import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TeamManagementServiceTest {
    TeamManagementRepository mockRepository = mock(TeamManagementRepository.class);

    TeamManagementService mockService = mock(TeamManagementService.class);


    String mockUserID = "1";
    String mockTeamName = "gelb";
    String mockID = "1";
    String[] mockMembers = {"1", "3", "7"};

    TeamManagement mockTeamManagement= new TeamManagement(mockUserID, mockTeamName, mockMembers, mockID);

    @Test
    void validateTaskFields() {
        TeamManagementService service = new TeamManagementService(mockRepository);

        class Testcase {
            final TeamManagement teamManagementInput;
            final boolean expected;

            public Testcase(TeamManagement testTeamManagement, boolean expected) {
                this.teamManagementInput = testTeamManagement;
                this.expected = expected;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTeamManagement, true),
                new Testcase(new TeamManagement(null,null,null,null),false),
                new Testcase(new TeamManagement("test", "test", mockMembers, "test"),true)
        };

        for (Testcase tc : testcases) {
            boolean actualOut = service.validateTeamManagementFields(new Gson().toJson(tc.teamManagementInput));
            assertEquals(tc.expected, actualOut);
        }
    }

    @Test
    void deleteTeam(){

        TeamManagementService service = new TeamManagementService(mockRepository);
        class Testcase{
            final String mockId;
            final boolean expected;

            public Testcase(String mockId, boolean expected){
                this.mockId = mockId;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", true),
                /*new Testcase("", false),
                new Testcase(null,true),
                new Testcase("", false)*/
        };

        for (Testcase tc : testcases) {
            service.deleteService(tc.mockId);
            verify(mockRepository, times(1)).deleteById(tc.mockId);
        }
    }

    @Test
    void insertTeam(){
        TeamManagementService service = new TeamManagementService(mockRepository);
        TeamManagement mockTeamTest = new TeamManagement();

        class Testcase{
            final TeamManagement mockInsert;
            final boolean expected;

            public Testcase(TeamManagement mockInsert, boolean expected){
                this.mockInsert = mockInsert;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTeamManagement, false),
                new Testcase(mockTeamTest, true)
        };

        for (Testcase tc : testcases) {
            try{
                service.insertService(tc.mockInsert);
                verify(mockRepository, times(1)).insert(tc.mockInsert);
            } catch (Exception e){

            }

        }
    }

    @Test
    void updateTeam(){
        TeamManagementService service = new TeamManagementService(mockRepository);
        TeamManagement mockTeamTest = new TeamManagement();
        class Testcase{
            final String mockId;
            final TeamManagement mockUpdate;
            final boolean expected;
            public Testcase(String mockId, TeamManagement mockUpdate, boolean expected){
                this.mockId = mockId;
                this.mockUpdate = mockUpdate;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1",mockTeamManagement, false),
                new Testcase("", mockTeamTest, true)
        };

        for (Testcase tc : testcases) {
            try{
                service.updateService(tc.mockId, tc.mockUpdate);
                verify(mockRepository, times(1)).findById(tc.mockId);
            } catch (Exception e){

            }

        }
    }

    @Test
    void addTeamMember(){
        TeamManagementService service = new TeamManagementService(mockRepository);
        class Testcase{
            final String mockId;
            final String[] mockMember;
            final String mockNewMember;
            final TeamManagement mockTeam;
            final boolean expected;

            public Testcase(String mockId, String[] mockMember, String mockNewMember, TeamManagement mockTeam, boolean expected){
                this.mockId = mockId;
                this.mockMember = mockMember;
                this.mockNewMember = mockNewMember;
                this.mockTeam = mockTeam;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", mockMembers, "1", mockTeamManagement, true),
                new Testcase("0", mockMembers, "1", mockTeamManagement, false),
                new Testcase("1", mockMembers, "0", mockTeamManagement, false),
        };

        for (Testcase tc : testcases) {
            try{
                service.addMemberService(tc.mockId, tc.mockMember, tc.mockNewMember, tc.mockTeam);
            } catch (Exception e){

            }

        }

    }

    @Test
    void deleteTeamMember(){
        TeamManagementService service = new TeamManagementService(mockRepository);
        class Testcase{
            final String mockId;
            final String[] mockMember;
            final String mockDeleteMember;
            final TeamManagement mockTeam;
            final boolean expected;

            public Testcase(String mockId, String[] mockMember, String mockDeleteMember, TeamManagement mockTeam, boolean expected){
                this.mockId = mockId;
                this.mockMember = mockMember;
                this.mockDeleteMember = mockDeleteMember;
                this.mockTeam = mockTeam;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", mockMembers, "1", mockTeamManagement, true),
                new Testcase("0", mockMembers, "1", mockTeamManagement, false),
                new Testcase("1", mockMembers, "0", mockTeamManagement, false),
        };

        for (Testcase tc : testcases) {
            try{
                service.deleteMemberService(tc.mockId, tc.mockMember, tc.mockTeam, tc.mockDeleteMember);
            } catch (Exception e){

            }

        }
    }
    /*@Test
    void getTeamById(){
        TeamManagementService mockService = new TeamManagementService(mockRepository);
        class Testcase{
            final String mockId;
            final boolean expected;
            public Testcase(String mockId, boolean expected){
                this.mockId = mockId;
                this.expected = expected;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("1",true),
                new Testcase("2", false )
        };

        for (Testcase tc : testcases) {

        }
    }*/
}
