package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

public class TaskCollectionControllerTest {

    final private TaskCollectionRepository mockRepo = mock(TaskCollectionRepository.class);
    final private TaskCollectionService mockService = mock(TaskCollectionService.class);
    final private String mockTCID = "1234";
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private String mockTCOwnerID = new ObjectId().toHexString();
    final private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID);
    final private String mockUserIds = "1";
    final private String mockListId = "1";
    final private String mockTopic = "topic1";
    final private String mockTeam = "team1";
    final private String mockPrio = "prio1";
    final private String mockDesc = "desc1";
    final private String mockDeadline = "11-11-2022";
    final private ObjectId mockObjectId = new ObjectId();
    final private Task.Taskbody mockBody = new Task.Taskbody("mockTopic","mockPrio","mockDescription");

    @Test
    void createTaskCollection(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        TaskCollectionController t = new TaskCollectionController(s);
        TaskCollection taskCollection = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID);
        ResponseEntity<String> actual = t.createTaskCollectionEndpoint(taskCollection);
        HttpStatus code = actual.getStatusCode();
        String taskCollectionString = new Gson().toJson(taskCollection);
        assertEquals(taskCollectionString, actual.getBody());
        assertEquals(HttpStatus.CREATED, code);
    }
    @Test
    void getTaskCollectionByUserID(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        TaskCollectionController c = new TaskCollectionController(s);
        Task task1 = new Task(mockUserIds, mockTCID, mockTCTeamID, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task1);
        TaskCollectionGetQuery tcSingle = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        List<TaskCollectionGetQuery> tcReturn = Arrays.asList(tcSingle);
        when(s.getTaskCollectionsByUser(mockTCOwnerID)).thenReturn(tcReturn);
        ResponseEntity<List<TaskCollectionGetQuery>> actual = c.getTaskCollectionsByUserID(mockTCOwnerID);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        String tcString = new Gson().toJson(tcReturn);
        String bodyString = new Gson().toJson(actual.getBody());
        assertEquals(tcString, bodyString);
    }
    @Test
    void getTaskCollectionByID(){
        TaskCollectionController c = new TaskCollectionController(mockService);
        Task task1 = new Task(mockUserIds, mockTCID, mockTCTeamID, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task1);
        TaskCollectionGetQuery tcSingle = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        when(mockService.getTaskCollectionByID(mockTCOwnerID)).thenReturn(tcSingle);
        ResponseEntity<List<TaskCollectionGetQuery>> actual = c.getTaskCollectionByID(mockTCOwnerID);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        String tcString = new Gson().toJson(tcSingle);
        assertEquals(tcString, actual.getBody());
    }
}
