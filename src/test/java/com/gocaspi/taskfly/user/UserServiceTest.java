package com.gocaspi.taskfly.user;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserServiceTest {
    UserRepository mockRepo = mock(UserRepository.class);

    String mockUserIds = "123";
    String mockListId = "1";
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";

    User mockUser = new User(mockUserIds, mockListId, mockFistName, mockTeam, mockLastName, mockEmail, mockPassword);

    @Test
    void getService_AllUser() {

        UserService t = new UserService(mockRepo);
        User[] mockTaskArr = new User[]{mockUser, mockUser};
        ArrayList<User> mockList = new ArrayList<>();
        for (User task : mockTaskArr) {
            mockList.add(task);
        }

        class Testcase {
            final String id;
            final List<User> dbReturn;
            final List<User> expected;

            public Testcase(String id, List<User> dbReturn, List<User> expected) {
                this.id = id;
                this.dbReturn = dbReturn;
                this.expected = expected;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("123", mockList, mockList),
                new Testcase("1", new ArrayList<>(), new ArrayList<>())
        };
        for (Testcase tc : testcases) {
            when(mockRepo.findAll()).thenReturn(tc.dbReturn);
            List actual = t.getServiceAllUser();
            assertEquals(tc.expected, actual);
        }
    }

    @Test
    void updateUser() {
        UserService service = new UserService(mockRepo);
        User emptyTeam = new User(null, null, null, null, null, null, null);

        class Testcase {
            final String mockId;
            final User mockUpdate;
            final boolean expected;

            public Testcase(String mockId, User mockUpdate, boolean expected) {
                this.mockId = mockId;
                this.mockUpdate = mockUpdate;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", mockUser, true),
                new Testcase("1", mockUser, false),
                new Testcase("1", emptyTeam, true),
        };

        for (Testcase tc : testcases) {
            try {
                Optional<User> optionalUser = Optional.ofNullable(tc.mockUpdate);
                when(mockRepo.findById(tc.mockId)).thenReturn(optionalUser);
                when(mockRepo.existsById(tc.mockId)).thenReturn(tc.expected);
                service.updateService(tc.mockId, tc.mockUpdate);
                verify(mockRepo, times(1)).save(tc.mockUpdate);
            } catch (Exception e) {

            }

        }
    }

    @Test
    void getUserById() {
        UserService service = new UserService(mockRepo);

        class Testcase {
            final String mockId;
            final boolean expected;
            final User mockTeam;

            public Testcase(String mockId, boolean expected, User mockTeam) {
                this.mockId = mockId;
                this.expected = expected;
                this.mockTeam = mockTeam;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("1", true, mockUser),
                new Testcase("1", true, null),
                new Testcase("", false, null)
        };

        for (Testcase tc : testcases) {
            try {
                when(mockRepo.existsById(tc.mockId)).thenReturn(tc.expected);
                Optional<User> optionalUser = Optional.ofNullable(tc.mockTeam);
                when(mockRepo.findById(tc.mockId)).thenReturn(optionalUser);
                service.getServicebyid(tc.mockId);
                verify(mockRepo, times(1)).findById(tc.mockId);
            } catch (Exception e) {

            }
        }
    }

    @Test
    void deleteUser() {
        UserService service = new UserService(mockRepo);
        class Testcase {
            final String mockId;
            final boolean expected;

            public Testcase(String mockId, boolean expected) {
                this.mockId = mockId;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase("1", true),
                new Testcase("", false),
        };

        for (Testcase tc : testcases) {
            try {
                when(mockRepo.existsById(tc.mockId)).thenReturn(tc.expected);
                service.deleteService(tc.mockId);
                verify(mockRepo, times(1)).deleteById(tc.mockId);
            } catch (Exception e) {

            }
        }
    }

    @Test
    void validateTaskFields() {
        UserService t = new UserService(mockRepo);
        class Testcase {
            final User taskInput;
            final boolean expected;

            public Testcase(User testTask, boolean expected) {
                this.taskInput = testTask;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockUser, true),
                new Testcase(new User(null, null, null, null, null, null, null), false),
                new Testcase(new User("test", "test", "test", "test", "test", "test", "test"), true),
                new Testcase(new User("", "", "", "", "", "", ""), true)
        };

        for (Testcase tc : testcases) {
            boolean actualOut = t.validateTaskFields(new Gson().toJson(tc.taskInput));
            assertEquals(tc.expected, actualOut);
        }
    }

    @Test
    void createUser() {
        UserService service = new UserService(mockRepo);
        User mockTeamTest = new User();

        class Testcase {
            final User mockInsert;
            final boolean expected;

            public Testcase(User mockInsert, boolean expected) {
                this.mockInsert = mockInsert;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockUser, false),
                new Testcase(mockTeamTest, true)
        };

        for (Testcase tc : testcases) {
            try {
                service.postService(tc.mockInsert);
                verify(mockRepo, times(1)).insert(tc.mockInsert);
            } catch (Exception e) {

            }

        }
    }
}
