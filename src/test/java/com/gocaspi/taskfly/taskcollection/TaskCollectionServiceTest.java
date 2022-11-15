package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class TaskCollectionServiceTest {
    final private TaskCollectionRepository mockRepo = mock(TaskCollectionRepository.class);
    final private String mockTCID = "1234";
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private String mockTCOwnerID = new ObjectId().toHexString();
    final private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockTCOwnerID);
    final private String mockUserIds = "1";
    final private String mockListId = "1";
    final private String mockTopic = "topic1";
    final private String mockTeam = "team1";
    final private String mockPrio = "prio1";
    final private String mockDesc = "desc1";
    final private String mockDeadline = "11-11-2022";
    final private ObjectId mockObjectId = new ObjectId();
    final private Task.Taskbody mockBody = new Task.Taskbody("mockTopic","mockPrio","mockDescription");

    @Test
    public void createTaskCollectionTest(){
        
    }
}
