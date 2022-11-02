package com.gocaspi.taskfly.Task;

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
	TaskService mockService = mock(TaskService.class);
	HttpClientErrorException er = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no tasks are assigned to the provided userId", null, null, null);
	String mockUserIds = "123";
	String mockListId = "1";
	String mockTopic = "topic1";
	String mockTeam = "team1";
	String mockPrio = "prio1";
	String mockDesc = "desc1";
	String mockDeadline = "11-11-2022";
	ObjectId mockObjectId = new ObjectId();
	Task mockTask = new Task(mockUserIds, mockListId, mockTopic, mockTeam, mockPrio, mockDesc, mockDeadline, mockObjectId);


	TaskService ts = new TaskService(mockRepo);
	@Test
	public void postService() throws HttpClientErrorException {
		TaskService t2 = mockService; // TODO Replace default value.
		Task t = mockTask; // TODO Replace default value.
		ts.postService(t);
		verify(t2, times(1));
	}

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
			List actual = t.getService_AllTasksOfUser(tc.id);
			assertEquals(tc.expected, actual);
		}
	}
}
