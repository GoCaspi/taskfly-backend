package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
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


public class TaskCollectionRepositoryImplTest {
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
    void findByOwnerIDTest(){
        TaskCollectionRepositoryImpl taskCollectionRepositoryImpl = new TaskCollectionRepositoryImpl(mongoTemplate);
        mongoTemplate.save(mockTC, "taskCollection");
        ObjectId fakeTaskCollectionID = new ObjectId();
        ObjectId fakeUserID = new ObjectId();
        Task validTask = new Task(mockUserID,mockListId,mockTeam,mockTime,mockObjectId,mockbody);
        mongoTemplate.save(validTask, "task");
        Task invalidTask = new Task(mockUserID,fakeTaskCollectionID.toHexString(),mockTeam,mockTime,mockObjectId,mockbody);
        mongoTemplate.save(invalidTask, "task");
        TaskCollection altCollection = new TaskCollection(fakeTaskCollectionID.toHexString(), mockTCName, mockTCTeamID, fakeUserID.toHexString(), mockTeamMember);
        mongoTemplate.save(altCollection, "taskCollection");
        var taskCollectionList = taskCollectionRepositoryImpl.findByOwnerID(mockUserID);
        assertEquals(1, taskCollectionList.size());

    }
}
