package com.gocaspi.taskfly.teammanagement;

import com.gocaspi.taskfly.task.Task;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeamManagementControllerTest {
    TeamManagementRepository mockRepository = mock(TeamManagementRepository.class);
    TeamManagementService mockService = mock(TeamManagementService.class);

    String mockId;
    String mockUserId;
    String mockTeamName;
    String[] mockMember;

    TeamManagement mockTeamManagement = new TeamManagement(mockUserId, mockTeamName, mockMember, mockId);

    @Test
    void updateTeam() {
        TeamManagementController t = new TeamManagementController(mockRepository);
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
                ResponseEntity<String> expected = new ResponseEntity<>("successfully updated task with id: " + tc.mockId, HttpStatus.ACCEPTED);
                try {
                    ResponseEntity<String> actual = t.updateTeam(tc.mockId, new Gson().toJson(tc.updateForTeam));
                    assertEquals(expected, actual);
                } catch (HttpClientErrorException e) {
                    throw new RuntimeException(e);
                }

            } else {
                try {
                    t.updateTeam(tc.mockId, new Gson().toJson(tc.updateForTeam));
                } catch (HttpClientErrorException e) {
                    HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                    assertEquals(e.getClass(), expectedException.getClass());
                }
            }
        }
    }

    @Test
    void createTeam() {
        TeamManagementController t = new TeamManagementController(mockRepository);

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
                ResponseEntity<String> expected = new ResponseEntity<>("successfully created Team" , HttpStatus.ACCEPTED);
                ResponseEntity<String> actual1 = t.createTeam(tc.mockPayload);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

    @Test
    void deleteTea() {
        TeamManagementController t = new TeamManagementController(mockRepository);

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
}

