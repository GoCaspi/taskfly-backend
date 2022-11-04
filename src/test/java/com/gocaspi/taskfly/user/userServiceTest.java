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

public class userServiceTest {
    userRepository mockRepo = mock(userRepository.class);
    userService mockService = mock(userService.class);
    HttpClientErrorException er = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no User are assigned to the provided userId", null, null, null);
    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    ObjectId mockObject_Id = new ObjectId();
    user mockUser = new user(mockUserIds, mockListId, mockFistName, mockTeam, mockLastName, mockEmail, mockPassword, mockObject_Id);
    userService ts = new userService(mockRepo);
    @Test
    public void postService() throws HttpClientErrorException {
        userService t2 = mockService;
        user t = mockUser;
        ts.postService(t);

    }
    @Test
    public void getService_AllUser() {

        userService t = new userService(mockRepo);
        user[] mockTaskArr = new user[]{mockUser, mockUser };
        ArrayList<user> mockList = new ArrayList<>();
        for (user task : mockTaskArr) { mockList.add(task); }

        class Testcase {
            final String id;
            final List<user> dbReturn;
            final List<user> expected;

            public	Testcase(String id, List<user> dbReturn, List<user> expected) {
                this.id = id;
                this.dbReturn = dbReturn;
                this.expected = expected;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("123", mockList,mockList),
                new Testcase("1",new ArrayList<user>(),new ArrayList<user>())
        };
        for(Testcase tc : testcases){
            when(mockRepo.findAll()).thenReturn(tc.dbReturn);
            List actual = t.getServiceAllUser();
            assertEquals(tc.expected, actual);
        }
    }

}
