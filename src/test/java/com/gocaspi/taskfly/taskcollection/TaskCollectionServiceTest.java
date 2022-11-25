package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import com.gocaspi.taskfly.task.TaskController;
import com.gocaspi.taskfly.task.TaskService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskCollectionServiceTest {
    final private TaskCollectionRepository mockRepo = mock(TaskCollectionRepository.class);
    final private String mockTCID = "1234";
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private String mockTCOwnerID = new ObjectId().toHexString();
    final private List<String> mockTeamMember = Arrays.asList("123", "456", "789");
    final private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID, mockTeamMember);
    final private String mockUserIds = "1";
    final private String mockListId = "1";
    final private String mockTopic = "topic1";
    final private String mockTeam = "team1";
    final private String mockPrio = "prio1";
    final private String mockDesc = "desc1";
    final private String mockDeadline = "11-11-2022";
    final private ObjectId mockObjectId = new ObjectId();
    final private Task.Taskbody mockBody = new Task.Taskbody("mockTopic",true,"mockDescription");

    @Test
    void createTaskCollectionTest(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        TaskCollection taskCollection = new TaskCollection(this.mockTCID, this.mockTCName, this.mockTCTeamID, this.mockTCOwnerID, mockTeamMember);

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
            s.createTaskCollection(tc.mockTaskCollection);
            Mockito.verify(mockRepo, times(1)).insert(tc.mockTaskCollection);
        }
    }
    @Test
    void getTaskCollectionByIDTest(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        Task task = new Task(mockUserIds, mockListId, mockTeam, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task);
        TaskCollectionGetQuery getQuery = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        class Testcase {
            final TaskCollectionGetQuery mockTaskCollection;
            final String mockID;

            public Testcase(TaskCollectionGetQuery taskCollection, String mockID) {
                this.mockTaskCollection = taskCollection;
                this.mockID = mockID;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(getQuery, mockTCID),
                new Testcase(null, mockTCID)
        };
        for (Testcase tc : testcases) {
            try {
                when(mockRepo.findByID(tc.mockID)).thenReturn(tc.mockTaskCollection);
                TaskCollectionGetQuery actual = s.getTaskCollectionByID(tc.mockID);
                assertEquals(actual, tc.mockTaskCollection);
            } catch (Exception e) {

            }

        }
    }
    @Test
    void getTaskCollectionByOwnerIDTest(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        Task task = new Task(mockUserIds, mockListId, mockTeam, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task);
        TaskCollectionGetQuery getQuery = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        List<TaskCollectionGetQuery> getQueries = Arrays.asList(getQuery);
        List<TaskCollectionGetQuery> emptyList = Arrays.asList();

        class Testcase {
            final List<TaskCollectionGetQuery> mockTaskCollection;
            final String mockID;

            public Testcase(List<TaskCollectionGetQuery> taskCollection, String mockID) {
                this.mockTaskCollection = taskCollection;
                this.mockID = mockID;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(getQueries, mockTCID),
                new Testcase(emptyList, mockTCID)
        };
        for (Testcase tc : testcases) {
            try {
                when(mockRepo.findByOwnerID(tc.mockID)).thenReturn(tc.mockTaskCollection);
                List<TaskCollectionGetQuery> actual = s.getTaskCollectionsByUser(tc.mockID);
                assertEquals(actual, tc.mockTaskCollection);
            } catch (Exception e) {

            }

        }
    }
    @Test
    void getTaskCollectionByTeamID(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        Task task = new Task(mockUserIds, mockListId, mockTeam, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task);
        TaskCollectionGetQuery getQuery = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        List<TaskCollectionGetQuery> getQueries = Arrays.asList(getQuery);
        List<TaskCollectionGetQuery> emptyList = Arrays.asList();

        class Testcase {
            final List<TaskCollectionGetQuery> mockTaskCollection;
            final String mockID;

            public Testcase(List<TaskCollectionGetQuery> taskCollection, String mockID) {
                this.mockTaskCollection = taskCollection;
                this.mockID = mockID;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(getQueries, mockTCID),
                new Testcase(emptyList, mockTCID)
        };
        for (Testcase tc : testcases) {
            try {
                when(mockRepo.findByTeamID(tc.mockID)).thenReturn(tc.mockTaskCollection);
                List<TaskCollectionGetQuery> actual = s.getTaskCollectionByTeamID(tc.mockID);
                assertEquals(actual, tc.mockTaskCollection);
            } catch (Exception e) {

            }

        }
    }
    @Test
    void deleteTaskCollectionByID(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);

        class Testcase {
            final String mockID;
            final Boolean exists;

            public Testcase(String mockID, Boolean exists) {
                this.mockID = mockID;
                this.exists = exists;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTCID, true),
                new Testcase(mockTCID, false)
        };
        for (Testcase tc : testcases) {
            try{
                when(mockRepo.existsById(tc.mockID)).thenReturn(tc.exists);
                s.deleteTaskCollectionByID(tc.mockID);
                Mockito.verify(mockRepo, times(1)).deleteById(tc.mockID);
            } catch (Exception e){

            }

        }
    }
    @Test
    void updateTaskCollection(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);

        class Testcase {
            final TaskCollection mockTaskCollection;
            final String mockID;
            final Boolean exists;

            public Testcase(TaskCollection taskCollection, String mockID, Boolean exists) {
                this.mockTaskCollection = taskCollection;
                this.mockID = mockID;
                this.exists = exists;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTC, mockTCID, true),
                new Testcase(mockTC, mockTCID, false),
                new Testcase(new TaskCollection(mockTCID, "", "", "", Arrays.asList()), mockTCID, true)
        };
        for (Testcase tc : testcases) {
            try {
                Optional<TaskCollection> taskCollection = Optional.ofNullable(tc.mockTaskCollection);
                when(mockRepo.findById(tc.mockID)).thenReturn(taskCollection);
                when(mockRepo.existsById(tc.mockID)).thenReturn(tc.exists);
                s.updateTaskCollectionByID(tc.mockID, tc.mockTaskCollection);
                verify(mockRepo, times(1)).save(tc.mockTaskCollection);
            } catch (Exception e) {

            }

        }
    }
}
