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

	@Test
	public void setDescription() {
		Task t = mockTask;
		for (Testcase_setString tc : testcases){
			t.setDescription(tc.newText);
			assertEquals(t.getDescription(),tc.newText);
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
}
