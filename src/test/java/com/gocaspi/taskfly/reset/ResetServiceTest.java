package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.common.hash.Hashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.*;

import org.bson.types.ObjectId;


import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResetServiceTest {
	 @Mock
	UserRepository repo;
	@Mock
	RedisTemplate<String, String> redisTemplate;
	@Mock
	ValueOperations valueOperations;
	@Mock
	JavaMailSender mockMailSender;
	Reset reset = new Reset("lName","fName@mail.to");
	@InjectMocks
	ResetService resetService;
	User.Userbody mockUserBody = new User.Userbody(new ObjectId().toHexString());
	User mockUser = new User("fName", "lName", "fName@mail.to", "admin123", "red", mockUserBody, false);
	List <User> mockList = new ArrayList<>();





	@Test
	 void getUserByEmail() {
		ResetService r = resetService; // TODO Replace default value.
		mockUser.setId("1");
		mockList.add(mockUser);

		class Testcase {
			final List<User> dbReturn;
			final List<User> expected;
			final Boolean notFound;
			final Boolean badRequest;

			public	Testcase(List<User> dbReturn, List<User> expected, Boolean notFound,Boolean badRequest) {
				this.dbReturn = dbReturn;
				this.expected = expected;
				this.notFound = notFound;
				this.badRequest = badRequest;
			}
		}
		Testcase[] testcases = new Testcase[]{
			new Testcase(mockList,mockList,false,false),
				new Testcase(Arrays.asList(),Arrays.asList(),true,false),

				new Testcase(mockList,mockList,true,false),
				new Testcase(mockList,mockList,false,true)
		};

		for(Testcase tc: testcases){

			if(!tc.badRequest & !tc.notFound){
				String mockHashMail = hashStr(reset.getEmail());
				when(repo.findUserByEmail(mockHashMail)).thenReturn(tc.dbReturn);
				when(repo.findById(any())).thenReturn(Optional.ofNullable(mockUser));
				List<User> actual = resetService.getUserByEmail(mockHashMail,reset.getLastName());
				assertEquals(actual,tc.expected);
			}
			if(tc.notFound){
				String mockHashMail = hashStr(reset.getEmail());
				when(repo.findUserByEmail(mockHashMail)).thenReturn(tc.dbReturn);

				try {
					List<User> actual = resetService.getUserByEmail(mockHashMail,reset.getLastName());
					assertEquals(actual,tc.expected);
				}
				catch (HttpClientErrorException e){
					HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
					assertEquals(e.getClass(), expectedException.getClass());
				}
			}

			if(tc.badRequest){

				String mockHashMail = hashStr(reset.getEmail());
				mockList.get(0).setLastName("changed");
				when(repo.findUserByEmail(mockHashMail)).thenReturn(mockList);

				try {
					List<User> actual = resetService.getUserByEmail(mockHashMail,reset.getLastName());
					assertEquals(actual,tc.expected);
				}
				catch (HttpClientErrorException e){
					HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "bad payload", null, null, null);
					assertEquals(e.getClass(), expectedException.getClass());
				}
			}

		}
	}

	@Test
	 void resetPwd_Test(){
		class Testcase{
			private final User dbReturn;
			private final String UserId;
			private final String token;
			private final boolean notFound;
			private final Optional<User> expected;

			public Testcase(User dbReturn, String userId, String token, Boolean notFound, Optional<User>expected){
				this.dbReturn = dbReturn;
				this.UserId = userId;
				this.token = token;
				this.notFound = notFound;
				this.expected = expected;
			}
		}
		mockUser.setId(new ObjectId().toHexString());
		Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		Testcase[] testcases = new Testcase[]{
				new Testcase(mockUser, mockUser.getId(), UUID.randomUUID().toString(), false, Optional.ofNullable(mockUser)),
				new Testcase(mockUser, mockUser.getId(), UUID.randomUUID().toString(), true, Optional.ofNullable(mockUser)),
		};
		for(Testcase tc : testcases){
			Mockito.reset(repo);
			if(tc.notFound){
				try {
					resetService.resetPwdOfUser(tc.dbReturn.getId(),"newPwd");
				}
				catch (HttpClientErrorException e){
					HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
					assertEquals(e.getClass(), expectedException.getClass());
				}
			}
			else{
				mockUser.setReseted(true);
				when(repo.findById(tc.dbReturn.getId())).thenReturn(Optional.ofNullable(mockUser));
				when(valueOperations.get(tc.token)).thenReturn(tc.UserId);
				Optional<User> actual = resetService.resetPwdOfUser(tc.token,"newPwd");
				assertEquals(actual,tc.expected);
			}

		}
	}

	@Test
	 void EnablePwdResetOfUser(){
		mockUser.setId("1");
		class Testcase{
			private final User dbReturn;
			private final boolean notFound;
			private final Optional<User> expected;

			public Testcase(User dbReturn, Boolean notFound, Optional<User>expected){
				this.dbReturn = dbReturn;
				this.notFound = notFound;
				this.expected = expected;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(mockUser,false, Optional.ofNullable(mockUser)),
				new Testcase(mockUser,true, Optional.ofNullable(mockUser)),
		};

		for(Testcase tc : testcases){
			if(!tc.notFound){
				when(repo.findById(tc.dbReturn.getId())).thenReturn(Optional.ofNullable(mockUser));
				try {
					 resetService.enablePwdReset(tc.dbReturn.getId(),true);
					resetService.enablePwdReset(tc.dbReturn.getId(),false);
				}
				catch (HttpClientErrorException e){}
			}
			else {
				when(repo.findById(tc.dbReturn.getId())).thenReturn(Optional.ofNullable(null));
				try {
					resetService.enablePwdReset(tc.dbReturn.getId(),true);
				}
				catch (HttpClientErrorException e){
					HttpClientErrorException expectedException = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", null, null, null);
					assertEquals(e.getClass(), expectedException.getClass());
				}
			}
		}

	}


	public String hashStr(String str)  {
		String sha256hex = Hashing.sha256()
				.hashString(str, StandardCharsets.UTF_8)
				.toString();

		return  sha256hex;

	}

	@Test
	void sendResetMail() throws Exception {
		when(mockMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
		String to = "toMock"; // TODO Replace default value.
		String subject = "subjectMock"; // TODO Replace default value.
		String text = "textMock"; // TODO Replace default value.
		resetService.sendResetMail(to, subject, text);
		atLeast(1);
		assertEquals(true,verifySendInfo(to,subject,text));
	}

	@Test
	void TestGenerateResetUserToken(){
		String userId = new ObjectId().toHexString();
		Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		doNothing().when(valueOperations).set(any(), any(), any());
		String actual = resetService.generateResetUserToken(userId);
		assertNotNull(actual);
	}

	@Test
	void TestCheckTokenValidityService(){
		class Testcase {
			String UserID;
			String UUID;
			Boolean result;

			Testcase(String userID, String uuid, Boolean result){
				this.UserID = userID;
				this.UUID = uuid;
				this.result = result;
			}
		}

		Testcase[] testcases = new Testcase[]{
				new Testcase(new ObjectId().toHexString(), UUID.randomUUID().toString(), true),
				new Testcase(null, UUID.randomUUID().toString(), false)
		};

		for (Testcase tc: testcases){
			when(redisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.get(tc.UUID)).thenReturn(tc.UserID);

			Boolean actual = resetService.checkTokenValidityService(tc.UUID);
			assertEquals(tc.result, actual);

		}



	}

	public boolean verifySendInfo(String to,String sub, String text){
		return (to == "toMock" && sub == "subjectMock" && text == "textMock");
	}

}
