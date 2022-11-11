package com.gocaspi.taskfly.taskcollection;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class TaskCollectionService {
    @Autowired
    private TaskCollectionRepository repo;
    @Autowired
    private MongoTemplate mongoTemplate;
    public TaskCollectionService(TaskCollectionRepository taskCollectionRepository){
        this.repo = taskCollectionRepository;
    }

    public List<TaskCollectionGetQuery> getTaskCollectionByOwnerIDLookup(String userID) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task")
                .localField("_id")
                .foreignField("listId")
                .as("tasks");
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("ownerID").is(userID)), lookupOperation);
        return mongoTemplate.aggregate(aggregation, "TaskCollection", TaskCollectionGetQuery.class).getMappedResults();
    }

    public String createTaskCollection(TaskCollection body) throws HttpClientErrorException {
        repo.insert(body);
        return new Gson().toJson(body);
    }

    public List<TaskCollectionGetQuery> getTaskCollectionsByUser(String userID) {
        return repo.findByOwnerID(userID);
    }

    public String getTaskCollectionByID(String collID){
        TaskCollectionGetQuery tcResult = repo.findByID(collID);
        return new Gson().toJson(tcResult);
    }

    public TaskCollection parseJSON(String body){
        return new Gson().fromJson(body, TaskCollection.class);
    }
}
