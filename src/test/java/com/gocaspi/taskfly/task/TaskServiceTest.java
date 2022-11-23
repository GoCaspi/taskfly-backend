package com.gocaspi.taskfly.task;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@Test
	public void updateTaskService(){
		TaskService s = new TaskService(mockRepo);
		var emptyBody = new Task.Taskbody("","","");
		var emptyTask = new Task("", "", "", "", mockObjectId, emptyBody);
		class Testcase {
			final Task mockTask;
			final String mockID;
			final Boolean exists;

			public Testcase(Task task, String mockID, Boolean exists) {
				this.mockTask = task;
				this.mockID = mockID;
				this.exists = exists;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(emptyTask, mockObjectId.toHexString(), true),
				new Testcase(mockTask, mockObjectId.toHexString(), true),
				new Testcase(mockTask, mockObjectId.toHexString(), false)
		};
		for (Testcase tc : testcases) {
			try {
				Optional<Task> taskCollection = Optional.ofNullable(tc.mockTask);
				when(mockRepo.findById(tc.mockID)).thenReturn(taskCollection);
				when(mockRepo.existsById(tc.mockID)).thenReturn(tc.exists);
				s.updateService(tc.mockID, tc.mockTask);
				if (tc.exists){
					verify(mockRepo, times(1)).save(tc.mockTask);
				}
			} catch (Exception e) {

			}

		}
	}
}

