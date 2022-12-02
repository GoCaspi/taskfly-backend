package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



class TaskCollectionTest {
    private final String mockID = new ObjectId().toHexString();
    private final String mockName = "TaskCollection1";
    private final String mockTeamID = new ObjectId().toHexString();
    private final String mockOwnerID = new ObjectId().toHexString();
    final private List<String> mockTeamMember = Arrays.asList("123", "456", "789");

    @Test
    void TaskCollectionTestPopulatedConstructor(){
        TaskCollection taskCollection = new TaskCollection(mockID, mockName, mockTeamID, mockOwnerID, mockTeamMember);
        assertEquals(mockName, taskCollection.getName());
        assertEquals(mockID, taskCollection.getId());
        assertEquals(mockTeamID, taskCollection.getTeamID());
        assertEquals(mockOwnerID, taskCollection.getOwnerID());
        assertEquals(mockTeamMember, taskCollection.getMembers());
    }

    @Test
    void TaskCollectionTestEmptyConstructor(){
        TaskCollection taskCollection = new TaskCollection();
        assertEquals(null, taskCollection.getName());
        assertEquals(null, taskCollection.getId());
        assertEquals(null, taskCollection.getTeamID());
        assertEquals(null, taskCollection.getOwnerID());
        assertEquals(null, taskCollection.getMembers());
    }

    @Test
    void TaskCollectionTestSetters(){
        TaskCollection taskCollection = new TaskCollection();
        taskCollection.setName(mockName);
        taskCollection.setId(mockID);
        taskCollection.setTeamID(mockTeamID);
        taskCollection.setOwnerID(mockOwnerID);
        taskCollection.setMembers(mockTeamMember);
        assertEquals(mockName, taskCollection.getName());
        assertEquals(mockID, taskCollection.getId());
        assertEquals(mockTeamID, taskCollection.getTeamID());
        assertEquals(mockOwnerID, taskCollection.getOwnerID());
        assertEquals(mockTeamMember, taskCollection.getMembers());


    }
}
