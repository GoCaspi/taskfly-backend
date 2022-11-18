package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;



class TaskCollectionTest {
    private final String mockID = new ObjectId().toHexString();
    private final String mockName = "TaskCollection1";
    private final String mockTeamID = new ObjectId().toHexString();
    private final String mockOwnerID = new ObjectId().toHexString();

    @Test
    void TaskCollectionTestPopulatedConstructor(){
        TaskCollection taskCollection = new TaskCollection(mockID, mockName, mockTeamID, mockOwnerID);
        assertEquals(mockName, taskCollection.getName());
        assertEquals(mockID, taskCollection.getId());
        assertEquals(mockTeamID, taskCollection.getTeamID());
        assertEquals(mockOwnerID, taskCollection.getOwnerID());
    }

    @Test
    void TaskCollectionTestEmptyConstructor(){
        TaskCollection taskCollection = new TaskCollection();
        assertEquals(null, taskCollection.getName());
        assertEquals(null, taskCollection.getId());
        assertEquals(null, taskCollection.getTeamID());
        assertEquals(null, taskCollection.getOwnerID());
    }

    @Test
    void TaskCollectionTestSetters(){
        TaskCollection taskCollection = new TaskCollection();
        taskCollection.setName(mockName);
        taskCollection.setId(mockID);
        taskCollection.setTeamID(mockTeamID);
        taskCollection.setOwnerID(mockOwnerID);
        assertEquals(mockName, taskCollection.getName());
        assertEquals(mockID, taskCollection.getId());
        assertEquals(mockTeamID, taskCollection.getTeamID());
        assertEquals(mockOwnerID, taskCollection.getOwnerID());

    }
}
