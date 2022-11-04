package com.gocaspi.taskfly.user;


import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
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


public class userControllerTest {
    userRepository mockRepo = mock(userRepository.class);
    userService mockService = mock(userService.class);
    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    ObjectId mockObject_Id = new ObjectId();
    user mockUser = new user(mockUserIds, mockListId, mockFistName, mockTeam, mockLastName, mockEmail, mockPassword, mockObject_Id);
    user[] mockUseArr = new user[]{mockUser,mockUser};
    @Test
    public void deleteUser() {


        userController t = new userController(mockRepo);

        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final user mockTask;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, user mockTask, String expectedOutput) {
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
        userController t = new userController(mockRepo);
        user mockUpdate = new user(mockUserIds, mockListId, mockEmail + "updated", mockPassword + "updated", mockFistName, mockLastName + "updated",mockService+"updated",mockObject_Id);

        class Testcase {
            final String mockId;
            final boolean idFoundInDb;
            final user taskFromDb;
            final user updateForTask;
            final boolean expectSuccess;

            public Testcase(String mockId, boolean idFoundInDb, user taskFromDb, user updateForTask, boolean expectSuccess) {
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
                    ResponseEntity<String> actual = t.handleUpdateUser(tc.mockId, new Gson().toJson(tc.updateForTask));
                    assertEquals(expected, actual);
                } catch (HttpClientErrorException e) {
                    throw new RuntimeException(e);
                }

            } else {
                try {
                    t.handleUpdateUser(tc.mockId, new Gson().toJson(tc.updateForTask));
                } catch (HttpClientErrorException e) {
                    HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                    assertEquals(e.getClass(), expectedException.getClass());
                }
            }
        }
    }
    @Test
    public void getUserById() {
        userController t = new userController(mockRepo);

        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final user mockTask;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, user mockTask, String expectedOutput) {
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
                ResponseEntity<user> expected = new ResponseEntity<>(tc.mockTask, HttpStatus.OK);
                ResponseEntity<user> actual1 = t.handlerGetUsreById(tc.userId);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

@Test
public void Handle_create() {
        userController t = new userController(mockRepo);

    class Testcase {
        final String userId;
        final boolean badPayload;
        final user mockTask;
        final String mockPayload;

        public Testcase(String userId, boolean badPayload, user mockTask, String mockPayload) {
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
            ResponseEntity<String> actual1 = t.handlerCreateUser(tc.mockPayload);
            assertEquals(actual1.getStatusCode(), expected.getStatusCode());
        } catch (HttpClientErrorException e) {
            HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
            assertEquals(e.getClass(), expectedException.getClass());
        }
    }

}
    @Test
    public void getAllUser() {
        userController t = new userController(mockRepo);
        ArrayList<user> mockList = new ArrayList<>();
        for (user task : mockUseArr) {
            mockList.add(task);
        }
        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final ArrayList<user> mockArrayList;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, ArrayList<user> mockArrayList, String expectedOutput) {
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
                ResponseEntity<List<user>> expected = new ResponseEntity<>(Arrays.asList(mockUser), HttpStatus.OK);
                ResponseEntity<List<user>> actual1 = t.handleGetAllUsers();
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

}
