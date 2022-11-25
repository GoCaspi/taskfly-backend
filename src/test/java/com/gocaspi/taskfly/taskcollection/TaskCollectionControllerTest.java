package com.gocaspi.taskfly.taskcollection;


import com.gocaspi.taskfly.task.Task;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.HttpClientErrorException;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import java.util.*;
class TaskCollectionControllerTest {

    private final HttpClientErrorException httpNotFoundError = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);


    TaskCollectionService mockService = mock(TaskCollectionService.class);
    final private String mockTCID = "1234";
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private String mockTCOwnerID = new ObjectId().toHexString();

    final private List<String> mockTeamMember = Arrays.asList("123", "456", "789");
    final private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID, mockTeamMember);
    final private String mockUserIds = "1";
    final private String mockListId = "1";

    final private String mockTeam = "team1";

    final private String mockDeadline = "11-11-2022";
    final private ObjectId mockObjectId = new ObjectId();
    final private Task.Taskbody mockBody = new Task.Taskbody("mockTopic", true, "mockDescription");


    @Test
    void createNewTaskCollection() {
        TaskCollectionController t = new TaskCollectionController(mockService);
        TaskCollection taskCollection = new TaskCollection(this.mockTCID, this.mockTCName, this.mockTCTeamID, this.mockTCOwnerID, this.mockTeamMember);

        class Testcase {
            final TaskCollection mockTaskCollection;

            public Testcase(TaskCollection taskCollection) {
                this.mockTaskCollection = taskCollection;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(taskCollection)
        };
        for (Testcase tc : testcases) {
            when(mockService.createTaskCollection(tc.mockTaskCollection)).thenReturn(tc.mockTaskCollection);


            try {
                ResponseEntity<TaskCollection> expected = new ResponseEntity<>(tc.mockTaskCollection, HttpStatus.CREATED);
                ResponseEntity<TaskCollection> actual1 = t.createTaskCollectionEndpoint(tc.mockTaskCollection);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }

    }
    @Test
    void getTaskCollectionByUserID() {
        TaskCollectionController t = new TaskCollectionController(mockService);
        Task task = new Task(mockUserIds, mockListId, mockTeam, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task);
        TaskCollectionGetQuery getQuery = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        List<TaskCollectionGetQuery> getQueries = Arrays.asList(getQuery);

        class Testcase {
            final List<TaskCollectionGetQuery> mockTaskCollection;
            final String mockUserID;
            final Boolean notFound;

            public Testcase(List<TaskCollectionGetQuery> taskCollection, String mockUserID, Boolean notFound) {
                this.mockTaskCollection = taskCollection;
                this.mockUserID = mockUserID;
                this.notFound = notFound;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(getQueries, mockUserIds, false),
                new Testcase(getQueries, mockUserIds, true)
        };
        for (Testcase tc : testcases) {
            if (tc.notFound) {
                when(mockService.getTaskCollectionsByUser(tc.mockUserID)).thenThrow(httpNotFoundError);
            } else {
                when(mockService.getTaskCollectionsByUser(tc.mockUserID)).thenReturn(tc.mockTaskCollection);
            }

            try {
                ResponseEntity<List<TaskCollectionGetQuery>> expected = new ResponseEntity<>(HttpStatus.OK);
                ResponseEntity<List<TaskCollectionGetQuery>> actual1 = t.getTaskCollectionsByUserID(tc.mockUserID);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }

    }
    @Test
    void getTaskCollectionsByTeamID() {
        TaskCollectionController t = new TaskCollectionController(mockService);
        Task task = new Task(mockUserIds, mockListId, mockTeam, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task);
        TaskCollectionGetQuery getQuery = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        List<TaskCollectionGetQuery> getQueries = Arrays.asList(getQuery);

        class Testcase {
            final List<TaskCollectionGetQuery> mockTaskCollection;
            final String mockTeamID;
            final Boolean notFound;

            public Testcase(List<TaskCollectionGetQuery> taskCollection, String mockTeamID, Boolean notFound) {
                this.mockTaskCollection = taskCollection;
                this.mockTeamID = mockTeamID;
                this.notFound = notFound;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(getQueries, mockUserIds, false),
                new Testcase(getQueries, mockUserIds, true)
        };
        for (Testcase tc : testcases) {
            if (tc.notFound) {
                when(mockService.getTaskCollectionByTeamID(tc.mockTeamID)).thenThrow(httpNotFoundError);
            } else {
                when(mockService.getTaskCollectionByTeamID(tc.mockTeamID)).thenReturn(tc.mockTaskCollection);
            }

            try {
                ResponseEntity<List<TaskCollectionGetQuery>> expected = new ResponseEntity<>(HttpStatus.OK);
                ResponseEntity<List<TaskCollectionGetQuery>> actual1 = t.getTaskCollectionsByTeamID(tc.mockTeamID);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }

    }
    @Test
    void getTaskCollectionsByID() {
        TaskCollectionController t = new TaskCollectionController(mockService);
        Task task = new Task(mockUserIds, mockListId, mockTeam, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task);
        TaskCollectionGetQuery getQuery = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);

        class Testcase {
            final TaskCollectionGetQuery mockTaskCollection;
            final String mockID;
            final Boolean notFound;

            public Testcase(TaskCollectionGetQuery taskCollection, String mockID, Boolean notFound) {
                this.mockTaskCollection = taskCollection;
                this.mockID = mockID;
                this.notFound = notFound;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(getQuery, mockUserIds, false),
                new Testcase(getQuery, mockUserIds, true)
        };
        for (Testcase tc : testcases) {
            if (tc.notFound) {
                when(mockService.getTaskCollectionByID(tc.mockID)).thenThrow(httpNotFoundError);
            } else {
                when(mockService.getTaskCollectionByID(tc.mockID)).thenReturn(tc.mockTaskCollection);
            }

            try {
                ResponseEntity<TaskCollectionGetQuery> expected = new ResponseEntity<>(HttpStatus.OK);
                ResponseEntity<TaskCollectionGetQuery> actual1 = t.getTaskCollectionByID(tc.mockID);
                assertEquals(actual1.getStatusCode(), expected.getStatusCode());
            } catch (HttpClientErrorException e) {
                HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
                assertEquals(e.getClass(), expectedException.getClass());
            }
        }

    }
    @Test
    void deleteTaskCollection() {
        TaskCollectionController t = new TaskCollectionController(mockService);
        class Testcase {
            final String mockID;

            public Testcase(String mockID) {
                this.mockID = mockID;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockUserIds),
        };
        for (Testcase tc : testcases) {
            ResponseEntity<String> actual = t.deleteTaskCollectionByID(tc.mockID);
            assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
            Mockito.verify(mockService, times(1)).deleteTaskCollectionByID(tc.mockID);

        }

    }
    @Test
    void updateTaskCollection() {
        TaskCollectionController t = new TaskCollectionController(mockService);
        class Testcase {
            final String mockID;
            final TaskCollection taskCollection;

            public Testcase(String mockID, TaskCollection taskCollection) {
                this.mockID = mockID;
                this.taskCollection = taskCollection;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockUserIds, mockTC),
        };
        for (Testcase tc : testcases) {
            ResponseEntity<TaskCollection> actual = t.patchTaskCollectionByID(tc.mockID, tc.taskCollection);
            assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
            Mockito.verify(mockService, times(1)).updateTaskCollectionByID(tc.mockID, tc.taskCollection);

        }

    }


}
