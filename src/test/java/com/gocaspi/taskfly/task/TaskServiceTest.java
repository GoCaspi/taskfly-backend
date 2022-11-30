package com.gocaspi.taskfly.task;


import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class TaskServiceTest {

	TaskRepository mockRepo = mock(TaskRepository.class);

	String mockUserIds = "123";
	String mockListId = "1";
	String mockTeam = "team1";
	String mockDeadline = "11-11-2022";
	ObjectId mockObjectId = new ObjectId();
Task.Taskbody mockbody = new Task.Taskbody("mockTopic","mockPrio","mockDescription");
	Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockDeadline,mockObjectId,mockbody);
	@Test
	void getService_AllTasksOfUser() {

		TaskService t = new TaskService(mockRepo);
		Task[] mockTaskArr = new Task[]{mockTask, mockTask};
		ArrayList<Task> mockList = new ArrayList<>();
		for (Task task : mockTaskArr) { mockList.add(task); }

		class Testcase {
			final String id;
			final List<Task> dbReturn;
			final List<Task> expected;

			public Testcase(String id, List<Task> dbReturn, List<Task> expected) {
				this.id = id;
				this.dbReturn = dbReturn;
				this.expected = expected;
			}
		}
		Testcase[] testcases = new Testcase[]{
				new Testcase("123", mockList,mockList),
				new Testcase("1",new ArrayList<>(),new ArrayList<>())
		};
		for(Testcase tc : testcases){
			when(mockRepo.findAll()).thenReturn(tc.dbReturn);
			List actual = t.getServiceAllTasksOfUser(tc.id);
			assertEquals(tc.expected, actual);
		}
	}
	@Test
	void validateTaskFields() {
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
				new Testcase(new Task("test","test","test","test",new ObjectId(),mockbody),true),
				new Testcase(new Task("","","","",new ObjectId(),mockbody),true)
		};

		for (Testcase tc : testcases) {
			boolean actualOut = t.validateTaskFields(new Gson().toJson(tc.taskInput));
			assertEquals(tc.expected, actualOut);
		}
	}
	@Test
	void updateService(){
		TaskService service = new TaskService(mockRepo);
		Task emptyTeam = new Task(null, null, null, null, mockObjectId, null);
		class Testcase{
			final String mockId;
			final Task mockUpdate;
			final boolean expected;
			public Testcase(String mockId, Task mockUpdate, boolean expected){
				this.mockId = mockId;
				this.mockUpdate = mockUpdate;
				this.expected = expected;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1",mockTask, true),
				new Testcase("1", mockTask, false),
				new Testcase("1", emptyTeam, true),
		};

		for (Testcase tc : testcases) {
			try{
				Optional<Task> optionalTask = Optional.ofNullable(tc.mockUpdate);
				when(mockRepo.findById(tc.mockId)).thenReturn(optionalTask);
				when(mockRepo.existsById(tc.mockId)).thenReturn(tc.expected);
				service.updateService(tc.mockId, tc.mockUpdate);
				verify(mockRepo, times(1)).save(tc.mockUpdate);
			} catch (Exception e){

			}
		}
	}
	@Test
	void getServiceTaskById(){
		TaskService service = new TaskService(mockRepo);
		class Testcase{
			final String mockId;
			final boolean expected;
			final Task task;
			public Testcase(String mockId, boolean expected, Task task){
				this.mockId = mockId;
				this.expected = expected;
				this.task = task;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1",true, mockTask),
				new Testcase("1", true, null),
				new Testcase("", false, null)
		};

		for (Testcase tc : testcases) {
			try{
				when(mockRepo.existsById(tc.mockId)).thenReturn(tc.expected);
				Optional<Task> optionalTask = Optional.ofNullable(tc.task);
				when(mockRepo.findById(tc.mockId)).thenReturn(optionalTask);
				service.getServiceTaskById(tc.mockId);
				verify(mockRepo, times(1)).findById(tc.mockId);
			} catch (Exception e){

			}
		}
	}
	@Test
	void deleteTask(){

		TaskService service = new TaskService(mockRepo);
		class Testcase{
			final String mockId;
			final boolean expected;

			public Testcase(String mockId, boolean expected){
				this.mockId = mockId;
				this.expected = expected;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase("1", true),
				new Testcase("", false),
		};

		for (Testcase tc : testcases) {
			try {
				when(mockRepo.existsById(tc.mockId)).thenReturn(tc.expected);
				service.deleteService(tc.mockId);
				verify(mockRepo, times(1)).deleteById(tc.mockId);
			} catch (Exception e){

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

