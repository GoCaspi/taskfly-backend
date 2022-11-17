package com.gocaspi.taskfly.task;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TaskServiceTest {

	TaskRepository mockRepo = mock(TaskRepository.class);
	HttpClientErrorException er = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no tasks are assigned to the provided userId", null, null, null);
	String mockUserIds = "123";
	String mockListId = "1";
	String mockTeam = "team1";
	String mockDeadline = "11-11-2022";
	ObjectId mockObjectId = new ObjectId();
Task.Taskbody mockbody = new Task.Taskbody("mockTopic","mockPrio","mockDescription");
	Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockDeadline,mockObjectId,mockbody);


	@Test
	public void getService_AllTasksOfUser() {

		TaskService t = new TaskService(mockRepo);
		Task[] mockTaskArr = new Task[]{mockTask, mockTask};
		ArrayList<Task> mockList = new ArrayList<>();
		for (Task task : mockTaskArr) { mockList.add(task); }

		class Testcase {
			final String id;
			final List<Task> dbReturn;
			final List<Task> expected;

		public	Testcase(String id,  List<Task> dbReturn, List<Task> expected) {
				this.id = id;
			this.dbReturn = dbReturn;
				this.expected = expected;
			}
		}
		Testcase[] testcases = new Testcase[]{
				new Testcase("123", mockList,mockList),
				new Testcase("1",new ArrayList<Task>(),new ArrayList<Task>())
		};
		for(Testcase tc : testcases){
				when(mockRepo.findAll()).thenReturn(tc.dbReturn);
			List actual = t.getServiceAllTasksOfUser(tc.id);
			assertEquals(tc.expected, actual);
		}
	}

	@Test
	public void validateTaskFields() {
		TaskService t = new TaskService(mockRepo);
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
				new Testcase(new Task(null,null,null,null,new ObjectId(),mockbody),false),
				new Testcase(new Task("test","test","test","test",new ObjectId(),mockbody),true)
		};

		for (Testcase tc : testcases) {
			boolean actualOut = t.validateTaskFields(new Gson().toJson(tc.taskInput));
			assertEquals(tc.expected, actualOut);
		}
	}
}

