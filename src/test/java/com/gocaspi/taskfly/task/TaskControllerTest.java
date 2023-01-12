package com.gocaspi.taskfly.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocaspi.taskfly.taskcollection.TaskCollection;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	MongoTemplate mongoTemplate;

	final private String mockTCID = new ObjectId().toHexString();
	final private String mockTCName = "TaskCollection1";
	final private String mockTCTeamID = new ObjectId().toHexString();
	final private String mockTCOwnerID = new ObjectId().toHexString();
	final private List<String> mockTeamMember = Arrays.asList("123", "456", "789");
	final private List<String> mockMember = Arrays.asList("123", "456", "789");

	private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID, mockMember);

	final private LocalDateTime mockTime = LocalDateTime.now();
	final private TaskRepository mockRepo = mock(TaskRepository.class);
	final private TaskService mockService = mock(TaskService.class);
	final private String mockUserIds = new ObjectId().toHexString();
	final private String mockListId = mockTCID;
	final private String mockTeam = mockTCTeamID;
	final private String mockObjectId = new ObjectId().toHexString();

	final private Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");

	final private Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockTime,mockObjectId,mockbody);
	final private Task[] mockTaskArr = new Task[]{mockTask, mockTask};
	@BeforeEach
	void beforeEach(){

		mongoTemplate.dropCollection("task");
	}

	@Test
	void TestHandleCreateNewTask() throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		String taskRequest = objectMapper.writeValueAsString(mockTask);
		Task invalidTask = new Task(mockUserIds,"",mockTeam,mockTime,mockObjectId,mockbody);
		String invalidTaskRequest = objectMapper.writeValueAsString(invalidTask);
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
				new Testcase(taskRequest, "successfully created task with id: " + mockObjectId, HttpStatus.ACCEPTED.value()),
				new Testcase(invalidTaskRequest, "", HttpStatus.BAD_REQUEST.value()),
		};

		for(Testcase tc: testcases){
			mockMvc.perform(post("/task")
							.contentType(MediaType.APPLICATION_JSON)
							.content(tc.mockRequestBody))
					.andExpect(status().is(tc.statusCode));
		}
	}

	@Test
	void TestHandleGetAllTasks() throws Exception {
		mongoTemplate.save(mockTask);
		List<Task> taskList = List.of(mockTask);
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
				new Testcase("/task/userId/" + mockUserIds, objectMapper.writeValueAsString(taskList), HttpStatus.OK.value()),
				new Testcase("/task/userId/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
				new Testcase("/task/userId", "", 405)
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
	void TestHandleGetTaskById() throws Exception{
		mongoTemplate.save(mockTask);
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
				new Testcase("/task/taskId/" + mockObjectId, objectMapper.writeValueAsString(mockTask), HttpStatus.OK.value()),
				new Testcase("/task/taskId/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
				new Testcase("/task/taskId", "", 405)
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
	void TestHandleGetTaskByUserIDandPriority() throws Exception{
		mongoTemplate.save(mockTask);
		List<Task> validList = List.of(mockTask);

		Task.Taskbody invalidTaskBody = new Task.Taskbody("test", false, "test");
		Task invalidTask = new Task(mockUserIds, new ObjectId().toHexString(), mockTeam, mockTime, new ObjectId().toHexString(), invalidTaskBody);
		mongoTemplate.save(invalidTask);
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
				new Testcase("/task/priority/" + mockUserIds, objectMapper.writeValueAsString(validList), HttpStatus.OK.value()),
				new Testcase("/task/priority/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
				new Testcase("/task/priority", "", 405)
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
	void TestHandleGetPrivateTasksByUser() throws Exception{
		mongoTemplate.save(mockTC);
		mongoTemplate.save(mockTask);
		String privateTCID = new ObjectId().toHexString();
		TaskCollection privateTC = new TaskCollection(privateTCID, mockTCName, "", mockUserIds, new ArrayList<>());
		mongoTemplate.save(privateTC);
		Task privateTask = new Task(mockUserIds,privateTCID,mockTeam,mockTime, new ObjectId().toHexString(), mockbody);
		List<Task> validTasks = List.of(privateTask);
		mongoTemplate.save(privateTask);
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
				new Testcase("/task/private/" + mockUserIds, objectMapper.writeValueAsString(validTasks), HttpStatus.OK.value()),
				new Testcase("/task/private/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
				new Testcase("/task/private", "", 405)
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
	void TestHandleGetSharedTasksByUser() throws Exception{
		mongoTemplate.save(mockTC);
		mongoTemplate.save(mockTask);
		String privateTCID = new ObjectId().toHexString();
		TaskCollection privateTC = new TaskCollection(privateTCID, mockTCName, "", mockUserIds, new ArrayList<>());
		mongoTemplate.save(privateTC);
		Task privateTask = new Task(mockUserIds,privateTCID,mockTeam,mockTime, new ObjectId().toHexString(), mockbody);
		List<Task> validTasks = List.of(mockTask);
		mongoTemplate.save(privateTask);
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
				new Testcase("/task/shared/" + mockUserIds, objectMapper.writeValueAsString(validTasks), HttpStatus.OK.value()),
				new Testcase("/task/shared/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
				new Testcase("/task/shared", "", 405)
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
	void TestTasksScheduledForOneWeekByUser() throws Exception{
		mongoTemplate.save(mockTask);
		LocalDateTime validDate = LocalDateTime.now().plusDays(3);
		Task validTask = new Task(mockUserIds,mockListId,mockTeam,validDate,new ObjectId().toHexString(), mockbody);
		mongoTemplate.save(validTask);
		List<Task> validResponse = List.of(validTask);
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
				new Testcase("/task/scheduled/week/" + mockUserIds, objectMapper.writeValueAsString(validResponse), HttpStatus.OK.value()),
				new Testcase("/task/scheduled/week/" + new ObjectId().toHexString(), "", HttpStatus.NOT_FOUND.value()),
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
	void TestHandleDeleteTask() throws Exception{
		mongoTemplate.save(mockTask);

		class Testcases{
			String taskID;
			Integer statusCode;

			Testcases(String taskID, Integer statusCode){
				this.taskID = taskID;
				this.statusCode = statusCode;
			}
		}

		Testcases[] testcases = new Testcases[]{
				new Testcases(mockObjectId, HttpStatus.ACCEPTED.value()),
				new Testcases(new ObjectId().toHexString(), HttpStatus.NOT_FOUND.value())
		};

		for (Testcases tc: testcases){
			mockMvc.perform(
							delete("/task/" + tc.taskID))
					.andExpect(status().is(tc.statusCode));
		}


	}

	@Test
	void HandleUpdateTask() throws Exception {
		mongoTemplate.save(mockTask);
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
				new Testcases(objectMapper.writeValueAsString(mockTask), mockObjectId, HttpStatusCodes.STATUS_CODE_ACCEPTED),
				new Testcases(objectMapper.writeValueAsString(mockTC), new ObjectId().toHexString(), HttpStatus.NOT_FOUND.value())
		};

		for (Testcases tc: testcases){
			mockMvc.perform(
							put("/task/" + tc.tcID)
									.contentType(MediaType.APPLICATION_JSON)
									.content(tc.requestBody))
					.andExpect(status().is(tc.statusCode));
		}
	}




}



