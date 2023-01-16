package com.gocaspi.taskfly.task;

import com.gocaspi.taskfly.taskcollection.TaskCollection;
import com.gocaspi.taskfly.taskcollection.TaskCollectionService;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TaskServiceTest {
	LocalDateTime mockTime = LocalDateTime.now();

	TaskRepository mockRepo = mock(TaskRepository.class);
	HttpClientErrorException er = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no tasks are assigned to the provided userId", null, null, null);
	String mockUserIds = "123";
	String mockListId = "1";
	String mockTeam = "team1";
	String mockObjectId = new ObjectId().toHexString();
Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");
	Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockTime,mockObjectId,mockbody);


	@Test
	void getServiceAllTasksOfUserTest() {

		TaskService t = new TaskService(mockRepo);
		List<Task> mockList = Arrays.asList(mockTask);
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
				new Testcase("1",new ArrayList<Task>(),new ArrayList<>())
		};
		for(Testcase tc : testcases){
			try{
				when(mockRepo.getTasksByUserId(tc.id)).thenReturn(tc.dbReturn);
				List actual = t.getServiceAllTasksOfUser(tc.id);
				assertEquals(tc.expected, actual);
			} catch (Exception e) {

			}

		}
	}
	@Test
	void getServiceTaskByIdTest() {
		TaskService t = new TaskService(mockRepo);

		class Testcase {
			final String id;
			final Task dbReturn;
			final Task expected;

			public	Testcase(String id,  Task dbReturn, Task expected) {
				this.id = id;
				this.dbReturn = dbReturn;
				this.expected = expected;
			}
		}
		Testcase[] testcases = new Testcase[]{
				new Testcase("123", mockTask,mockTask),
				new Testcase("1",null,null)
		};
		for(Testcase tc : testcases){
			try{
				Optional<Task> task = Optional.ofNullable(tc.dbReturn);
				when(mockRepo.findById(tc.id)).thenReturn(Optional.ofNullable(tc.dbReturn));
				Task actual = t.getServiceTaskById(tc.id);
				assertEquals(tc.expected, actual);
			} catch (Exception e) {

			}

		}
	}
	@Test
	void updateServiceTest(){
		TaskService s = new TaskService(mockRepo);
		var emptyBody = new Task.Taskbody("",null,"");
		var emptyTask = new Task("", "", "", null, mockObjectId, emptyBody);
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
				new Testcase(emptyTask, mockObjectId, true),
				new Testcase(mockTask, mockObjectId, true),
				new Testcase(mockTask, mockObjectId, false)
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
	@Test
	void getTasksByHighPriorityServiceTest(){
		TaskService s = new TaskService(mockRepo);
		List<Task> mockList = Arrays.asList(mockTask);

		class Testcase {
			final List<Task> mockTasks;
			final String mockID;

			public Testcase(List<Task> task, String mockID) {
				this.mockTasks = task;
				this.mockID = mockID;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockList, mockObjectId),
				new Testcase(new ArrayList<>(), mockObjectId)
		};
		for (Testcase tc : testcases) {
			try {

				when(mockRepo.getTaskByUserIdAndBodyHighPriority(tc.mockID, true)).thenReturn(tc.mockTasks);
				var actual = s.getTasksByHighPriorityService(tc.mockID);
				assertEquals(tc.mockTasks, actual);
			} catch (Exception e) {

			}

		}
	}
	@Test
	void getPrivateTasksTest(){
		TaskService s = new TaskService(mockRepo);
		List<Task> mockList = Arrays.asList(mockTask);

		class Testcase {
			final List<Task> mockTasks;
			final String mockID;

			public Testcase(List<Task> task, String mockID) {
				this.mockTasks = task;
				this.mockID = mockID;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockList, mockObjectId),
				new Testcase(new ArrayList<>(), mockObjectId)
		};
		for (Testcase tc : testcases) {
			try {

				when(mockRepo.findPrivateTasksByUserID(tc.mockID)).thenReturn(tc.mockTasks);
				var actual = s.getPrivateTasks(tc.mockID);
				assertEquals(tc.mockTasks, actual);
			} catch (Exception e) {

			}

		}
	}
	@Test
	void getSharedTasksTest(){
		TaskService s = new TaskService(mockRepo);
		List<Task> mockList = Arrays.asList(mockTask);

		class Testcase {
			final List<Task> mockTasks;
			final String mockID;

			public Testcase(List<Task> task, String mockID) {
				this.mockTasks = task;
				this.mockID = mockID;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockList, mockObjectId),
				new Testcase(new ArrayList<>(), mockObjectId)
		};
		for (Testcase tc : testcases) {
			try {

				when(mockRepo.findSharedTasksByUserID(tc.mockID)).thenReturn(tc.mockTasks);
				var actual = s.getSharedTasks(tc.mockID);
				assertEquals(tc.mockTasks, actual);
			} catch (Exception e) {

			}

		}
	}
	@Test
	void deleteTaskTest(){
		TaskService s = new TaskService(mockRepo);
		class Testcase {
			final String mockID;
			final Boolean exists;

			public Testcase(String mockID, Boolean exists) {
				this.mockID = mockID;
				this.exists = exists;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockObjectId, true),
				new Testcase(mockObjectId, false),

		};
		for (Testcase tc : testcases) {
			when(mockRepo.existsById(tc.mockID)).thenReturn(tc.exists);
			try{
				s.deleteService(tc.mockID);
				Mockito.verify(mockRepo, times(1)).deleteById(tc.mockID);
			} catch (Exception e){

			}

		}
	}
	@Test
	void getTasksScheduledForOneWeekTest(){
		TaskService s = new TaskService(mockRepo);
		List<Task> mockList = Arrays.asList(mockTask);
		class Testcase {
			final List<Task> mockTasks;
			final String mockID;

			public Testcase(List<Task> task, String mockID) {
				this.mockTasks = task;
				this.mockID = mockID;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockList, mockObjectId),
				new Testcase(new ArrayList<>(), mockObjectId)
		};
		for (Testcase tc : testcases) {
			try {

				when(mockRepo.findTasksScheduledForOneWeekByUserID(tc.mockID)).thenReturn(tc.mockTasks);
				var actual = s.getTasksScheduledForOneWeek(tc.mockID);
				assertEquals(tc.mockTasks, actual);
			} catch (Exception e) {

			}

		}
	}
	@Test
	void insertTeam(){
		TaskService service = new TaskService(mockRepo);
		Task mockTeamTest = new Task();

		class Testcase{
			final Task mockInsert;
			final boolean expected;

			public Testcase(Task mockInsert, boolean expected){
				this.mockInsert = mockInsert;
				this.expected = expected;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockTask, false),
				new Testcase(mockTeamTest, true)
		};

		for (Testcase tc : testcases) {
			try{
				service.postService(tc.mockInsert);
				verify(mockRepo, times(1)).insert(tc.mockInsert);
			} catch (Exception e){

			}

		}
	}
}

