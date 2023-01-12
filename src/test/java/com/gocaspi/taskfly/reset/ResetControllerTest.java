package com.gocaspi.taskfly.reset;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.internet.MimeMessage;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
 class ResetControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	MongoTemplate mongoTemplate;
	@MockBean
	ResetService mockResetService;
	User.Userbody mockUserBody = new User.Userbody(new ObjectId().toHexString());
	User mockUser = new User("1", "1", "1", "1", "1", mockUserBody, true);

	HttpClientErrorException exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);

	@BeforeEach
	void setup(){
		mockUser.setId("1");
		reset(mockResetService);
	}
	@Test
	void TestHandleSetNewUserPw() throws Exception{
		String validRedisToken = UUID.randomUUID().toString();
		class Testcase {
			ResetNewPassword mockResetNewPassword;
			Integer status;
			User mockUser;
			String redisToken;

			Testcase(ResetNewPassword resetNewPassword, Integer status, User user, String redisToken){
				this.mockResetNewPassword = resetNewPassword;
				this.status = status;
				this.mockUser = user;
				this.redisToken = redisToken;
			}
		}

		Testcase[] testcase = new Testcase[]{
				new Testcase(new ResetNewPassword("password", validRedisToken), HttpStatus.OK.value(), mockUser, validRedisToken),
				new Testcase(new ResetNewPassword("password", validRedisToken), HttpStatus.NOT_FOUND.value(), mockUser, "")
		};
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		for(Testcase tc: testcase){
			if(tc.redisToken != "" && !Objects.isNull(tc.mockUser)) {
				when(mockResetService.resetPwdOfUser(tc.mockResetNewPassword.getToken(), tc.mockResetNewPassword.getPwd())).thenReturn(Optional.of(tc.mockUser));
			} else {
				when(mockResetService.resetPwdOfUser(any(), any())).thenThrow(exceptionNotFound);
			}
			mongoTemplate.save(mockUser);
			mockMvc.perform(post("/reset/setNew")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(tc.mockResetNewPassword)))
					.andExpect(status().is(tc.status));
		}
	}

	@Test
	void TestHandleReset() throws Exception{
		Reset invalidRequest = new Reset("", "");
		class Testcases {
			Reset resetRequest;
			List<User> userList;
			String userID;
			String returnToken;
			Integer status;

			Testcases(Reset resetRequest, List<User> userList, String userID, String returnToken, Integer status){
				this.resetRequest = resetRequest;
				this.userList = userList;
				this.userID = userID;
				this.returnToken = returnToken;
				this.status = status;
			}
		}

		Testcases[] testcases = new Testcases[]{
				new Testcases(new Reset("1", "1"), Arrays.asList(mockUser), mockUser.getId(), UUID.randomUUID().toString(), HttpStatus.ACCEPTED.value()),
				new Testcases(new Reset("1", "1"), Arrays.asList(), mockUser.getId(), UUID.randomUUID().toString(), HttpStatus.NOT_FOUND.value()),
				new Testcases(invalidRequest, Arrays.asList(), mockUser.getId(), UUID.randomUUID().toString(), HttpStatus.BAD_REQUEST.value())

		};
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		for (Testcases tc: testcases){
			when(mockResetService.getUserByEmail(any(), any())).thenReturn(tc.userList);
			if(tc.returnToken != ""){
				when(mockResetService.generateResetUserToken(tc.userID)).thenReturn(tc.returnToken);
			}


			mockMvc.perform(post("/reset")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(tc.resetRequest)))
					.andExpect(
							status().is(tc.status)
					);
		}
	}

	@Test
	void TestCheckTokenValidity() throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		ObjectNode validNode = objectMapper.createObjectNode();
		validNode.put("isValid", true);

		ObjectNode invalidNode = objectMapper.createObjectNode();
		invalidNode.put("isValid", false);

		class Testcase {
			String url;
			Integer status;
			Boolean serviceStatus;
			String responseBody;
			Testcase(String url, Integer status, Boolean serviceStatus, String responseBody){
				this.url = url;
				this.status = status;
				this.serviceStatus = serviceStatus;
				this.responseBody = responseBody;
			}

		}
		Testcase[] testcases = new Testcase[]{
				new Testcase("/reset/valid/123", HttpStatus.OK.value(), true, objectMapper.writeValueAsString(validNode)),
				new Testcase("/reset/valid/456", HttpStatus.OK.value(), false, objectMapper.writeValueAsString(invalidNode)),


		};

		for (Testcase tc: testcases){
			when(mockResetService.checkTokenValidityService(any())).thenReturn(tc.serviceStatus);
			mockMvc.perform(
					get(tc.url)
			)
					.andExpect(status().is(tc.status))
					.andExpect(content().string(tc.responseBody));
		}


	}
}
