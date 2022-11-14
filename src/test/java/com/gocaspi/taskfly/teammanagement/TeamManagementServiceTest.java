package com.gocaspi.taskfly.teammanagement;


import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TeamManagementServiceTest {
    TeamManagementRepository mockrepository = mock(TeamManagementRepository.class);

    String mockUserID = "1";
    String mockTeamName = "gelb";

    String mockID = "1";
    String[] mockMembers = {"1", "3", "7"};

    TeamManagement mockTeamManagement= new TeamManagement(mockUserID, mockTeamName, mockMembers, mockID);

    @Test
    void validateTaskFields() {
        TeamManagementService team = new TeamManagementService(mockrepository);

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
    }

}