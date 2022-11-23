package com.gocaspi.taskfly.reseter;

import com.gocaspi.taskfly.task.Task;
import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.gocaspi.taskfly.user.UserService;
import com.google.common.hash.Hashing;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResetServiceTest {
	UserRepository repo = mock(UserRepository.class);
	Reset reset = new Reset("lName","fName@mail.to");
	ResetService resetService = new ResetService(repo);

	User mockUser = new User("fName", "lName", "fName@mail.to", "admin123", "red", "1", "12345",false);
	List <User> mockList = new ArrayList<>();




	@Test
	public void getUserByEmail() {
		ResetService r = resetService; // TODO Replace default value.
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
				new Testcase(mockList,mockList,true,false),
				new Testcase(mockList,mockList,false,true)
		};

		for(Testcase tc: testcases){

			if(!tc.badRequest & !tc.notFound){
				String mockHashMail = hashStr(reset.getEmail());
				when(repo.findUserByEmail(mockHashMail)).thenReturn(tc.dbReturn);
				when(repo.existsById(mockUser.getId())).thenReturn(true);
				List<User> actual = resetService.getUserByEmail(mockHashMail,reset.getLastName());

				assertEquals(actual,tc.expected);
			}
			if(tc.notFound){
				String mockHashMail = hashStr(reset.getEmail());
				when(repo.findUserByEmail(mockHashMail)).thenReturn(tc.dbReturn);
				when(repo.existsById(mockUser.getId())).thenReturn(false);
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
				when(repo.existsById(mockUser.getId())).thenReturn(true);

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


	public String hashStr(String str)  {
		String sha256hex = Hashing.sha256()
				.hashString(str, StandardCharsets.UTF_8)
				.toString();

		return  sha256hex;

	}
}
