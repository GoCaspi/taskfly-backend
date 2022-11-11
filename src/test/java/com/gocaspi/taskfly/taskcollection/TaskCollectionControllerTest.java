package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

public class TaskCollectionControllerTest {

    final private TaskCollectionRepository mockRepo = mock(TaskCollectionRepository.class);
    final private TaskCollectionService mockService = mock(TaskCollectionService.class);
    final private String mockTCID = "1234";
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private String mockTCOwnerID = new ObjectId().toHexString();
    final private TaskCollection mockTC = new TaskCollection(mockTCID, "", mockTCTeamID, "");
    final private String mockUserIds = "1";
    final private String mockListId = "1";
    final private String mockTopic = "topic1";
    final private String mockTeam = "team1";
    final private String mockPrio = "prio1";
    final private String mockDesc = "desc1";
    final private String mockDeadline = "11-11-2022";
    final private ObjectId mockObjectId = new ObjectId();
    final private Task.Taskbody mockbody = new Task.Taskbody("mockTopic","mockPrio","mockDescription");

    @Test
    void createTaskCollection(){
        TaskCollectionService s = new TaskCollectionService(mockRepo);
        TaskCollectionController t = new TaskCollectionController(s);
        class Testcase {
            final TaskCollection mockTaskCollection;
            final Boolean badRequest;

            public Testcase(TaskCollection mockTaskCollection, Boolean badRequest){
                this.badRequest = badRequest;
                this.mockTaskCollection = mockTaskCollection;
            }

        }



        Testcase[] testcases = new Testcase[]{
                new Testcase(mockTC, false),
        };
        for (Testcase tc : testcases){
            ResponseEntity<String> actual = t.createTaskCollectionEndpoint(tc.mockTaskCollection);
            System.out.println(actual);
        }
    }

}
