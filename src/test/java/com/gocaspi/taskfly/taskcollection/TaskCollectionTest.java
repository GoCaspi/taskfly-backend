package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class TaskCollectionTest {
    private final String mockID = new ObjectId().toHexString();
    private final String mockName = "TaskCollection1";
    private final String mockTeamID = new ObjectId().toHexString();
    private final String mockOwnerID = new ObjectId().toHexString();

    @Test
    void TaskCollectionTestPopulatedConstructor(){
        TaskCollection taskCollection = new TaskCollection(mockID, mockName, mockTeamID, mockOwnerID);
        assertEquals(taskCollection.getName(), mockName);
        assertEquals(taskCollection.getId(), mockID);
        assertEquals(taskCollection.getTeamID(), mockTeamID);
        assertEquals(taskCollection.getOwnerID(), mockOwnerID);
    }

    @Test
    void TaskCollectionTestEmptyConstructor(){
        TaskCollection taskCollection = new TaskCollection();
        assertEquals(taskCollection.getName(), null);
        assertEquals(taskCollection.getId(), null);
        assertEquals(taskCollection.getTeamID(), null);
        assertEquals(taskCollection.getOwnerID(), null);
    }

    @Test
    void TaskCollectionTestSetters(){
        TaskCollection taskCollection = new TaskCollection();
        taskCollection.setName(mockName);
        taskCollection.setId(mockID);
        taskCollection.setTeamID(mockTeamID);
        taskCollection.setOwnerID(mockOwnerID);
        assertEquals(taskCollection.getName(), mockName);
        assertEquals(taskCollection.getId(), mockID);
        assertEquals(taskCollection.getTeamID(), mockTeamID);
        assertEquals(taskCollection.getOwnerID(), mockOwnerID);

    }
}
