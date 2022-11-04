package com.gocaspi.taskfly.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class TaskTest {
	String mockUserIds = "1"; String mockListId = ""; String mockTopic = ""; String mockTeam = "";
	String mockPrio = ""; String mockDesc = ""; String mockDeadline = ""; ObjectId mockObjectId = new ObjectId();
	Task mockTask = new Task(mockUserIds, mockListId, mockTopic, mockTeam, mockPrio, mockDesc, mockDeadline, mockObjectId);


	class Testcase_setString{
		final String newText;

		Testcase_setString(String newText) {
			this.newText = newText;
		}
	}

	Testcase_setString[] testcases = new Testcase_setString[]{
			new Testcase_setString("abc"),
			new Testcase_setString(null),
			new Testcase_setString(""),
	};

	class Testcase_getString{
		final String expected;

		Testcase_getString(String expected) {
			this.expected = expected;
		}
	}

	Testcase_getString[] testcases_get = new Testcase_getString[]{
			new Testcase_getString("abc"),
			new Testcase_getString(null),
			new Testcase_getString(""),
	};

	@Test
	public void setDescription() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setDescription(tc.newText);
			assertEquals(t.getDescription(),tc.newText);
		}
	}

	@Test
	public void setUserId() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setUserId(tc.newText);
			assertEquals(t.getUserId(),tc.newText);
		}
	}

	@Test
	public void setTeam() {
		Task t = mockTask; 
		for (Testcase_setString tc : testcases){
			t.setTeam(tc.newText);
			assertEquals(t.getTeam(),tc.newText);
		}
	}

	@Test
	public void setPriority() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setPriority(tc.newText);
			assertEquals(t.getPriority(),tc.newText);
		}
	}

	@Test
	public void setTopic() {
		Task t = mockTask; 
		for (Testcase_setString tc : testcases){
			t.setTopic(tc.newText);
			assertEquals(t.getTopic(),tc.newText);
		}
	}

	@Test
	public void setDeadline() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setDeadline(tc.newText);
			assertEquals(t.getDeadline(),tc.newText);
		}
	}
	@Test
	public void getDescription(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setDescription(tc.expected);
			String actual = t.getDescription();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	public void getDeadline(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setDeadline(tc.expected);
			String actual = t.getDeadline();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	public void getPriority(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setPriority(tc.expected);
			String actual = t.getPriority();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	public void getTeam(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setTeam(tc.expected);
			String actual = t.getTeam();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	public void getTopic(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setTopic(tc.expected);
			String actual = t.getTopic();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	public void getTaskIdString(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setTopic(tc.expected);
			String actual = t.getTaskIdString();
			assertEquals(actual,t.getTaskIdString());
		}
	}

	@Test
	public void getListId(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setListId(tc.expected);
			String actual = t.getListId();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	public void setListId() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setListId(tc.newText);
			assertEquals(t.getListId(),tc.newText);
		}
	}
}