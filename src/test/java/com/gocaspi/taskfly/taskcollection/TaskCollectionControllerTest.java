package com.gocaspi.taskfly.taskcollection;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocaspi.taskfly.task.Task;

import com.google.gson.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
@SpringBootTest
@AutoConfigureMockMvc
class TaskCollectionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MongoTemplate mongoTemplate;
    private final HttpClientErrorException httpNotFoundError = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
    LocalDateTime mockTime = LocalDateTime.now();
    final private String mockTCID = new ObjectId().toHexString();
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private String mockTCOwnerID = new ObjectId().toHexString();
    final private List<String> mockTeamMember = Arrays.asList("123", "456", "789");
    final private List<String> mockMember = Arrays.asList("123", "456", "789");

    private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID, mockMember);
    final private Task.Taskbody mockBody = new Task.Taskbody("mockTopic", true, "mockDescription");
    final private String mockTaskID = new ObjectId().toHexString();
    final private Task mockTask = new Task(mockTCOwnerID, mockTCID, mockTCTeamID, mockTime, mockTaskID, mockBody);

    @BeforeEach
    void beforeEach(){

        mongoTemplate.dropCollection("taskCollection");
    }

    @Test
    void TestCreateTaskCollectionEndpoint() throws Exception{
        mongoTemplate.dropCollection("taskCollection");
        var taskCollectionRequest = new Gson().toJson(mockTC);
        TaskCollection emptyTaskCollection = new TaskCollection(new ObjectId().toHexString(), "", mockTCTeamID, mockTCOwnerID, mockTeamMember);
        var emptyTaskCollectionRequest = new Gson().toJson(emptyTaskCollection);
        class Testcase {
            final String mockRequestBody;
            final String mockResponseBody;
            final Integer statusCode;

            public Testcase(String mockRequestBody, String mockResponseBody, Integer statusCode){
                this.mockRequestBody = mockRequestBody;
                this.mockResponseBody = mockResponseBody;
                this.statusCode = statusCode;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase(taskCollectionRequest, taskCollectionRequest, HttpStatus.CREATED.value()),
                new Testcase(emptyTaskCollectionRequest, "", HttpStatus.BAD_REQUEST.value())
        };

        for(Testcase tc: testcases){
            mockMvc.perform(post("/tc")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(tc.mockRequestBody))
                    .andExpect(status().is(tc.statusCode))
                    .andExpect(content().string(tc.mockResponseBody));
        }

    }
    @Test
    void TestGetTaskCollectionsByOwnerID() throws Exception{
        TaskCollectionGetQuery workingTaskCollection = new TaskCollectionGetQuery(mockTC);
        List<Task> mockTaskList = List.of(mockTask);
        workingTaskCollection.setTasks(mockTaskList);
        List<TaskCollectionGetQuery> taskCollectionGetQueryList = List.of(workingTaskCollection);
        mongoTemplate.save(mockTask, "task");
        mongoTemplate.save(mockTC, "taskCollection");

        class Testcase {
            final String tcID;
            final String responseBody;
            final Integer statusCode;

            Testcase(String tcID, String responseBody, Integer statusCode){
                this.tcID = tcID;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTCOwnerID, objectMapper.writeValueAsString(taskCollectionGetQueryList), HttpStatus.OK.value()),
                new Testcase(new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value())
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get("/tc/owner/" + tc.tcID))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get("/tc/owner/" + tc.tcID))
                        .andExpect(content().string(tc.responseBody))
                        .andExpect(status().is(tc.statusCode));
            }

        }


    }

    @Test
    void TestGetTaskCollectionByID() throws Exception{
        TaskCollectionGetQuery workingTaskCollection = new TaskCollectionGetQuery(mockTC);
        List<Task> mockTaskList = List.of(mockTask);
        workingTaskCollection.setTasks(mockTaskList);
        mongoTemplate.save(mockTask, "task");
        mongoTemplate.save(mockTC, "taskCollection");

        class Testcase {
            final String tcID;
            final String responseBody;
            final Integer statusCode;

            Testcase(String tcID, String responseBody, Integer statusCode){
                this.tcID = tcID;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTCID, objectMapper.writeValueAsString(workingTaskCollection), HttpStatus.OK.value()),
                new Testcase(new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value())
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get("/tc/id/" + tc.tcID))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get("/tc/id/" + tc.tcID))
                        .andExpect(content().string(tc.responseBody))
                        .andExpect(status().is(tc.statusCode));
            }

        }

    }

    @Test
    void TestGetTaskCollectionsByTeamID() throws Exception{
        TaskCollectionGetQuery workingTaskCollection = new TaskCollectionGetQuery(mockTC);
        List<Task> mockTaskList = List.of(mockTask);
        workingTaskCollection.setTasks(mockTaskList);
        List<TaskCollectionGetQuery> taskCollectionGetQueryList = List.of(workingTaskCollection);

        mongoTemplate.save(mockTask, "task");
        mongoTemplate.save(mockTC, "taskCollection");

        class Testcase {
            final String tcID;
            final String responseBody;
            final Integer statusCode;

            Testcase(String tcID, String responseBody, Integer statusCode){
                this.tcID = tcID;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTCTeamID, objectMapper.writeValueAsString(taskCollectionGetQueryList), HttpStatus.OK.value()),
                new Testcase(new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value())
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get("/tc/team/" + tc.tcID))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get("/tc/team/" + tc.tcID))
                        .andExpect(content().string(tc.responseBody))
                        .andExpect(status().is(tc.statusCode));
            }

        }
    }
    @Test
    void TestGetTaskCollectionByUserID() throws Exception {
        TaskCollectionGetQuery workingTaskCollection = new TaskCollectionGetQuery(mockTC);
        List<Task> mockTaskList = List.of(mockTask);
        workingTaskCollection.setTasks(mockTaskList);
        List<TaskCollectionGetQuery> taskCollectionGetQueryList = List.of(workingTaskCollection);

        mongoTemplate.save(mockTask, "task");
        mongoTemplate.save(mockTC, "taskCollection");

        class Testcase {
            final String tcID;
            final String responseBody;
            final Integer statusCode;

            Testcase(String tcID, String responseBody, Integer statusCode){
                this.tcID = tcID;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTCOwnerID, objectMapper.writeValueAsString(taskCollectionGetQueryList), HttpStatus.OK.value()),
                new Testcase(new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value())
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get("/tc/user/" + tc.tcID))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get("/tc/user/" + tc.tcID))
                        .andExpect(content().string(tc.responseBody))
                        .andExpect(status().is(tc.statusCode));
            }

        }
    }

    @Test
    void TestDeleteTaskCollectionByID() throws Exception {
        mongoTemplate.save(mockTC, "taskCollection");

        class Testcases{
            String tcID;
            Integer statusCode;

            Testcases(String tcID, Integer statusCode){
                this.tcID = tcID;
                this.statusCode = statusCode;
            }
        }

        Testcases[] testcases = new Testcases[]{
                new Testcases(mockTCID, HttpStatus.ACCEPTED.value()),
                new Testcases(new ObjectId().toHexString(), HttpStatus.NOT_FOUND.value())
        };

        for (Testcases tc: testcases){
            mockMvc.perform(
                    delete("/tc/" + tc.tcID))
                    .andExpect(status().is(tc.statusCode));
        }


    }

    @Test
    void TestPatchTaskCollectionByID() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        class Testcases {
            final String requestBody;
            final String tcID;
            final Integer statusCode;

            Testcases(String requestBody, String tcID, Integer statusCode){
                this.requestBody = requestBody;
                this.tcID = tcID;
                this.statusCode = statusCode;
            }


        }
        Testcases[] testcases = new Testcases[]{
                new Testcases(objectMapper.writeValueAsString(mockTC), mockTCID, HttpStatus.)
        }
    }




}
