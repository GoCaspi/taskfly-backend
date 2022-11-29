package com.gocaspi.taskfly.task;

import com.gocaspi.taskfly.taskcollection.TaskCollection;
import com.gocaspi.taskfly.taskcollection.TaskCollectionService;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TaskServiceTest {
	LocalDateTime mockTime = LocalDateTime.now();

	TaskRepository mockRepo = mock(TaskRepository.class);
	HttpClientErrorException er = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no tasks are assigned to the provided userId", null, null, null);
	String mockUserIds = "123";
	String mockListId = "1";
	String mockTeam = "team1";
	ObjectId mockObjectId = new ObjectId();
Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");
	Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockTime,mockObjectId,mockbody);


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
	public void updateTaskService(){
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
	@Test
	public void getTaskByUserIDandPriorityTest(){
		TaskService s = new TaskService(mockRepo);
		List<Task> taskList = new ArrayList<>();
		taskList.add(mockTask);
		class Testcase {
			final List<Task> mockTasks;
			final String mockID;

			public Testcase(List<Task> task, String mockID) {
				this.mockTasks = task;
				this.mockID = mockID;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(taskList, mockObjectId.toHexString()),
				new Testcase(new ArrayList<>(), mockObjectId.toHexString())
		};
		for (Testcase tc : testcases) {
			try {

				when(mockRepo.getTaskByUserIdAndBody_HighPriority(tc.mockID, true)).thenReturn(tc.mockTasks);
				var actual = s.getTasksByHighPriorityService(tc.mockID);
				assertEquals(tc.mockTasks, actual);
			} catch (Exception e) {

			}

		}
	}
}

