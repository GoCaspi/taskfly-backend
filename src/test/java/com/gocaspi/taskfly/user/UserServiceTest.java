package com.gocaspi.taskfly.user;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

 class UserServiceTest {
    UserRepository mockRepo = mock(UserRepository.class);
    UserService mockService = mock(UserService.class);
    HttpClientErrorException er = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no User are assigned to the provided userId", null, null, null);
    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    String mockSrole ="";
    ObjectId mockObject_Id = new ObjectId();
    User mockUser = new User(mockUserIds, mockListId, mockFistName, mockTeam, mockLastName, mockEmail, mockPassword,mockSrole);
    UserService ts = new UserService(mockRepo);

    @Test
     void getService_AllUser() {

        UserService t = new UserService(mockRepo);
        User[] mockTaskArr = new User[]{mockUser, mockUser };
        ArrayList<User> mockList = new ArrayList<>();
        for (User task : mockTaskArr) { mockList.add(task); }

        class Testcase {
            final String id;
            final List<User> dbReturn;
            final List<User> expected;

            public	Testcase(String id, List<User> dbReturn, List<User> expected) {
                this.id = id;
                this.dbReturn = dbReturn;
                this.expected = expected;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("123", mockList,mockList),
                new Testcase("1",new ArrayList<User>(),new ArrayList<User>())
        };
        for(Testcase tc : testcases){
            when(mockRepo.findAll()).thenReturn(tc.dbReturn);
            List actual = t.getServiceAllUser();
            assertEquals(tc.expected, actual);
        }
    }

}
