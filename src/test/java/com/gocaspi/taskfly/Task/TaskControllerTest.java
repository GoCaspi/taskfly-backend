package com.gocaspi.taskfly.Task;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TaskControllerTest {
	TaskRepository mockRepo = mock(TaskRepository.class);
	String[] mockUserIds = new String[]{ "1", "2", "3" };
	String mockListId = new String("1");
	String mockTopic = new String("topic1");
	String mockTeam = new String("team1");
	String mockPrio = new String("prio1");
	String mockDesc = new String("desc1");
	String mockDeadline = new String("11-11-2022");
	ObjectId mockObjectId = new ObjectId();
	Task mockTask = new Task(mockUserIds, mockListId, mockTopic, mockTeam, mockPrio, mockDesc, mockDeadline, mockObjectId);
	Task[] mockTaskArr = new Task[]{ mockTask, mockTask };



	@Test
	public void getAllTasks() {

		class Testcase {
			String id;
			Task[] mockTasks;
			Task[] expected;
			public Testcase(String id, Task[] mockTasks, Task[] expected) {
				this.id = id;
				this.mockTasks = mockTasks;
				this.expected = expected;
			}
		}


		Testcase[] testcases = new Testcase[] { 
		  new Testcase("1", mockTaskArr, mockTaskArr), 
		  new Testcase("2", mockTaskArr, mockTaskArr), 
		  new Testcase("3", mockTaskArr, mockTaskArr), 
		  new Testcase("4", mockTaskArr, new Task[0]) };


		 for (Testcase tc : testcases) {
			 when(mockRepo.findAll()).thenReturn(List.of(tc.mockTasks));
			 TaskController t = new TaskController(mockRepo);

			 String expectedOut = new Gson().toJson(tc.expected);
			 String actualOut = t.getAllTasks(tc.id);

			 assertEquals(expectedOut, actualOut);
		 }
	}

	@Test
	public void RemoveNullElements() {
		TaskController t = new TaskController(mockRepo);

		Task[] arr = mockTaskArr;
		Task[] expected = mockTaskArr;
		String expectedOutput = new Gson().toJson(expected);
		Task[] actual = t.RemoveNullElements(arr);
		String actualString = new Gson().toJson(actual);

		assertEquals(expectedOutput, actualString);
	}

	@Test
	public void validateTaskFields() {
		TaskController t = new TaskController(mockRepo); // TODO Replace default value.
		class Testcase {
			Task taskInput;
			boolean expected;

			public Testcase(Task testTask, boolean expected){
				this.taskInput = testTask;
				this.expected = expected;
			}
		}

		Testcase[] testcases = new Testcase[] {
				new Testcase(mockTask,true),
			};
		for (Testcase tc : testcases){
			boolean actualOut = t.validateTaskFields(new Gson().toJson(tc.taskInput));
			assertEquals(tc.expected, actualOut);
		}

	}

}
