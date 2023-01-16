package com.gocaspi.taskfly.taskcollection;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocaspi.taskfly.task.Task;

import com.gocaspi.taskfly.task.TaskService;
import com.google.api.client.http.HttpStatusCodes;
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
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;


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
    final private Task.Taskbody mockBody = new Task.Taskbody("mockTopic", true, "mockDescription",false);
    final private String mockTaskID = new ObjectId().toHexString();
    final private Task mockTask = new Task(mockTCOwnerID, mockTCID, mockTCTeamID, mockTime, mockTaskID, mockBody);

    @BeforeEach
    void beforeEach(){

        mongoTemplate.dropCollection("taskCollection");
    }

    @Test
    void TestCreateTaskCollectionEndpoint() throws Exception{
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
                new Testcase(emptyTaskCollectionRequest, "", HttpStatus.BAD_REQUEST.value()),
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
            final String url;
            final String responseBody;
            final Integer statusCode;

            Testcase(String url, String responseBody, Integer statusCode){
                this.url = url;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase("/tc/owner/" + mockTCOwnerID, objectMapper.writeValueAsString(taskCollectionGetQueryList), HttpStatus.OK.value()),
                new Testcase("/tc/owner/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
                new Testcase("/tc/owner", "", 405)
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get(tc.url))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get(tc.url))
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
            final String url;
            final String responseBody;
            final Integer statusCode;

            Testcase(String url, String responseBody, Integer statusCode){
                this.url = url;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase("/tc/id/" + mockTCID, objectMapper.writeValueAsString(workingTaskCollection), HttpStatus.OK.value()),
                new Testcase("/tc/id/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
                new Testcase("/tc/id", "", 405)
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get(tc.url))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get(tc.url))
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
            final String url;
            final String responseBody;
            final Integer statusCode;

            Testcase(String url, String responseBody, Integer statusCode){
                this.url = url;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase("/tc/team/" + mockTCTeamID, objectMapper.writeValueAsString(taskCollectionGetQueryList), HttpStatus.OK.value()),
                new Testcase("/tc/team/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
                new Testcase("/tc/team", "", 405)
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get(tc.url))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get(tc.url))
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
            final String url;
            final String responseBody;
            final Integer statusCode;

            Testcase(String url, String responseBody, Integer statusCode){
                this.url = url;
                this.responseBody = responseBody;
                this.statusCode = statusCode;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Testcase[] testcases = new Testcase[]{
                new Testcase("/tc/user/" + mockTCOwnerID, objectMapper.writeValueAsString(taskCollectionGetQueryList), HttpStatus.OK.value()),
                new Testcase("/tc/user/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
                new Testcase("/tc/user", "", 405)
        };

        for (Testcase tc: testcases){
            if(tc.statusCode > 299){
                mockMvc.perform(get(tc.url))
                        .andExpect(status().is(tc.statusCode));
            } else {
                mockMvc.perform(get(tc.url))
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
        mongoTemplate.save(mockTC, "taskCollection");
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
                new Testcases(objectMapper.writeValueAsString(mockTC), mockTCID, HttpStatusCodes.STATUS_CODE_ACCEPTED),
                new Testcases(objectMapper.writeValueAsString(mockTC), new ObjectId().toHexString(), HttpStatus.NOT_FOUND.value())
        };

        for (Testcases tc: testcases){
            mockMvc.perform(
                    patch("/tc?id=" + tc.tcID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(tc.requestBody))
                    .andExpect(status().is(tc.statusCode));
        }
    }

    @Test
    void sendTaskCollectionMessageTest(){
        String testMessage = "Test123";
        TaskCollectionService s = mock(TaskCollectionService.class);
        TaskCollectionController t = new TaskCollectionController(s);
        String actual = t.sendTaskCollectionMessage(createMessage("123", testMessage));
        assertEquals(testMessage, actual);
    }

    Message<String> createMessage(String collectionID, String payload){
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.SEND);
        fakeSubscriptionHeader.setHeader("simpDestination", "/collection/" + collectionID);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload(payload);
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }




}
