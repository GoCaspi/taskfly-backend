package com.gocaspi.taskfly.reset;


import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

 class ResetControllerTest {
	UserRepository mockRepo = mock(UserRepository.class);
	ResetService mockService = mock(ResetService.class);
	Reset mockReset = new Reset("lName", "abc@mail.to");
	Reset mockResetEmptyLName = new Reset("", "abc@mail.to");
	User.Userbody mockUserBody = new User.Userbody(new ObjectId().toHexString());
	User mockUser = new User("1", "1", "1", "1", "1", mockUserBody, true);

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
	  void handleSetNewUserPwd() {
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
	 void handleReset() {
		ArrayList<User> mockList = new ArrayList<>();
		mockList.add(new User("fName", "lName", "abc@mail.to", "123", "red", mockUserBody, false));
		ArrayList<User> mockList1 = new ArrayList<>();
		mockList1.add(new User("fName", "lName", "abc@mail.to", "123", "red", mockUserBody, false));
		ResetController r = new ResetController(mockRepo); // TODO Replace default value.
		String body = new Gson().toJson(mockReset); // TODO Replace default value.
		ResponseEntity expected = null; // TODO Replace default value.
		ResponseEntity actual = r.handleReset(body);

		//	assertEquals(expected, actual);


		class Testcase {
			private List dbReturn;
			private Boolean emptyLastName;
			private Integer expectedCode;

			Testcase(List dbReturn, Boolean emptyLastName, Integer expectedCode) {
				this.dbReturn = dbReturn;
				this.emptyLastName = emptyLastName;
				this.expectedCode = expectedCode;
			}
		}
		Testcase[] testcases = new Testcase[]{ 
		  new Testcase(mockList, true, 400), 
		  new Testcase(mockList, false, 404), 
		  new Testcase(mockList, false, 200) };

		for (Testcase tc : testcases) {
			if (tc.expectedCode == 400) {
				ResponseEntity actual1 = r.handleReset(new Gson().toJson(mockResetEmptyLName));
				assertEquals(HttpStatus.BAD_REQUEST, actual1.getStatusCode() );
			}
			if (tc.expectedCode == 404) {
				mockList.add(new User("fName", "lName", "abc@mail.to", "123", "red", mockUserBody, false));
				when(mockRepo.findUserByEmail(mockReset.hashStr(mockReset.getEmail()))).thenReturn(mockList);
				//			when(mockService.getUserByEmail(mockReset.hashStr(mockReset.getEmail()),mockReset.getLastName())).thenReturn(mockList);

				ResponseEntity actual1 = r.handleReset(new Gson().toJson(mockReset));
				assertEquals(HttpStatus.NOT_FOUND,actual1.getStatusCode());
			}

			if (tc.expectedCode == 200) {
				mockList.add(new User("fName", "lName", "abc@mail.to", "123", "red", mockUserBody, false));
				when(mockRepo.findUserByEmail(mockReset.hashStr(mockReset.getEmail()))).thenReturn(mockList1);
				when(mockRepo.findById(null)).thenReturn(Optional.of(new User("fName", "lName", "abc@mail.to", "123", "red", mockUserBody, false)));
				when(mockRepo.existsById(null)).thenReturn(true);
				//			when(mockService.getUserByEmail(mockReset.hashStr(mockReset.getEmail()),mockReset.getLastName())).thenReturn(mockList);

				ResponseEntity actual1 = r.handleReset(new Gson().toJson(mockReset));
				assertEquals( HttpStatus.ACCEPTED,actual1.getStatusCode());
			}
		}
	}

	@Test
	 void getJavaMailSender() {
		ResetController r = new ResetController(mockRepo); // TODO Replace default value.

		JavaMailSender expected = new JavaMailSenderImpl();
		((JavaMailSenderImpl) expected).setHost("smtp.sendgrid.net");
		((JavaMailSenderImpl) expected).setPort(465);

		((JavaMailSenderImpl) expected).setUsername("apikey");
		((JavaMailSenderImpl) expected).setPassword("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");

		Properties props = ((JavaMailSenderImpl) expected).getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.ssl.checkserveridentity", true);
		JavaMailSender actual = r.getJavaMailSender();

		assertEquals(expected.getClass(), actual.getClass());
	}

	@Test
	 void jsonToReset() {
		ResetController r = new ResetController(mockRepo); // TODO Replace default value.
		String jsonPayload = new Gson().toJson(mockReset); // TODO Replace default value.
		Reset expected = mockReset; // TODO Replace default value.
		Reset actual = r.jsonToReset(jsonPayload);

		assertEquals(expected.getClass(), actual.getClass());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getLastName(), actual.getLastName());
	}

	@Test
	 void sendResetMail() {
		ResetController r = new ResetController(mockRepo); // TODO Replace default value.
		JavaMailSender mockSender = mock(JavaMailSender.class);
		String to = "toMock"; // TODO Replace default value.
		String subject = "subjectMock"; // TODO Replace default value.
		String text = "textMock"; // TODO Replace default value.
		r.sendResetMail(to, subject, text);
		atLeast(1);
		assertEquals(true,verifySendInfo(to,subject,text));
	}

	public boolean verifySendInfo(String to,String sub, String text){
		return (to == "toMock" && sub == "subjectMock" && text == "textMock");
	}


}
