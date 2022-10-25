package com.gocaspi.taskfly.Task;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class TaskControllerTest {

	@Mock
	private TaskRepository repository;

	@InjectMocks
	@Resource
	private TaskController testedController;

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
		TaskController t = new TaskController(mockRepo); // TODO Replace default value.

		String id = "1"; // TODO Replace default value.
		Task[] expected = mockTaskArr;  // TODO Replace default value.; // TODO Replace default value.
		String expectedOutput = new Gson().toJson(expected);
		String actual = t.getAllTasks(id);

		assertEquals(expectedOutput, actual);
	}

	@Test
	public void RemoveNullElements() {
		TaskController t = new TaskController(mockRepo); // TODO Replace default value.

		Task[] arr = mockTaskArr; // TODO Replace default value.
		Task[] expected = mockTaskArr; // TODO Replace default value.
		String expectedOutput = new Gson().toJson(expected);
		Task[] actual = t.RemoveNullElements(arr);
		String actualString = new Gson().toJson(actual);

		assertEquals(expectedOutput, actualString);
	}

}
