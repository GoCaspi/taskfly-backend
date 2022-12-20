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

    private final HttpClientErrorException httpNotFoundError = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);

    /**
     * the constructor for the TaskCollectionService Class.
     * @param repo The TaskCollectionRepository
     */
    public TaskCollectionService(TaskCollectionRepository repo){
        this.repo = repo;
    }

    /**
     * This service passes an TaskCollection Object to the Repository which stores it in the MongoDB.
     * The Return is the entire TaskCollection Object including the assigned ID.
     * @param body the TaskCollection Object that is to be stored in the DB.
     * @return the stored TaskCollection Object with the new ID.
     * @throws HttpClientErrorException
     */
    public TaskCollection createTaskCollection(TaskCollection body) throws HttpClientErrorException {
        repo.insert(body);
        return body;
    }

    /**
     * This service calls the findByOwnerID repository function which gets all TaskCollections
     * where the user is assigned as the Owner of an TaskCollection from the db
     * @param userID the userID of the user who could be an owner of a taskCollection
     * @return List<TaskCollectionGetQuery>
     */
    public List<TaskCollectionGetQuery> getTaskCollectionsByOwnerID(String userID) {
        List<TaskCollectionGetQuery> tc = repo.findByOwnerID(userID);
        if(tc.isEmpty()){
            throw httpNotFoundError;
        }
        return tc;    }

    /**
     * This service calls the findByID repository function which returns the
     * TaskCollection with the corresponding ID from the db.
     * @param collID
     * @return TaskCollectionGetQuery
     */
    public TaskCollectionGetQuery getTaskCollectionByID(String collID){
        TaskCollectionGetQuery tc = repo.findByID(collID);
        if(Objects.isNull(tc)){
            throw httpNotFoundError;
        }
        return tc;
    }

    /**
     * This service calls the findByTeamID repository function which gets all TaskCollections
     * where the supplied TeamID is set.
     * @param teamID the ID of the Team
     * @return List<TaskCollectionGetQuery>
     */
    public List<TaskCollectionGetQuery> getTaskCollectionByTeamID(String teamID){
        List<TaskCollectionGetQuery> tc = repo.findByTeamID(teamID);
        if(tc.isEmpty()){
            throw httpNotFoundError;
        }
        return tc;
    }

    /**
     * This service calls the findByUserID repository function which gets all TaskCollections where
     * the supplied User has access to. e.g. the user is listed within the members or part of the team the
     * taskCollection is assigned to
     * @param userid
     * @return List<TaskCollectionGetQuery>
     */
    public List<TaskCollectionGetQuery> getTaskCollectionsByUserID(String userid){
        List<TaskCollectionGetQuery> tc = repo.findByUserID(userid);
        if(tc.isEmpty()){
            throw httpNotFoundError;
        }
        return tc;
    }


    /**
     * This service first checks if the supplied id matches a taskCollection that exists within the database and when it does,
     * the taskCollection gets deleted.
     * @param id
     */
    public void deleteTaskCollectionByID(String id){
        if (!repo.existsById(id)){
            throw httpNotFoundError;
        }
        repo.deleteById(id);
    }

    /**
     * This service first checks if the supplied id matches a TaskCollection that exists within the database, and when it does,
     * the supplied fields get updated
     * @param id the taskCollection which should be updated
     * @param update the taskCollection Object which contains the new fields
     */
    public void updateTaskCollectionByID(String id, TaskCollection update){
        String[] emptyArr = new String[0];
        Optional<TaskCollection> task = repo.findById(id);
        if(!repo.existsById(id)){ throw httpNotFoundError; }
        task.ifPresent( t->{
            if(!Objects.equals(update.getName(), "")){t.setName(update.getName());}
            if(!Objects.equals(update.getOwnerID(), "")){t.setOwnerID(update.getOwnerID());}
            if(!Objects.equals(update.getTeamID(), "")){t.setTeamID(update.getTeamID());}
            if(!Objects.equals(update.getMembers(), emptyArr)){t.setMembers(update.getMembers());}
            repo.save(t);
        });
    }


}