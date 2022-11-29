package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ResetControllerTest {
	UserRepository mockRepo = mock(UserRepository.class);
	ResetService mockService = mock(ResetService.class);

	User mockUser = new User("1", "1", "1","1","1","1","1",true);
	public static class uIdAndPwdBody{
		private String pwd;
		private String userId;
		public uIdAndPwdBody(String pwd, String userId){
			this.userId = userId;
			this.pwd = pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}

		public String getPwd() {
			return pwd;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUserId() {
			return userId;
		}
	}

	@Test
	public void handleSetNewUserPwd() {
		/*ResetController r = null; // TODO Replace default value.
		String body = null; // TODO Replace default value.
		ResponseEntity expected = null; // TODO Replace default value.
		ResponseEntity actual = r.handleSetNewUserPwd(body);
		assertEquals(expected, actual);*/


		ResetController resetController = new ResetController(mockRepo);
	    uIdAndPwdBody uIdAndPwdBody = new uIdAndPwdBody("1", "1" );

		class Testcase{
			final String body;
			final boolean payload;
			final String mockUserId;
			final String mockPw;
			final boolean exist;

			public Testcase(String body, boolean payload, String mockUserId, String mockPw, boolean exist){
				this.body = body;
				this.payload = payload;
				this.mockUserId = mockUserId;
				this.mockPw = mockPw;
				this.exist = exist;
			}
		}
		Testcase[] testcases = new Testcase[]{
				new Testcase(new Gson().toJson(uIdAndPwdBody),true, "1", "1", true),
				new Testcase(new Gson().toJson(uIdAndPwdBody),false, "", "", true),
				new Testcase(new Gson().toJson(uIdAndPwdBody),false, null, null, false),
		};

		for (Testcase tc : testcases) {


			try{
				Optional<User> optionalUserUser = Optional.ofNullable(mockUser);
				when(mockRepo.findById(tc.mockUserId)).thenReturn(optionalUserUser);
				when(mockRepo.existsById(tc.mockUserId)).thenReturn(tc.exist);
				when(mockService.resetPwdOfUser(tc.mockUserId, tc.mockPw)).thenReturn(Optional.ofNullable(mockUser));
				resetController.handleSetNewUserPwd(tc.body);
				ResponseEntity expected = new ResponseEntity("healthy", HttpStatus.OK );
				ResponseEntity actual1 = resetController.handleSetNewUserPwd(tc.body);
				assertEquals(actual1.getStatusCode(), expected.getStatusCode());
			} catch (Exception e){

			}
		}
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
