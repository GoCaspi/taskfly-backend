package com.gocaspi.taskfly.task;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {
	LocalDateTime mockTime = LocalDateTime.now();
	TaskRepository mockRepo = mock(TaskRepository.class);
	TaskService mockService = mock(TaskService.class);
	String mockUserIds = "1";
	String mockListId = "1";
	String mockTeam = "team1";
	ObjectId mockObjectId = new ObjectId();

	Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");

	Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockTime,mockObjectId,mockbody);
	Task[] mockTaskArr = new Task[]{mockTask, mockTask};



	@Test
	 void updateTask() {
		TaskController t = new TaskController(mockService);
	//	Task mockUpdate = new Task(mockUserIds, mockListId, mockTopic + "updated", mockTeam + "updated", mockPrio, mockDesc + "updated", mockDeadline, mockObjectId);
		Task mockUpdate = new Task(mockUserIds,mockListId,mockTeam,mockTime,mockObjectId,mockbody);
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

				ResponseEntity<String> expected = new ResponseEntity<>("successfully updated task with id: " + tc.mockId, HttpStatus.ACCEPTED);
				try {
						ResponseEntity<String> actual = t.handleUpdateTask(tc.mockId, tc.updateForTask);
						assertEquals(expected, actual);
				} catch (HttpClientErrorException e) {
					throw new RuntimeException(e);
				}

			}
		}

	@Test
	 void getAllTasksDB() {

		TaskService s = new TaskService(mockRepo);
		TaskController t = new TaskController(s);
		ArrayList<Task> mockList = new ArrayList<>();
		for (Task task : mockTaskArr) {
			mockList.add(task);
		}
		class Testcase {
			final String userId;
			final ArrayList<Task> mockArrayList;

			public Testcase(String userId, ArrayList<Task> mockArrayList) {
				this.userId = userId;
				this.mockArrayList = mockArrayList;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1",  mockList),
				new Testcase("1", null),
		};
		for (Testcase tc : testcases) {
			when(mockRepo.findAll()).thenReturn(tc.mockArrayList);

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
		TaskController t = new TaskController(mockService);

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
		TaskController t = new TaskController(mockService);

		class Testcase {
			final String userId;
			final Task mockTask;

			public Testcase(String userId, Task mockTask) {
				this.userId = userId;
				this.mockTask = mockTask;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1", mockTask),
		};
		for (Testcase tc : testcases) {
			when(mockRepo.insert(tc.mockTask)).thenReturn(mockTask);
			try {
				ResponseEntity<String> expected = new ResponseEntity<>("successfully created task with id: " + tc.mockTask.getId().toHexString(), HttpStatus.ACCEPTED);
				ResponseEntity<String> actual1 = t.handleCreateNewTask(tc.mockTask);
				assertEquals(actual1.getStatusCode(), expected.getStatusCode());
			} catch (HttpClientErrorException e) {
				HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
				assertEquals(e.getClass(), expectedException.getClass());
			}
		}

	}

	@Test
	 void Handle_deleteTask() {
		TaskController t = new TaskController(mockService);

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
     @Test
     void GetTasksByPriority() {
         TaskController t = new TaskController(mockService);
		 List<Task> taskList =  new ArrayList<Task>();
		 taskList.add(mockTask);
         class Testcase {
             final String userId;
             final List<Task> mockTasks;

             public Testcase(String userId, List<Task> mockTasks) {
                 this.userId = userId;
                 this.mockTasks = mockTasks;
             }
         }

         Testcase[] testcases = new Testcase[]{
                 new Testcase("1", taskList),
         };
         for (Testcase tc : testcases) {
             try {
				 when(mockService.getTasksByHighPriorityService(tc.userId)).thenReturn(tc.mockTasks);
                 ResponseEntity<List<Task>> expected = new ResponseEntity<>(tc.mockTasks, HttpStatus.OK);
                 ResponseEntity<List<Task>> actual1 = t.handleGetTaskByUserIDandPriority(tc.userId);
                 assertEquals(actual1.getStatusCode(), expected.getStatusCode());
             } catch (HttpClientErrorException e) {

             }
         }
     }
}



