package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.common.hash.Hashing;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

 class ResetServiceTest {
	UserRepository repo = mock(UserRepository.class);
	Reset reset = new Reset("lName","fName@mail.to");
	ResetService resetService = new ResetService(repo);
	User.Userbody mockUserBody = new User.Userbody(new ObjectId().toHexString());
	User mockUser = new User("fName", "lName", "fName@mail.to", "admin123", "red", mockUserBody, false);
	List <User> mockList = new ArrayList<>();




	@Test
	 void getUserByEmail() {
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
				new Testcase(mockList,mockList,false,true),
				new Testcase(mockList,mockList,true,true),
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

	@Test
	 void resetPwd_Test(){
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
			if(tc.notFound){
				when(repo.findById(tc.dbReturn.getId())).thenReturn(null);
				when(repo.existsById(tc.dbReturn.getId())).thenReturn(false);
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
				when(repo.existsById(tc.dbReturn.getId())).thenReturn(true);
				Optional<User> actual = resetService.resetPwdOfUser(tc.dbReturn.getId(),"newPwd");
				assertEquals(actual,tc.expected);
			}

		}
	}

	@Test
	 void EnablePwdResetOfUser(){
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
				when(repo.existsById(tc.dbReturn.getId())).thenReturn(true);
				try {
					 resetService.enablePwdReset(tc.dbReturn.getId(),true);
					resetService.enablePwdReset(tc.dbReturn.getId(),false);
				}
				catch (HttpClientErrorException e){}
			}
			else {
				when(repo.findById(tc.dbReturn.getId())).thenReturn(null);
				when(repo.existsById(tc.dbReturn.getId())).thenReturn(false);
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
}
