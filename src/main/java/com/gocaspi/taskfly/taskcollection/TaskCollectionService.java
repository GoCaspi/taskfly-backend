package com.gocaspi.taskfly.taskcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskCollectionService {
    @Autowired
    private TaskCollectionRepository repo;

    public TaskCollectionService(TaskCollectionRepository repo){
        this.repo = repo;
    }


    private final HttpClientErrorException httpNotFoundError = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);


    public TaskCollection createTaskCollection(TaskCollection body) throws HttpClientErrorException {
        repo.insert(body);
        return body;
    }

    public List<TaskCollectionGetQuery> getTaskCollectionsByUser(String userID) {
        List<TaskCollectionGetQuery> tc = repo.findByOwnerID(userID);
        if(tc.isEmpty()){
            throw httpNotFoundError;
        }
        return repo.findByOwnerID(userID);
    }

    public TaskCollectionGetQuery getTaskCollectionByID(String collID){
        TaskCollectionGetQuery tc = repo.findByID(collID);
        if(Objects.isNull(tc)){
            throw httpNotFoundError;
        }
        return repo.findByID(collID);
    }

    public void deleteTaskCollectionByID(String id){
        repo.deleteById(id);
    }

    public void updateTaskCollectionByID(String id, TaskCollection update){
        Optional<TaskCollection> task = repo.findById(id);
        if(!repo.existsById(id)){ throw httpNotFoundError; }
        task.ifPresent( t->{
            if(!Objects.equals(update.getName(), "")){t.setName(update.getName());}
            if(!Objects.equals(update.getOwnerID(), "")){t.setOwnerID(update.getOwnerID());}
            if(!Objects.equals(update.getTeamID(), "")){t.setTeamID(update.getTeamID());}
            repo.save(t);
        });
    }
}
