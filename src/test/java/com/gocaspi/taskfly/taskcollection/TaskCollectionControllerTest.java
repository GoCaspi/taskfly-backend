package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.SecurityConfiguration;
import com.gocaspi.taskfly.advice.ApiExceptionHandler;
import com.gocaspi.taskfly.task.Task;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.*;
@WebMvcTest(TaskCollectionController.class)
@ContextConfiguration
@Import(SecurityConfiguration.class)
@WebAppConfiguration
class TaskCollectionControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private  TaskCollectionService service;
    @MockBean
    private TaskCollectionRepository repo;
    @InjectMocks
    private TaskCollectionController controller;


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
    final private Task mockTask = new Task(mockUserIds, mockListId, mockTeam, mockDeadline, mockObjectId, mockBody);

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    void createTaskCollection() throws Exception {
        final String URL = "/tc";
        TaskCollection taskCollection = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID);


        class Testcase {
            final String requestBody;
            final int httpStatus;
            final String responseBody;


            public Testcase(String requestBody, int httpStatus, String responseBody) {
                this.requestBody = requestBody;
                this.httpStatus = httpStatus;
                this.responseBody = responseBody;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(new Gson().toJson(taskCollection), HttpStatus.CREATED.value(), new Gson().toJson(taskCollection)),
                new Testcase(new Gson().toJson(taskCollection).substring(5,6), HttpStatus.BAD_REQUEST.value(), new Gson().toJson(taskCollection))
        };

        for (Testcase tc: testcases){
            when(service.createTaskCollection(taskCollection)).thenReturn(taskCollection);
            this.mockMvc.perform(post(URL).with(csrf()).contentType("application/json").content(tc.requestBody)).andExpect(status().is(tc.httpStatus));
        }

    }

    @Test
    void getTaskCollectionsByUserID() throws Exception {
        final String URL = "/tc";
        List<Task> tasks = Arrays.asList(mockTask);
        TaskCollectionGetQuery taskCollection = new TaskCollectionGetQuery(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID, tasks);
        List<TaskCollectionGetQuery> taskCollectionGetQueries = Arrays.asList(taskCollection);

        class Testcase {
            final String userIDParam;
            final int httpStatus;
            final String responseBody;


            public Testcase(String userIDParam, int httpStatus, String responseBody) {
                this.userIDParam = userIDParam;
                this.httpStatus = httpStatus;
                this.responseBody = responseBody;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockUserIds, HttpStatus.OK.value(), new Gson().toJson(taskCollection)),
                new Testcase(mockUserIds, HttpStatus.NOT_FOUND.value(), new Gson().toJson(taskCollection))
        };

        for (Testcase tc: testcases){
            when(service.getTaskCollectionsByUser(mockUserIds)).thenReturn(taskCollectionGetQueries);
            this.mockMvc.perform(get(URL).param(tc.userIDParam))
                    .andExpect(status().is(tc.httpStatus));
        }

    }

}
