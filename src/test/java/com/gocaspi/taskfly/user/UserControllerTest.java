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


 class UserControllerTest {
    UserRepository mockRepo = mock(UserRepository.class);
    UserService mockService = mock(UserService.class);
    String mockUserIds = "123";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mocklastname = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    String mockSrole = "";
    ObjectId mockObject_Id = new ObjectId();

     User mockUser = new User(mocklastname,mockEmail,mockPassword,mockFistName,mockTeam);
    User[] mockUseArr = new User[]{mockUser,mockUser};
     UserController ts = new UserController(mockRepo);
    @Test
     void deleteUser() {


        UserController t = new UserController(mockRepo);

        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final User mockUser;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, User mockUser, String expectedOutput) {
                this.userId = userId;
                this.dbReturnSize0 = dbReturnSize0;
                this.mockUser = mockUser;
                this.expectedOutput = expectedOutput;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", false, mockUser, ""),
                new Testcase("1", true, mockUser, "no users were found to the provided id"),
                new Testcase(null, true, mockUser, "no users were found to the provided id"),
                new Testcase("", true, mockUser, "no users were found to the provided id"),
                new Testcase(null, false, mockUser, "no users were found to the provided id")
        };
        for (Testcase tc : testcases) {
            if (tc.dbReturnSize0) {
                when(mockRepo.existsById(tc.userId)).thenReturn(false);
            } else {
                when(mockRepo.existsById(tc.userId)).thenReturn(true);
                //	when(mockRepo.deleteById(tc.userId)).thenReturn(mockTask);
            }

            try {
                ResponseEntity<String> expected = new ResponseEntity<>("successfully deleted user with id: " + tc.userId, HttpStatus.ACCEPTED);
                ResponseEntity<String> actual1 = t.deleteUser(tc.userId);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }
    @Test
     void updateUser() {
        UserController t = new UserController(mockRepo);
        User mockUpdate = new User( mocklastname+"updated",mockEmail + "updated", mockPassword + "updated", mockTeam +"updated", mockFistName+"update");

        class Testcase {
            final String mockId;
            final boolean idFoundInDb;
            final User taskFromDb;
            final User updateForUser;
            final boolean expectSuccess;

            public Testcase(String mockId, boolean idFoundInDb, User taskFromDb, User updateForUser, boolean expectSuccess) {
                this.mockId = mockId;
                this.idFoundInDb = idFoundInDb;
                this.taskFromDb = taskFromDb;
                this.updateForUser = updateForUser;
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
                    ResponseEntity<String> actual = t.handleUpdateUser(tc.mockId, new Gson().toJson(tc.updateForUser));
                    assertEquals(expected, actual);
                } catch (HttpClientErrorException e) {
                    throw new RuntimeException(e);
                }

            } else {
                try {
                    t.handleUpdateUser(tc.mockId, new Gson().toJson(tc.updateForUser));
                } catch (HttpClientErrorException e) {
                    HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                    assertEquals(e.getClass(), expectedException.getClass());
                }
            }
        }
    }
    @Test
     void getUserById() {
        UserController t = new UserController(mockRepo);

        class Testcase {
            final String userId;
            final boolean dbReturnSize0;
            final User mockUser;
            final String expectedOutput;

            public Testcase(String userId, boolean dbReturnSize0, User mockUser, String expectedOutput) {
                this.userId = userId;
                this.dbReturnSize0 = dbReturnSize0;
                this.mockUser = mockUser;
                this.expectedOutput = expectedOutput;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", false, mockUser, ""),
                new Testcase("1", true, mockUser, "no users were found to the provided id"),
                new Testcase(null, true, mockUser, "no users were found to the provided id"),
                new Testcase("", true, mockUser, "no users were found to the provided id"),
                new Testcase(null, false, mockUser, "no users were found to the provided id")
        };
        for (Testcase tc : testcases) {
            if (tc.dbReturnSize0) {
                when(mockRepo.existsById(tc.userId)).thenReturn(false);
            } else {
                when(mockRepo.existsById(tc.userId)).thenReturn(true);
                when(mockRepo.findById(tc.userId)).thenReturn(Optional.ofNullable(mockUser));
            }

            try {
                ResponseEntity<User> expected = new ResponseEntity<>(tc.mockUser, HttpStatus.OK);
                ResponseEntity<User> actual1 = t.handlerGetUsreById(tc.userId);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

@Test
 void Handle_create() {
        UserController t = new UserController(mockRepo);

    class Testcase {
        final String userId;
        final boolean badPayload;
        final User mockUser;
        final String mockPayload;

        public Testcase(String userId, boolean badPayload, User mockUser, String mockPayload) {
            this.userId = userId;
            this.badPayload = badPayload;
            this.mockUser = mockUser;
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
            when(mockRepo.insert(tc.mockUser)).thenReturn(mockUser);
        }

        try {
            ResponseEntity<String> expected = new ResponseEntity<>("successfully created User" , HttpStatus.ACCEPTED);
            ResponseEntity<String> actual1 = t.handlerCreateUser(tc.mockPayload);
            assertEquals(actual1.getStatusCode(), expected.getStatusCode());
        } catch (HttpClientErrorException e) {
            HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
            assertEquals(e.getClass(), expectedException.getClass());
        }
    }

}
    @Test
     void getAllUser() {
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
                new Testcase("1", true, new ArrayList<>(), "no users were found to the provided id"),
                new Testcase(null, true, new ArrayList<>(), "no users were found to the provided id"),
                new Testcase("", true, new ArrayList<>(), "no users were found to the provided id"),
                new Testcase(null, false, new ArrayList<>(), "no users were found to the provided id")
        };
        for (Testcase tc : testcases) {
            if (tc.dbReturnSize0) {
                when(mockRepo.findAll()).thenReturn(new ArrayList<>());
            } else {
                when(mockRepo.findAll()).thenReturn(tc.mockArrayList);
            }

            try {
                ResponseEntity<List<User>> expected = new ResponseEntity<>(Arrays.asList(mockUser), HttpStatus.OK);
                ResponseEntity<List<User>> actual1 = t.handleGetAllUsers();
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }
    }

}
