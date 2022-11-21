package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

class TaskCollectionGetQueryTest {
    private final String mockID = new ObjectId().toHexString();
    private final String mockName = "TaskCollection1";
    private final String mockTeamID = new ObjectId().toHexString();
    private final String mockOwnerID = new ObjectId().toHexString();
    private final String mockUserID = "1";
    private final String mockListID = "1";
    final private ObjectId mockObjectID = new ObjectId();

    private final String mockDeadline = "11-11-2022";
    private final Task.Taskbody mockBody = new Task.Taskbody("mockTopic", true, "mockDescription");
    @Test
    void TestTaskCollectionGetQueryConstructor(){
        Task task1 = new Task(mockUserID, mockListID, mockTeamID, mockDeadline, mockObjectID, mockBody);
        List<Task> taskList = new ArrayList<>();
        taskList.add(0, task1);
        TaskCollectionGetQuery taskCollectionGetQuery = new TaskCollectionGetQuery(mockName, mockTeamID, mockID, mockOwnerID, taskList);
        assertEquals(mockName, taskCollectionGetQuery.getName());
        assertEquals(mockID, taskCollectionGetQuery.getId());
        assertEquals(mockTeamID, taskCollectionGetQuery.getTeamID());
        assertEquals(mockOwnerID, taskCollectionGetQuery.getOwnerID());
        assertEquals(taskList, taskCollectionGetQuery.getTasks());
    }
    @Test
    void TestTaskCollectionGetQuerySetter(){
        Task task1 = new Task(mockUserID, mockListID, mockTeamID, mockDeadline, mockObjectID, mockBody);
        List<Task> taskList = new ArrayList<>();
        taskList.add(0, task1);
        TaskCollectionGetQuery taskCollectionGetQuery = new TaskCollectionGetQuery(mockName, mockTeamID, mockID, mockOwnerID, taskList);
        taskCollectionGetQuery.setId(mockID);
        taskCollectionGetQuery.setName(mockName);
        taskCollectionGetQuery.setOwnerID(mockOwnerID);
        taskCollectionGetQuery.setTeamID(mockTeamID);
        taskCollectionGetQuery.setTasks(taskList);
        assertEquals(mockName, taskCollectionGetQuery.getName());
        assertEquals(mockID, taskCollectionGetQuery.getId());
        assertEquals(mockTeamID, taskCollectionGetQuery.getTeamID());
        assertEquals(mockOwnerID, taskCollectionGetQuery.getOwnerID());
        assertEquals(taskList, taskCollectionGetQuery.getTasks());

    }
}
