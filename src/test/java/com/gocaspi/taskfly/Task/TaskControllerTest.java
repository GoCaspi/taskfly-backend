package com.gocaspi.taskfly.Task;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskControllerTest {
	TaskRepository mockRepo = mock(TaskRepository.class);
	String[] mockUserIds = new String[]{ "1", "2", "3" };
	String mockListId = "1";
	String mockTopic = "topic1";
	String mockTeam = "team1";
	String mockPrio = "prio1";
	String mockDesc = "desc1";
	String mockDeadline = "11-11-2022";
	ObjectId mockObjectId = new ObjectId();
	Task mockTask = new Task(mockUserIds, mockListId, mockTopic, mockTeam, mockPrio, mockDesc, mockDeadline, mockObjectId);
	Task[] mockTaskArr = new Task[]{ mockTask, mockTask };



	@Test
	public void updateTask() {
		TaskController t = new TaskController(mockRepo); // TODO Replace default value.
		Task mockUpdate = new Task(mockUserIds, mockListId, mockTopic + "updated", mockTeam + "updated", mockPrio, mockDesc + "updated", mockDeadline, mockObjectId);

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

		Testcase[] testcases = new Testcase[] { 
		  new Testcase("123", true, mockTask, mockUpdate, true) };

		for (Testcase tc : testcases) {
			when(mockRepo.existsById(tc.mockId)).thenReturn(tc.idFoundInDb);
			if (tc.idFoundInDb) {
				when(mockRepo.findById(tc.mockId)).thenReturn(Optional.ofNullable(tc.taskFromDb));
			} else 
			    {
				when(mockRepo.findById(tc.mockId)).thenReturn(null);
			}

			if (tc.expectSuccess) {
				String successMsg = "task was updated";
				String actualOut = t.Handle_updateTask(tc.mockId, new Gson().toJson(tc.updateForTask));
				assertEquals(successMsg, actualOut);
			} else 
			     {
				String notFoundMsg = "could not find matching Task to the provided id";
				String actualOut = t.Handle_updateTask(tc.mockId, new Gson().toJson(tc.updateForTask));
				assertEquals(notFoundMsg, actualOut);
			}
		}
	}


	@Test
	public void validateTaskFields() {
		TaskController t = new TaskController(mockRepo); // TODO Replace default value.
		class Testcase {
			final Task taskInput;
			final boolean expected;

			public Testcase(Task testTask, boolean expected) {
				this.taskInput = testTask;
				this.expected = expected;
			}
		}

		Testcase[] testcases = new Testcase[] { 
		  new Testcase(mockTask, true) };

		for (Testcase tc : testcases) {
			boolean actualOut = t.validateTaskFields(new Gson().toJson(tc.taskInput));
			assertEquals(tc.expected, actualOut);
		}
	}

	@Test
	public void getAllTasksDB() {
		TaskController t =  new TaskController(mockRepo); // TODO Replace default value.
		String id = "123"; // TODO Replace default value.
		ArrayList<Task> mockList = new ArrayList<Task>();
		for(Task task: mockTaskArr){
			mockList.add(task);
		}
		class Testcase {
			final String userId;
			final boolean dbReturnSize0;
			final ArrayList<Task> mockArrayList;
			final  String expectedOutput;

		public 	Testcase(String userId, boolean dbReturnSize0, ArrayList<Task> mockArrayList, String expectedOutput) {
				this.userId = userId;
				this.dbReturnSize0 = dbReturnSize0;
				this.mockArrayList = mockArrayList;
				this.expectedOutput = expectedOutput;
		}
		}

		Testcase[] testcases = new Testcase[]{
			new Testcase("123",false, mockList, new Gson().toJson(mockList)),
				new Testcase("123",true, new ArrayList<Task>(), "no tasks were found to the provided id"),
				new Testcase(null,true, new ArrayList<Task>(), "no tasks were found to the provided id"),
				new Testcase("",true, new ArrayList<Task>(), "no tasks were found to the provided id"),
				new Testcase(null,false, new ArrayList<Task>(), "no tasks were found to the provided id")
		};
		for (Testcase tc : testcases){
			if (tc.dbReturnSize0){
				when(mockRepo.getAllTasksById(id)).thenReturn(tc.mockArrayList);
			}
			else {
				when(mockRepo.getAllTasksById(id)).thenReturn(tc.mockArrayList);
			}

			String actual1 = t.Handle_getAllTasks(tc.userId);
			assertEquals(actual1,tc.expectedOutput);
		}

	}

}
