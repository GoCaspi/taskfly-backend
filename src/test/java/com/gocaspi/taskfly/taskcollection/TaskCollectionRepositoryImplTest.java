package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import com.gocaspi.taskfly.taskcollection.TaskCollection;
import com.gocaspi.taskfly.teammanagement.TeamManagement;
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


class TaskCollectionRepositoryImplTest {

    final private String mockTeamName = "FakeTeam1";
    final private String[] mockTeamMemberArray = {new ObjectId().toHexString(), new ObjectId().toHexString(), new ObjectId().toHexString()};
    final private String mockUserID = new ObjectId().toHexString();
    final private String mockTCID = new ObjectId().toHexString();
    final private String mockTCName = "TaskCollection1";
    final private String mockTCTeamID = new ObjectId().toHexString();
    final private List<String> mockTeamMember = Arrays.asList(new ObjectId().toHexString(), new ObjectId().toHexString());
    final private TaskCollection mockTC = new TaskCollection(mockTCID, mockTCName, mockTCTeamID, mockUserID, mockTeamMember);
    final private TeamManagement mockTeam = new TeamManagement(mockUserID, mockTeamName, mockTeamMemberArray, mockTCTeamID);
    LocalDateTime mockTime = LocalDateTime.now().withHour(3).withMinute(0).withSecond(0).withNano(0);
    String mockListId = mockTCID;
    String mockObjectId = new ObjectId().toHexString();
    Task.Taskbody mockbody = new Task.Taskbody("mockTopic",true,"mockDescription");
    Task mockTask = new Task(mockUserID,mockListId,mockTCTeamID,mockTime,mockObjectId,mockbody);
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
        Task validTask = new Task(mockUserID,mockListId,mockTCTeamID,mockTime,mockObjectId,mockbody);
        mongoTemplate.save(validTask, "task");
        Task invalidTask = new Task(mockUserID,fakeTaskCollectionID.toHexString(),mockTCTeamID,mockTime,new ObjectId().toHexString(),mockbody);
        mongoTemplate.save(invalidTask, "task");
        TaskCollection altCollection = new TaskCollection(fakeTaskCollectionID.toHexString(), mockTCName, mockTCTeamID, fakeUserID.toHexString(), mockTeamMember);
        mongoTemplate.save(altCollection, "taskCollection");
        List<TaskCollectionGetQuery> taskCollectionList = taskCollectionRepositoryImpl.findByOwnerID(mockUserID);
        assertEquals(1, taskCollectionList.size());
        assertEquals(1, taskCollectionList.get(0).getTasks().size());
    }

    @Test
    void findByIDTest(){
        TaskCollectionRepositoryImpl taskCollectionRepositoryImpl = new TaskCollectionRepositoryImpl(mongoTemplate);
        mongoTemplate.save(mockTC, "taskCollection");
        ObjectId fakeTaskCollectionID = new ObjectId();
        ObjectId fakeUserID = new ObjectId();
        Task validTask = new Task(mockUserID,mockListId,mockTCTeamID,mockTime,mockObjectId,mockbody);
        mongoTemplate.save(validTask, "task");
        Task invalidTask = new Task(mockUserID,fakeTaskCollectionID.toHexString(),mockTCTeamID,mockTime,new ObjectId().toHexString(),mockbody);
        mongoTemplate.save(invalidTask, "task");
        TaskCollection altCollection = new TaskCollection(fakeTaskCollectionID.toHexString(), mockTCName, mockTCTeamID, fakeUserID.toHexString(), mockTeamMember);
        mongoTemplate.save(altCollection, "taskCollection");
        TaskCollectionGetQuery taskCollectionGetQuery = taskCollectionRepositoryImpl.findByID(mockTCID);
        assertEquals(mockTCName, taskCollectionGetQuery.getName());
    }

    @Test
    void findByTeamIDTest(){
        TaskCollectionRepositoryImpl taskCollectionRepositoryImpl = new TaskCollectionRepositoryImpl(mongoTemplate);
        mongoTemplate.save(mockTC, "taskCollection");
        ObjectId fakeTaskCollectionID = new ObjectId();
        ObjectId fakeUserID = new ObjectId();
        Task validTask = new Task(mockUserID,mockListId,mockTCTeamID,mockTime,mockObjectId,mockbody);
        mongoTemplate.save(validTask, "task");
        Task invalidTask = new Task(mockUserID,fakeTaskCollectionID.toHexString(),mockTCTeamID,mockTime,new ObjectId().toHexString(),mockbody);
        mongoTemplate.save(invalidTask, "task");
        TaskCollection altCollection = new TaskCollection(fakeTaskCollectionID.toHexString(), mockTCName, new ObjectId().toHexString(), fakeUserID.toHexString(), mockTeamMember);
        mongoTemplate.save(altCollection, "taskCollection");
        List<TaskCollectionGetQuery> taskCollectionList = taskCollectionRepositoryImpl.findByTeamID(mockTCTeamID);
        assertEquals(1, taskCollectionList.size());
        assertEquals(1, taskCollectionList.get(0).getTasks().size());
    }
    @Test
    void findByUserIDTest(){
        TaskCollectionRepositoryImpl taskCollectionRepositoryImpl = new TaskCollectionRepositoryImpl(mongoTemplate);
        ObjectId fakeMockUserID = new ObjectId();
        ObjectId fakeMockTeamID = new ObjectId();
        TeamManagement fakeTeam = new TeamManagement(fakeMockUserID.toHexString(), mockTeamName, mockTeamMemberArray, fakeMockTeamID.toHexString());
        mongoTemplate.save(mockTeam, "teamManagement");
        mongoTemplate.save(fakeTeam, "teamManagement");
        mongoTemplate.save(mockTC, "taskCollection");
        ObjectId fakeTaskCollectionID = new ObjectId();
        ObjectId fakeUserID = new ObjectId();
        Task validTask = new Task(mockUserID,mockListId,mockTCTeamID,mockTime,mockObjectId,mockbody);
        mongoTemplate.save(validTask, "task");
        Task invalidTask = new Task(mockUserID,fakeTaskCollectionID.toHexString(),mockTCTeamID,mockTime,new ObjectId().toHexString(),mockbody);
        mongoTemplate.save(invalidTask, "task");
        TaskCollection altCollection = new TaskCollection(fakeTaskCollectionID.toHexString(), mockTCName, new ObjectId().toHexString(), fakeUserID.toHexString(), mockTeamMember);
        mongoTemplate.save(altCollection, "taskCollection");
        List<TaskCollectionGetQuery> taskCollectionList = taskCollectionRepositoryImpl.findByUserID(mockUserID);
        assertEquals(1, taskCollectionList.size());
        assertEquals(1, taskCollectionList.get(0).getTasks().size());
    }
}
