package com.gocaspi.taskfly.Task;

import com.google.gson.Gson;
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
	Task mockTask = new Task(mockUserIds, mockListId, mockTopic, mockTeam, mockPrio, mockDesc, mockDeadline);
	Task[] mockTaskArr = new Task[]{ mockTask, mockTask };


	@Test
	public void getAllTasks() {
		when(mockRepo.findAll()).thenReturn(List.of(mockTaskArr));
		TaskController t = new TaskController(mockRepo); 

		String id = "1";
		Task[] expected = mockTaskArr;
		String expectedOutput = new Gson().toJson(expected);
		String actual = t.getAllTasks(id);

		assertEquals(expectedOutput, actual);
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

}
