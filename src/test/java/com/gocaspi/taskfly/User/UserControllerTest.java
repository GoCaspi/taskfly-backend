package com.gocaspi.taskfly.User;


import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {
    UserRepository mockRepo = mock(UserRepository.class);
    UserService mockService = mock(UserService.class);
    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    ObjectId mockObject_Id = new ObjectId();
    User mockUser = new User(mockUserIds, mockListId, mockFistName, mockTeam, mockLastName, mockEmail, mockPassword, mockObject_Id);
    User[] mockUseArr = new User[]{mockUser,mockUser};
    @Test
    public void deleteUser() {


        UserController t = new UserController(mockRepo);

        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final User mockTask;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, User mockTask, String expectedOutput) {
                this.userId = userId;
                this.dbReturnSize0 = dbReturnSize0;
                this.mockTask = mockTask;
                this.expectedOutput = expectedOutput;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", false, mockUser, ""),
                new Testcase("1", true, mockUser, "no tasks were found to the provided id"),
                new Testcase(null, true, mockUser, "no tasks were found to the provided id"),
                new Testcase("", true, mockUser, "no tasks were found to the provided id"),
                new Testcase(null, false, mockUser, "no tasks were found to the provided id")
        };
        for (Testcase tc : testcases) {
            if (tc.dbReturnSize0) {
                when(mockRepo.existsById(tc.userId)).thenReturn(false);
            } else {
                when(mockRepo.existsById(tc.userId)).thenReturn(true);
                //	when(mockRepo.deleteById(tc.userId)).thenReturn(mockTask);
            }

            try {
                ResponseEntity<String> expected = new ResponseEntity<>("successfully deleted task with id: " + tc.userId, HttpStatus.ACCEPTED);
                ResponseEntity<String> actual1 = t.deleteUser(tc.userId);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }
    @Test
    public void updateUser() {
        UserController t = new UserController(mockRepo);
        User mockUpdate = new User(mockUserIds, mockListId, mockEmail + "updated", mockPassword + "updated", mockFistName, mockLastName + "updated",mockService+"updated",mockObject_Id);

        class Testcase {
            final String mockId;
            final boolean idFoundInDb;
            final User taskFromDb;
            final User updateForTask;
            final boolean expectSuccess;

            public Testcase(String mockId, boolean idFoundInDb, User taskFromDb, User updateForTask, boolean expectSuccess) {
                this.mockId = mockId;
                this.idFoundInDb = idFoundInDb;
                this.taskFromDb = taskFromDb;
                this.updateForTask = updateForTask;
                this.expectSuccess = expectSuccess;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("123", true, mockUser, mockUpdate, true),
                new Testcase("123", false, mockUser, mockUpdate, false),
                new Testcase(null, false, mockUser, mockUpdate, false),

        };

        for (Testcase tc : testcases) {
            when(mockRepo.existsById(tc.mockId)).thenReturn(tc.idFoundInDb);
            if (tc.idFoundInDb) {
                when(mockRepo.findById(tc.mockId)).thenReturn(Optional.ofNullable(tc.taskFromDb));
            } else {
                when(mockRepo.findById(tc.mockId)).thenReturn(null);
            }

            if (tc.expectSuccess) {
                ResponseEntity<String> expected = new ResponseEntity<>("Successfully update User with id :" + tc.mockId, HttpStatus.ACCEPTED);
                try {
                    ResponseEntity<String> actual = t.Handle_updateUser(tc.mockId, new Gson().toJson(tc.updateForTask));
                    assertEquals(expected, actual);
                } catch (HttpClientErrorException e) {
                    throw new RuntimeException(e);
                }

            } else {
                try {
                    t.Handle_updateUser(tc.mockId, new Gson().toJson(tc.updateForTask));
                } catch (HttpClientErrorException e) {
                    HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                    assertEquals(e.getClass(), expectedException.getClass());
                }
            }
        }
    }
    @Test
    public void getUserById() {
        UserController t = new UserController(mockRepo);

        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final User mockTask;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, User mockTask, String expectedOutput) {
                this.userId = userId;
                this.dbReturnSize0 = dbReturnSize0;
                this.mockTask = mockTask;
                this.expectedOutput = expectedOutput;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", false, mockUser, ""),
                new Testcase("1", true, mockUser, "no tasks were found to the provided id"),
                new Testcase(null, true, mockUser, "no tasks were found to the provided id"),
                new Testcase("", true, mockUser, "no tasks were found to the provided id"),
                new Testcase(null, false, mockUser, "no tasks were found to the provided id")
        };
        for (Testcase tc : testcases) {
            if (tc.dbReturnSize0) {
                when(mockRepo.existsById(tc.userId)).thenReturn(false);
            } else {
                when(mockRepo.existsById(tc.userId)).thenReturn(true);
                when(mockRepo.findById(tc.userId)).thenReturn(Optional.ofNullable(mockUser));
            }

            try {
                ResponseEntity<User> expected = new ResponseEntity<>(tc.mockTask, HttpStatus.OK);
                ResponseEntity<User> actual1 = t.Handler_getUsreById(tc.userId);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

@Test
public void Handle_create() {
        UserController t = new UserController(mockRepo);

    class Testcase {
        final String userId;
        final boolean badPayload;
        final User mockTask;
        final String mockPayload;

        public Testcase(String userId, boolean badPayload, User mockTask, String mockPayload) {
            this.userId = userId;
            this.badPayload = badPayload;
            this.mockTask = mockTask;
            this.mockPayload = mockPayload;
        }
    }

    Testcase[] testcases = new Testcase[]{
            new Testcase("1", false, mockUser, new Gson().toJson(mockUser)),
            new Testcase("1", true, mockUser, new Gson().toJson(mockUser)),
    };
    for (Testcase tc : testcases) {
        if (tc.badPayload) {
            when(mockService.validateTaskFields(tc.mockPayload)).thenReturn(false);
        } else {
            when(mockService.validateTaskFields(tc.mockPayload)).thenReturn(true);
            when(mockRepo.insert(tc.mockTask)).thenReturn(mockUser);
        }

        try {
            ResponseEntity<String> expected = new ResponseEntity<>("successfully created User" + tc.mockTask.getUserIdString(), HttpStatus.ACCEPTED);
            ResponseEntity<String> actual1 = t.Handler_createUser(tc.mockPayload);
            assertEquals(actual1.getStatusCode(), expected.getStatusCode());
        } catch (HttpClientErrorException e) {
            HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
            assertEquals(e.getClass(), expectedException.getClass());
        }
    }

}
    @Test
    public void getAllUser() {
        UserController t = new UserController(mockRepo);
        ArrayList<User> mockList = new ArrayList<>();
        for (User task : mockUseArr) {
            mockList.add(task);
        }
        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final ArrayList<User> mockArrayList;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, ArrayList<User> mockArrayList, String expectedOutput) {
                this.userId = userId;
                this.dbReturnSize0 = dbReturnSize0;
                this.mockArrayList = mockArrayList;
                this.expectedOutput = expectedOutput;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", false, mockList, new Gson().toJson(mockList)),
                new Testcase("1", true, new ArrayList<>(), "no tasks were found to the provided id"),
                new Testcase(null, true, new ArrayList<>(), "no tasks were found to the provided id"),
                new Testcase("", true, new ArrayList<>(), "no tasks were found to the provided id"),
                new Testcase(null, false, new ArrayList<>(), "no tasks were found to the provided id")
        };
        for (Testcase tc : testcases) {
            if (tc.dbReturnSize0) {
                when(mockRepo.findAll()).thenReturn(new ArrayList<>());
            } else {
                when(mockRepo.findAll()).thenReturn(tc.mockArrayList);
            }

            try {
                ResponseEntity<List<User>> expected = new ResponseEntity<>(Arrays.asList(mockUser), HttpStatus.OK);
                ResponseEntity<List<User>> actual1 = t.Handle_getAllUsers();
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

}
