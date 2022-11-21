package com.gocaspi.taskfly.task;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

 class TaskControllerTest {
	TaskRepository mockRepo = mock(TaskRepository.class);
	TaskService mockService = mock(TaskService.class);
	String mockUserIds = "1";
	String mockListId = "1";
	String mockTopic = "topic1";
	String mockTeam = "team1";
	String mockPrio = "prio1";
	String mockDesc = "desc1";
	String mockDeadline = "11-11-2022";
	ObjectId mockObjectId = new ObjectId();

	Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");

	Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockDeadline,mockObjectId,mockbody);
//	Task mockTask = new Task(mockUserIds, mockListId, mockTopic, mockTeam, mockPrio, mockDesc, mockDeadline, mockObjectId);
	Task[] mockTaskArr = new Task[]{mockTask, mockTask};



	@Test
	 void updateTask() {
		TaskController t = new TaskController(mockRepo);
	//	Task mockUpdate = new Task(mockUserIds, mockListId, mockTopic + "updated", mockTeam + "updated", mockPrio, mockDesc + "updated", mockDeadline, mockObjectId);
		Task mockUpdate = new Task(mockUserIds,mockListId,mockTeam,mockDeadline,mockObjectId,mockbody);
		class Testcase {
			final String mockId;
			final boolean idFoundInDb;
			final Task taskFromDb;
			final Task updateForTask;
			final boolean expectSuccess;

			public Testcase(String mockId, boolean idFoundInDb, Task taskFromDb, Task updateForTask, boolean expectSuccess) {
				this.mockId = mockId;
				this.idFoundInDb = idFoundInDb;
				this.taskFromDb = taskFromDb;
				this.updateForTask = updateForTask;
				this.expectSuccess = expectSuccess;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("123", true, mockTask, mockUpdate, true),
				new Testcase("123", false, mockTask, mockUpdate, false),
				new Testcase(null, false, mockTask, mockUpdate, false),

		};

		for (Testcase tc : testcases) {
			when(mockRepo.existsById(tc.mockId)).thenReturn(tc.idFoundInDb);
			if (tc.idFoundInDb) {
				when(mockRepo.findById(tc.mockId)).thenReturn(Optional.ofNullable(tc.taskFromDb));
			} else {
				when(mockRepo.findById(tc.mockId)).thenReturn(null);
			}

			if (tc.expectSuccess) {
				ResponseEntity<String> expected = new ResponseEntity<>("successfully updated task with id: " + tc.mockId, HttpStatus.ACCEPTED);
				try {
					ResponseEntity<String> actual = t.handleUpdateTask(tc.mockId, new Gson().toJson(tc.updateForTask));
					assertEquals(expected, actual);
				} catch (HttpClientErrorException e) {
					throw new RuntimeException(e);
				}

			} else {
				try {
					t.handleUpdateTask(tc.mockId, new Gson().toJson(tc.updateForTask));
				} catch (HttpClientErrorException e) {
					HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
					assertEquals(e.getClass(), expectedException.getClass());
				}
			}
		}
	}


	@Test
	 void validateTaskFields() {
		TaskController t = new TaskController(mockRepo);
		class Testcase {
			final Task taskInput;
			final boolean expected;

			public Testcase(Task testTask, boolean expected) {
				this.taskInput = testTask;
				this.expected = expected;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockTask, true),
		new Testcase(new Task(null,null,null,null,new ObjectId(),null),false)
		};

		for (Testcase tc : testcases) {
			boolean actualOut = t.validateTaskFields(new Gson().toJson(tc.taskInput));
			assertEquals(tc.expected, actualOut);
		}
	}


	@Test
	 void getAllTasksDB() {
		TaskController t = new TaskController(mockRepo);
		ArrayList<Task> mockList = new ArrayList<>();
		for (Task task : mockTaskArr) {
			mockList.add(task);
		}
		class Testcase {
			final String userId;
			final boolean dbReturnSize0;
			final ArrayList<Task> mockArrayList;
			final String expectedOutput;

			public Testcase(String userId, boolean dbReturnSize0, ArrayList<Task> mockArrayList, String expectedOutput) {
				this.userId = userId;
				this.dbReturnSize0 = dbReturnSize0;
				this.mockArrayList = mockArrayList;
				this.expectedOutput = expectedOutput;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1", false, mockList, new Gson().toJson(mockList)),
				new Testcase("1", true, new ArrayList<>(), "no tasks were found to the provided id"),
				new Testcase(null, true, new ArrayList<>(), "no tasks were found to the provided id"),
				new Testcase("", true, new ArrayList<>(), "no tasks were found to the provided id"),
				new Testcase(null, false, new ArrayList<>(), "no tasks were found to the provided id")
		};
		for (Testcase tc : testcases) {
			if (tc.dbReturnSize0) {
				when(mockRepo.findAll()).thenReturn(new ArrayList<>());
			} else {
				when(mockRepo.findAll()).thenReturn(tc.mockArrayList);
			}

			try {
				ResponseEntity<List<Task>> expected = new ResponseEntity<>(Arrays.asList(mockTask), HttpStatus.OK);
				ResponseEntity<List<Task>> actual1 = t.handleGetAllTasks(tc.userId);
				assertEquals(actual1.getStatusCode(), expected.getStatusCode());
			} catch (HttpClientErrorException e) {
				HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
				assertEquals(e.getClass(), expectedException.getClass());
			}
		}
	}


	@Test
	 void getAllTasksById() {
		TaskController t = new TaskController(mockRepo);

		class Testcase {
			final String userId;
			final boolean dbReturnSize0;
			final Task mockTask;
			final String expectedOutput;

			public Testcase(String userId, boolean dbReturnSize0, Task mockTask, String expectedOutput) {
				this.userId = userId;
				this.dbReturnSize0 = dbReturnSize0;
				this.mockTask = mockTask;
				this.expectedOutput = expectedOutput;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1", false, mockTask, ""),
				new Testcase("1", true, mockTask, "no tasks were found to the provided id"),
				new Testcase(null, true, mockTask, "no tasks were found to the provided id"),
				new Testcase("", true, mockTask, "no tasks were found to the provided id"),
				new Testcase(null, false, mockTask, "no tasks were found to the provided id")
		};
		for (Testcase tc : testcases) {
			if (tc.dbReturnSize0) {
				when(mockRepo.existsById(tc.userId)).thenReturn(false);
			} else {
				when(mockRepo.existsById(tc.userId)).thenReturn(true);
				when(mockRepo.findById(tc.userId)).thenReturn(Optional.ofNullable(mockTask));
			}

			try {
				ResponseEntity<Task> expected = new ResponseEntity<>(tc.mockTask, HttpStatus.OK);
				ResponseEntity<Task> actual1 = t.handleGetTaskById(tc.userId);
				assertEquals(actual1.getStatusCode(), expected.getStatusCode());
			} catch (HttpClientErrorException e) {
				HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
				assertEquals(e.getClass(), expectedException.getClass());
			}
		}
	}


	@Test
	 void Handle_createNewTask() {
		TaskController t = new TaskController(mockRepo);

		class Testcase {
			final String userId;
			final boolean badPayload;
			final Task mockTask;
			final String mockPayload;

			public Testcase(String userId, boolean badPayload, Task mockTask, String mockPayload) {
				this.userId = userId;
				this.badPayload = badPayload;
				this.mockTask = mockTask;
				this.mockPayload = mockPayload;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1", false, mockTask, new Gson().toJson(mockTask)),
				new Testcase("1", true, mockTask, new Gson().toJson(mockTask)),
				//			new Testcase(null,true, mockTask, "no tasks were found to the provided id"),
				//			new Testcase("",true, mockTask, "no tasks were found to the provided id"),
				//			new Testcase(null,false, mockTask, "no tasks were found to the provided id")
		};
		for (Testcase tc : testcases) {
			if (tc.badPayload) {
				when(mockService.validateTaskFields(tc.mockPayload)).thenReturn(false);
			} else {
				when(mockService.validateTaskFields(tc.mockPayload)).thenReturn(true);
				when(mockRepo.insert(tc.mockTask)).thenReturn(mockTask);
			}

			try {
				ResponseEntity<String> expected = new ResponseEntity<>("successfully created task with id: " + tc.mockTask.getId().toHexString(), HttpStatus.ACCEPTED);
				ResponseEntity<String> actual1 = t.handleCreateNewTask(tc.mockPayload);
				assertEquals(actual1.getStatusCode(), expected.getStatusCode());
			} catch (HttpClientErrorException e) {
				HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
				assertEquals(e.getClass(), expectedException.getClass());
			}
		}

	}

	@Test
	 void Handle_deleteTask() {
		TaskController t = new TaskController(mockRepo);

		class Testcase {
			final String userId;
			final boolean dbReturnSize0;
			final Task mockTask;
			final String expectedOutput;

			public Testcase(String userId, boolean dbReturnSize0, Task mockTask, String expectedOutput) {
				this.userId = userId;
				this.dbReturnSize0 = dbReturnSize0;
				this.mockTask = mockTask;
				this.expectedOutput = expectedOutput;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1", false, mockTask, ""),
				new Testcase("1", true, mockTask, "no tasks were found to the provided id"),
				new Testcase(null, true, mockTask, "no tasks were found to the provided id"),
				new Testcase("", true, mockTask, "no tasks were found to the provided id"),
				new Testcase(null, false, mockTask, "no tasks were found to the provided id")
		};
		for (Testcase tc : testcases) {
			if (tc.dbReturnSize0) {
				when(mockRepo.existsById(tc.userId)).thenReturn(false);
			} else {
				when(mockRepo.existsById(tc.userId)).thenReturn(true);
			//	when(mockRepo.deleteById(tc.userId)).thenReturn(mockTask);
			}

			try {
				ResponseEntity<String> expected = new ResponseEntity<>("successfully deleted task with id: " + tc.userId, HttpStatus.ACCEPTED);
				ResponseEntity<String> actual1 = t.handleDeleteTask(tc.userId);
				assertEquals(actual1.getStatusCode(), expected.getStatusCode());
			} catch (HttpClientErrorException e) {
				HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
				assertEquals(e.getClass(), expectedException.getClass());
			}
		}
	}
}
