package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;
import junit.framework.TestCase;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ResetControllerTest {
	UserRepository mockRepo = mock(UserRepository.class);
	ResetService mockService = mock(ResetService.class);

	User mockUser = new User("1", "1", "1", "1", "1", "1", "1", true);

	public static class uIdAndPwdBody {
		private String pwd;
		private String userId;

		public uIdAndPwdBody(String pwd, String userId) {
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
		ResetController resetController = new ResetController(mockRepo);
		uIdAndPwdBody uIdAndPwdBody = new uIdAndPwdBody("1", "1");

		class Testcase {
			final String body;
			final boolean payload;
			final String mockUserId;
			final String mockPw;
			final boolean exist;

			public Testcase(String body, boolean payload, String mockUserId, String mockPw, boolean exist) {
				this.body = body;
				this.payload = payload;
				this.mockUserId = mockUserId;
				this.mockPw = mockPw;
				this.exist = exist;
			}
		}
		Testcase[] testcases = new Testcase[]{
				new Testcase(new Gson().toJson(uIdAndPwdBody), true, "1", "1", true),
				new Testcase(new Gson().toJson(uIdAndPwdBody), false, "", "", true),
				new Testcase(new Gson().toJson(uIdAndPwdBody), false, null, null, false),
		};

		for (Testcase tc : testcases) {


			try {
				Optional<User> optionalUserUser = Optional.ofNullable(mockUser);
				when(mockRepo.findById(tc.mockUserId)).thenReturn(optionalUserUser);
				when(mockRepo.existsById(tc.mockUserId)).thenReturn(tc.exist);
				when(mockService.resetPwdOfUser(tc.mockUserId, tc.mockPw)).thenReturn(Optional.ofNullable(mockUser));
				resetController.handleSetNewUserPwd(tc.body);
				ResponseEntity expected = new ResponseEntity("healthy", HttpStatus.OK);
				ResponseEntity actual1 = resetController.handleSetNewUserPwd(tc.body);
				assertEquals(actual1.getStatusCode(), expected.getStatusCode());
			} catch (Exception e) {

			}
		}
	}

	@Test
	public void sendResetMail() {
		ResetController resetController = new ResetController(mockRepo);
		SimpleMailMessage mockMessage = new SimpleMailMessage();
		class Tesetcase {
			final String mockTo, mockSubject, mockText;

			public Tesetcase(String mockTo, String mockSubject, String mockText) {
				this.mockTo = mockTo;
				this.mockSubject = mockSubject;
				this.mockText = mockText;
			}
		}

		Tesetcase[] testcases = new Tesetcase[]{
				new Tesetcase("1", "1", "1")
		};

		for (Tesetcase tc : testcases) {
			resetController.sendResetMail(tc.mockTo, tc.mockSubject, tc.mockText);
			verify(resetController, times(1)).getJavaMailSender();
		}
	}
	@Test
	public void sendSimpleEmailWithCC() {
		// Runs a Dumbster simple SMTP server - default config
		SimpleSmtpServer server = SimpleSmtpServer.start();
		String from = "whoever@from.com";
		String to = "whoever@to.com";
		String messageText = "Good message";
		String title = "Test message";
		String cc = "whoever@cc.com";
		Assert.assertTrue(mailSender.sendEmail(from, to, cc, title, messageText));
		server.stop();
		Assert.assertTrue(server.getReceivedEmailSize() == 1);
		Iterator emailIter = server.getReceivedEmail();
		SmtpMessage email = (SmtpMessage) emailIter.next();
		Assert.assertTrue(email.getHeaderValue("From").equals(from));
		Assert.assertTrue(email.getHeaderValue("To").equals(to));
		Assert.assertTrue(email.getHeaderValue("Cc").equals(cc));
		Assert.assertTrue(email.getHeaderValue("Subject")
				.equals("Test message"));
		Assert.assertTrue(email.getBody().equals(messageText));
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
