package com.gocaspi.taskfly.teammanagement;


import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TeamManagementServiceTest {
    TeamManagementRepository mockRepository = mock(TeamManagementRepository.class);

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
                new Testcase("", false),
                new Testcase(null,true),
                new Testcase("", false)
        };

        for (Testcase tc : testcases) {
            when(service.getTeamById(tc.mockId)).thenReturn(team);

        }
    }
}
