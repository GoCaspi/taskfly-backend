package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.taskcollection.TaskCollection;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.CollectionUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.mock;
@ExtendWith(SpringExtension.class)
class TaskRepositoryImplTest {
    final private String mockUserID = new ObjectId().toHexString();
    final private String mockTCID = new ObjectId().toHexString();
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private List<String> mockTeamMember = Arrays.asList(new ObjectId().toHexString(), new ObjectId().toHexString());
    final private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockUserID, mockTeamMember);
    LocalDateTime mockTime = LocalDateTime.now().withHour(3).withMinute(0).withSecond(0).withNano(0);
    String mockListId = mockTCID;
    String mockTeam = "team1";
    ObjectId mockObjectId = new ObjectId();
    Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");
    Task mockTask = new Task(mockUserID,mockListId,mockTeam,mockTime,mockObjectId,mockbody);
    List<Task> mockTaskList = Arrays.asList(mockTask, mockTask);
    private static final String CONNECTION_STRING = "mongodb://%s:%d";
    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;

    @AfterEach
    void clean() {
        mongodExecutable.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        String ip = "localhost";
        int port = 27018;


        ImmutableMongodConfig mongodConfig = MongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "test");
    }
    @Test
    void findPrivateTasksByUserIDTest(){
        TaskRepositoryImpl tri = new TaskRepositoryImpl(mongoTemplate);
        ObjectId privateTCObjectID = new ObjectId();
        TaskCollection privateTC = mockTC;
        privateTC.setMembers(new ArrayList<>());
        privateTC.setTeamID("");
        privateTC.setId(privateTCObjectID.toHexString());
        mongoTemplate.save(privateTC,"taskCollection");
        mongoTemplate.save(mockTC, "taskCollection");
        Task privateTask = mockTask;
        privateTask.setListId(privateTCObjectID.toHexString());
        mongoTemplate.save(privateTask,"task");
        mongoTemplate.save(mockTask, "task");
        List<Task> correctList = Arrays.asList(privateTask);
        List<Task> taskList = tri.findPrivateTasksByUserID(mockUserID);
        assertEquals(1, taskList.size());
        assertEquals(correctList.get(0).getId(), taskList.get(0).getId());
        assertEquals(correctList.get(0).getBody().getHighPriority(), taskList.get(0).getBody().getHighPriority());
        assertEquals(correctList.get(0).getBody().getTopic(), taskList.get(0).getBody().getTopic());
        assertEquals(correctList.get(0).getBody().getDescription(), taskList.get(0).getBody().getDescription());
        assertEquals(correctList.get(0).getDeadline(), taskList.get(0).getDeadline());
        assertEquals(correctList.get(0).getUserId(), taskList.get(0).getUserId());
        assertEquals(correctList.get(0).getListId(), taskList.get(0).getListId());
    }
    @Test
    void findSharedTasksByUserIDTest(){
        TaskRepositoryImpl tri = new TaskRepositoryImpl(mongoTemplate);
        ObjectId privateTCObjectID = new ObjectId();
        TaskCollection privateTC = new TaskCollection(privateTCObjectID.toHexString(), mockTCName, "", mockUserID, new ArrayList<>());
        mongoTemplate.save(privateTC,"taskCollection");
        mongoTemplate.save(mockTC, "taskCollection");
        Task privateTask = new Task(mockUserID,privateTCObjectID.toHexString(),mockTeam,mockTime,mockObjectId,mockbody);
        privateTask.setListId(privateTCObjectID.toHexString());
        mongoTemplate.save(privateTask,"task");
        mongoTemplate.save(mockTask, "task");
        List<Task> correctList = Arrays.asList(mockTask);
        List<Task> taskList = tri.findSharedTasksByUserID(mockUserID);
        assertEquals(1, taskList.size());
        assertEquals(correctList.get(0).getId(), taskList.get(0).getId());
        assertEquals(correctList.get(0).getBody().getHighPriority(), taskList.get(0).getBody().getHighPriority());
        assertEquals(correctList.get(0).getBody().getTopic(), taskList.get(0).getBody().getTopic());
        assertEquals(correctList.get(0).getBody().getDescription(), taskList.get(0).getBody().getDescription());
        assertEquals(correctList.get(0).getDeadline(), taskList.get(0).getDeadline());
        assertEquals(correctList.get(0).getUserId(), taskList.get(0).getUserId());
        assertEquals(correctList.get(0).getListId(), taskList.get(0).getListId());
    }
}
