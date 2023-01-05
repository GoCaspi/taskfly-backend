package com.gocaspi.taskfly.teammanagement;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TeamManagementControllerTest {
    TeamManagementRepository mockRepository = mock(TeamManagementRepository.class);
    TeamManagementService mockService = mock(TeamManagementService.class);

    String mockId;
    String mockUserId;
    String mockTeamName;
    String[] mockMember;
    TeamManagementController.TeamRequest mockTeamRequest = new TeamManagementController.TeamRequest(mockUserId, mockTeamName, mockMember, mockId);

    TeamManagement mockTeamManagement = new TeamManagement(mockUserId, mockTeamName, mockMember, mockId);

    @Test
    void updateTeam() {
        TeamManagementController t = new TeamManagementController(mockRepository,mockService);
        TeamManagement mockUpdate = new TeamManagement(mockUserId, mockTeamName, mockMember, mockId);

        class Testcase {
            final String mockId;
            final boolean idFoundInDb;
            final TeamManagement teamFromDb;
            final TeamManagement updateForTeam;
            final boolean expectSuccess;

            public Testcase(String mockId, boolean idFoundInDb, TeamManagement teamFromDb, TeamManagement updateForTeam, boolean expectSuccess) {
                this.mockId = mockId;
                this.idFoundInDb = idFoundInDb;
                this.teamFromDb = teamFromDb;
                this.updateForTeam = updateForTeam;
                this.expectSuccess = expectSuccess;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("123", true, mockTeamManagement, mockUpdate, true),
                new Testcase("123", false, mockTeamManagement, mockUpdate, false),
                new Testcase(null, false, mockTeamManagement, mockUpdate, false)
        };

        for (Testcase tc : testcases) {
            when(mockRepository.existsById(tc.mockId)).thenReturn(tc.idFoundInDb);
            if (tc.idFoundInDb) {
                when(mockRepository.findById(tc.mockId)).thenReturn(Optional.ofNullable(tc.teamFromDb));
            } else {
                when(mockRepository.findById(tc.mockId)).thenReturn(null);
            }

            if (tc.expectSuccess) {
                ResponseEntity<String> expected = new ResponseEntity<>("successfully updated Team", HttpStatus.ACCEPTED);
                try {
                    ResponseEntity<String> actual = t.updateTeam(tc.mockId,mockTeamRequest);
                    assertEquals(expected, actual);
                } catch (HttpClientErrorException e) {
                    throw new RuntimeException(e);
                }

            } else {
                try {
                    t.updateTeam(tc.mockId, mockTeamRequest);
                } catch (HttpClientErrorException e) {
                    HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                    assertEquals(e.getClass(), expectedException.getClass());
                }
            }
        }
    }

    @Test
    void createTeam() {
        TeamManagementController t = new TeamManagementController(mockRepository, mockService);

        class Testcase {
            final String mockId;
            final boolean badPayload;
            final TeamManagement mockTeam;
            final String mockPayload;

            public Testcase(String mockId, boolean badPayload, TeamManagement mockTeam, String mockPayload) {
                this.mockId = mockId;
                this.badPayload = badPayload;
                this.mockTeam = mockTeam;
                this.mockPayload = mockPayload;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", false, mockTeamManagement, new Gson().toJson(mockTeamManagement)),
                new Testcase("1", true, mockTeamManagement, new Gson().toJson(mockTeamManagement))
        };
        for (Testcase tc : testcases) {
            if (tc.badPayload) {
                when(mockService.validateTeamManagementFields(tc.mockPayload)).thenReturn(false);
            } else {
                when(mockService.validateTeamManagementFields(tc.mockPayload)).thenReturn(true);
                when(mockRepository.insert(tc.mockTeam)).thenReturn(mockTeamManagement);
            }

            try {
                ResponseEntity<String> expected = new ResponseEntity<>(HttpStatus.ACCEPTED);
                ResponseEntity<String> actual1 = t.createTeam(mockTeamRequest);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

    @Test
    void deleteTea() {
        TeamManagementController t = new TeamManagementController(mockRepository, mockService);

        class Testcase {
            final String mockId;
            final boolean dbReturnSize0;
            final TeamManagement mockTeam;
            final String expectedOutput;

            public Testcase(String mockId, boolean dbReturnSize0, TeamManagement mockTeam, String expectedOutput) {
                this.mockId = mockId;
                this.dbReturnSize0 = dbReturnSize0;
                this.mockTeam = mockTeam;
                this.expectedOutput = expectedOutput;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("1", false, mockTeamManagement, ""),
                new Testcase("1", true, mockTeamManagement, "no teams were found to the provided id"),
                new Testcase(null, true, mockTeamManagement, "no teams were found to the provided id"),
                new Testcase("", true, mockTeamManagement, "no teams were found to the provided id"),
                new Testcase(null, false, mockTeamManagement, "no teams were found to the provided id")
        };

        for (Testcase tc : testcases) {
            if (tc.dbReturnSize0) {
                when(mockRepository.existsById(tc.mockId)).thenReturn(false);
            } else {
                when(mockRepository.existsById(tc.mockId)).thenReturn(true);
            }

            try {
                ResponseEntity<String> expected = new ResponseEntity<>("successfully deleted team with id: " + tc.mockId, HttpStatus.ACCEPTED);
                ResponseEntity<String> actual1 = t.deleteTeam(tc.mockId);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

    @Test
    void deleteTeamMember() {
        TeamManagementController t = new TeamManagementController(mockRepository, mockService);

        String[] mockMembers = {"1", "2"};
        TeamManagement team = new TeamManagement("1", "1", mockMembers, "1");
        class Testcase {

            final boolean idFoundInDb;
            final String mockId;
            final String mockMember;
            final String expectedOutput;

            final String[] mockMemberss;
            final TeamManagement team;

            public Testcase(boolean idFoundInDb, String mockId, String mockMember, String expectedOutput, TeamManagement team, String[]mockMemberss) {
                this.idFoundInDb = idFoundInDb;
                this.mockId = mockId;
                this.mockMember = mockMember;
                this.expectedOutput = expectedOutput;
                this.team = team;
                this.mockMemberss = mockMemberss;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(false, "1", "1","", team, mockMembers),
                new Testcase(true,"1", "1","no teams were found to the provided id", team, mockMembers),
                 new Testcase(false, null, "1","no teams were found to the provided id", team, mockMembers),
                 new Testcase(true,null, "1","no teams were found to the provided id", team, mockMembers),
                 new Testcase(false,"", "1", "no teams were found to the provided id", team, mockMembers),
                 new Testcase(true, "", "1", "no teams were found to the provided id", team, mockMembers),

                 new Testcase(false, "1", "1","", team, mockMembers),
                 new Testcase(true, "1", "1","no member were found to the provided id", team, mockMembers),
                 new Testcase(false, "1", null,"no member were found to the provided id", team, mockMembers),
                 new Testcase(true,"1", null,"no member were found to the provided id", team, mockMembers),
                 new Testcase(false,"1", "","no member were found to the provided id", team, mockMembers),
                 new Testcase(true, "1", "","no member were found to the provided id", team, mockMembers)
        };

        for (Testcase tc : testcases) {
            when(mockService.getTeamById(tc.mockId)).thenReturn(team);
            //when(mockRepository.existsById(tc.mockId)).thenReturn(tc.idFoundInDb);
            //when(mockService.deleteMemberService(tc.mockMember)).thenReturn();

            try {
                ResponseEntity<String> expected = new ResponseEntity<>("successfully delete Member from Team" , HttpStatus.ACCEPTED);
                ResponseEntity<String> actual1 = t.deleteTeamMember(tc.mockId, tc.mockMember);
                //verify(mockService, times(1)).deleteMemberService(tc.mockId, mockMembers, tc.team, tc.mockMember);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

    @Test
    void addTeamMember() {
        TeamManagementController t = new TeamManagementController(mockRepository, mockService);

        String[] mockMembers = {"1", "2"};

        TeamManagement team = new TeamManagement("1", "1", mockMembers, "1");

        class Testcase{
            final boolean idFoundInDb;
            final String mockId;
            final String mockBody;
            final String expectedOutput;
            final TeamManagement team;


            public Testcase(boolean idFoundInDb, String mockId, String mockMember, String expectedOutput, TeamManagement team) {
                this.idFoundInDb = idFoundInDb;
                this.mockId = mockId;
                this.mockBody = mockMember;
                this.expectedOutput = expectedOutput;
                this.team = team;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase(false, "1", "1","", team),
                new Testcase(true,"1", "1","no teams were found to the provided id", team),
                new Testcase(false, null, "1","no teams were found to the provided id", team),
                new Testcase(true,null, "1","no teams were found to the provided id", team),
                new Testcase(false,"", "1", "no teams were found to the provided id", team),
                new Testcase(true, "", "1", "no teams were found to the provided id", team),

                new Testcase(false, "1", "1","", team),
                new Testcase(true, "1", "1","no member were found to the provided id", team),
                new Testcase(false, "1", null,"no member were found to the provided id", team),
                new Testcase(true,"1", null,"no member were found to the provided id", team),
                new Testcase(false,"1", "","no member were found to the provided id", team),
                new Testcase(true, "1", "","no member were found to the provided id", team),
        };

        for (Testcase tc : testcases) {
            when(mockService.getTeamById(tc.mockId)).thenReturn(tc.team);

            try {
                ResponseEntity<String> expected = new ResponseEntity<>("successfully created new Member in Team" , HttpStatus.ACCEPTED);
                ResponseEntity<String> actual1 = t.addTeamMember(tc.mockId, tc.mockBody);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }


    }
}

