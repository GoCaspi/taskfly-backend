package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.task.TaskRepository;
import com.gocaspi.taskfly.task.TaskService;
import com.gocaspi.taskfly.user.UserRepository;
import org.junit.*;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ResetControllerTest {
	UserRepository mockRepo = mock(UserRepository.class);
	ResetService mockService = mock(ResetService.class);
	@Test
	public void handleSetNewUserPwd() {
		ResetController r = null; // TODO Replace default value.
		String body = null; // TODO Replace default value.
		ResponseEntity expected = null; // TODO Replace default value.
		ResponseEntity actual = r.handleSetNewUserPwd(body);

		assertEquals(expected, actual);
	}

	@Test
	public void handleReset() {
		ResetController r = null; // TODO Replace default value.
		String body = null; // TODO Replace default value.
		ResponseEntity expected = null; // TODO Replace default value.
		ResponseEntity actual = r.handleReset(body);

		assertEquals(expected, actual);
	}
}
