package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.SecurityConfiguration;
import com.gocaspi.taskfly.task.Task;
import com.google.gson.Gson;
import com.mongodb.connection.ServerDescription;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.*;
@WebMvcTest(TaskCollectionController.class)
@ContextConfiguration
@Import(SecurityConfiguration.class)
@WebAppConfiguration
public class TaskCollectionControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskCollectionService service;



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

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }



/*
    @Test
    void createTaskCollection(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        TaskCollectionController t = new TaskCollectionController(s);
        TaskCollection taskCollection = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID);
        ResponseEntity<TaskCollection> actual = t.createTaskCollectionEndpoint(taskCollection);
        HttpStatus code = actual.getStatusCode();
        String taskCollectionString = new Gson().toJson(taskCollection);
        String bodyString = new Gson().toJson(actual.getBody());
        assertEquals(taskCollectionString, bodyString);
        assertEquals(HttpStatus.CREATED, code);
    }
    @Test
    void getTaskCollectionByUserID(){
        TaskCollectionController c = new TaskCollectionController(mockService);
        Task task1 = new Task(mockUserIds, mockTCID, mockTCTeamID, mockDeadline, mockObjectId, mockBody);
        List<Task> taskList = Arrays.asList(task1);
        TaskCollectionGetQuery tcSingle = new TaskCollectionGetQuery(mockTCName, mockTCTeamID, mockTCID, mockTCOwnerID, taskList);
        List<TaskCollectionGetQuery> tcReturn = Arrays.asList(tcSingle);
        when(mockService.getTaskCollectionsByUser(mockTCOwnerID)).thenThrow(ConstraintViolationException.class);
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
        ResponseEntity<TaskCollectionGetQuery> actual = c.getTaskCollectionByID(mockTCOwnerID);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        String tcString = new Gson().toJson(tcSingle);
        String bodyString = new Gson().toJson(actual.getBody());
        assertEquals(tcString, bodyString);
    }
*/
    @Test
    public void createTaskCollectionShouldReturn200() throws Exception {
        TaskCollection taskCollection = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID);
        when(service.createTaskCollection(taskCollection)).thenReturn(taskCollection);
        this.mockMvc.perform(post("/tc").with(csrf()).contentType("application/json").content(new Gson().toJson(taskCollection))).andExpect(status().isCreated());
    }
}
