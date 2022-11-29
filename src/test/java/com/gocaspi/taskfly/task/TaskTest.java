package com.gocaspi.taskfly.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class TaskTest {
	LocalDateTime mockTime = LocalDateTime.now();
	String mockUserIds = "1"; String mockListId = ""; String mockTopic = ""; String mockTeam = "";
	String mockPrio = ""; String mockDesc = ""; String mockDeadline = ""; ObjectId mockObjectId = new ObjectId();
//	Task mockTask = new Task(mockUserIds, mockListId, mockTopic, mockTeam, mockPrio, mockDesc, mockDeadline, mockObjectId);
Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");
Task mockTask = new Task(mockUserIds,mockListId,mockTeam,mockTime,mockObjectId,mockbody);

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
	 void setDescription() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.getBody().setDescription(tc.newText);
			assertEquals(t.getBody().getDescription(),tc.newText);
		}
	}

	@Test
	 void setUserId() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setUserId(tc.newText);
			assertEquals(t.getUserId(),tc.newText);
		}
	}

	@Test
	 void setTeam() {
		Task t = mockTask; 
		for (Testcase_setString tc : testcases){
			t.setTeam(tc.newText);
			assertEquals(t.getTeam(),tc.newText);
		}
	}

	@Test
	 void setHighPriority() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.getBody().setHighPriority(false);
			assertEquals(false, t.getBody().getHighPriority());
		}
	}

	@Test
	 void setTopic() {
		Task t = mockTask; 
		for (Testcase_setString tc : testcases){
			t.getBody().setTopic(tc.newText);
			assertEquals(t.getBody().getTopic(),tc.newText);
		}
	}

	@Test
	 void setDeadline() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			LocalDateTime time =LocalDateTime.now();
			t.setDeadline(time);
			assertEquals(t.getDeadline(),time);
		}
	}
	@Test
	 void getDescription(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.getBody().setDescription(tc.expected);
			String actual = t.getBody().getDescription();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	 void getDeadline(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			LocalDateTime time =LocalDateTime.now();
			t.setDeadline(time);
			LocalDateTime actual = t.getDeadline();
			assertEquals(actual,time);
		}
	}

	@Test
	 void getPriority(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.getBody().setHighPriority(true);
			Boolean actual = t.getBody().getHighPriority();
			assertEquals(true, actual);
		}
	}

	@Test
	 void getTeam(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setTeam(tc.expected);
			String actual = t.getTeam();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	 void getTopic(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.getBody().setTopic(tc.expected);
			String actual = t.getBody().getTopic();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	 void getListId(){
		Task t = mockTask;
		for (Testcase_getString tc : testcases_get){
			t.setListId(tc.expected);
			String actual = t.getListId();
			assertEquals(actual,tc.expected);
		}
	}

	 @Test
	 void getID(){
		 Task t = mockTask;
		 ObjectId oID = new ObjectId();
		 t.setId(oID);
		 assertEquals(t.getId(), oID);
	 }

	@Test
	 void setListId() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setListId(tc.newText);
			assertEquals(t.getListId(),tc.newText);
		}
	}

	@Test
	void setID(){
		Task t = mockTask;
		ObjectId oID = new ObjectId();
		t.setId(oID);
		assertEquals(t.getId(), oID);
	}

	 @Test
	 void setBody() {
		 Task t = mockTask;
		 Task.Taskbody mockbody =  new Task.Taskbody("mockTopic",true,"mockDescription");
		 t.setBody(mockbody);
		 Task.Taskbody actual = t.getBody();
		 assertEquals(actual,mockbody);
	 }
}
