package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.task.TaskRepository;
import com.gocaspi.taskfly.task.TaskService;
import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResetControllerTest {
	UserRepository mockRepo = mock(UserRepository.class);
	ResetService mockService = mock(ResetService.class);
	Reset mockReset = new Reset("lName","abc@mail.to");
	Reset mockResetEmptyLName = new Reset("","abc@mail.to");
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
		ArrayList<User> mockList = new ArrayList<>();
		mockList.add(new User("fName","lName","abc@mail.to","123","red","1","123",false));
		ArrayList<User> mockList1= new ArrayList<>();
		mockList1.add(new User("fName","lName","abc@mail.to","123","red","1","123",false));
		ResetController r = new ResetController(mockRepo); // TODO Replace default value.
		String body = new Gson().toJson(mockReset); // TODO Replace default value.
		ResponseEntity expected = null; // TODO Replace default value.
		ResponseEntity actual = r.handleReset(body);

	//	assertEquals(expected, actual);


		class Testcase{
			private List dbReturn;
			private Boolean emptyLastName;
			private Integer expectedCode;

			Testcase(List dbReturn,Boolean emptyLastName,Integer expectedCode){
				this.dbReturn = dbReturn;
				this.emptyLastName = emptyLastName;
				this.expectedCode = expectedCode;
			}
		}
		Testcase[] testcases = new Testcase[]{
				new Testcase(mockList,true,400),
				new Testcase(mockList,false,404),
				new Testcase(mockList,false,200),
		};
		for(Testcase tc: testcases){
			if(tc.expectedCode == 400){
				ResponseEntity actual1 = r.handleReset(new Gson().toJson(mockResetEmptyLName));
				assertEquals(actual1.getStatusCode(), HttpStatus.BAD_REQUEST);
			}
			if (tc.expectedCode==404){
				mockList.add(new User("fName","lName","abc@mail.to","123","red","1","123",false));
				when(mockRepo.findUserByEmail(mockReset.hashStr(mockReset.getEmail()))).thenReturn(mockList);
	//			when(mockService.getUserByEmail(mockReset.hashStr(mockReset.getEmail()),mockReset.getLastName())).thenReturn(mockList);

				ResponseEntity actual1 = r.handleReset(new Gson().toJson(mockReset));
				assertEquals(actual1.getStatusCode(), HttpStatus.NOT_FOUND);
			}

			if(tc.expectedCode == 200){
				mockList.add(new User("fName","lName","abc@mail.to","123","red","1","123",false));
				when(mockRepo.findUserByEmail(mockReset.hashStr(mockReset.getEmail()))).thenReturn(mockList1);
				when(mockRepo.findById(null)).thenReturn(Optional.of(new User("fName", "lName", "abc@mail.to", "123", "red", "1", "123", false)));
				when(mockRepo.existsById(null)).thenReturn(true);
				//			when(mockService.getUserByEmail(mockReset.hashStr(mockReset.getEmail()),mockReset.getLastName())).thenReturn(mockList);

				ResponseEntity actual1 = r.handleReset(new Gson().toJson(mockReset));
				assertEquals(actual1.getStatusCode(), HttpStatus.ACCEPTED);
			}
		}
	}


}
