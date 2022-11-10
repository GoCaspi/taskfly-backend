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
    public TaskCollectionRepository getRepo(){
        return repo;
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

    public void createTaskCollection(String body) throws HttpClientErrorException {
        TaskCollection taskCollection = parseJSON(body);
        repo.insert(taskCollection);
    }

    public String getTaskCollectionsByUser(String userID) {
        List<TaskCollectionGetQuery> tcResult = repo.findByOwnerID(userID);
        System.out.println(tcResult);
        return new Gson().toJson(tcResult);
    }

    public TaskCollection parseJSON(String body){
        return new Gson().fromJson(body, TaskCollection.class);
    }
}
