package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TaskCollectionGetQueryTest {
    private final String mockID = new ObjectId().toHexString();
    private final String mockName = "TaskCollection1";
    private final String mockTeamID = new ObjectId().toHexString();
    private final String mockOwnerID = new ObjectId().toHexString();
    private final String mockUserID = "1";
    private final String mockListID = "1";
    final private String mockObjectID = new ObjectId().toHexString();
    final private List<String> mockMember = Arrays.asList("123", "456", "789");
    LocalDateTime mockTime = LocalDateTime.now();

    private final Task.Taskbody mockBody = new Task.Taskbody("mockTopic", true, "mockDescription",false);
    Task task1 = new Task(mockUserID, mockListID, mockTeamID, mockTime, mockObjectID, mockBody);
    List<Task> mockTasks = Arrays.asList(task1);
    @Test
    void TestTaskCollectionGetQuerySetter(){
        Task task1 = new Task(mockUserID, mockListID, mockTeamID, mockTime, mockObjectID, mockBody);
        List<Task> taskList = new ArrayList<>();
        taskList.add(0, task1);
        TaskCollectionGetQuery taskCollectionGetQuery = new TaskCollectionGetQuery(taskList);
        taskCollectionGetQuery.setId(mockID);
        taskCollectionGetQuery.setName(mockName);
        taskCollectionGetQuery.setOwnerID(mockOwnerID);
        taskCollectionGetQuery.setTeamID(mockTeamID);
        taskCollectionGetQuery.setTasks(taskList);
        taskCollectionGetQuery.setMembers(mockMember);
        assertEquals(mockName, taskCollectionGetQuery.getName());
        assertEquals(mockID, taskCollectionGetQuery.getId());
        assertEquals(mockTeamID, taskCollectionGetQuery.getTeamID());
        assertEquals(mockOwnerID, taskCollectionGetQuery.getOwnerID());
        assertEquals(taskList, taskCollectionGetQuery.getTasks());
        assertEquals(mockMember, taskCollectionGetQuery.getMembers());

    }

    @Test
    void TestTaskCollectionGetQueryConstructor(){
        TaskCollectionGetQuery taskCollectionGetQuery = new TaskCollectionGetQuery(mockID, mockName, mockTeamID, mockOwnerID, mockMember, mockTasks);
        assertEquals(mockName, taskCollectionGetQuery.getName());
        assertEquals(mockID, taskCollectionGetQuery.getId());
        assertEquals(mockTeamID, taskCollectionGetQuery.getTeamID());
        assertEquals(mockOwnerID, taskCollectionGetQuery.getOwnerID());
        assertEquals(mockTasks, taskCollectionGetQuery.getTasks());
        assertEquals(mockMember, taskCollectionGetQuery.getMembers());
    }
}
