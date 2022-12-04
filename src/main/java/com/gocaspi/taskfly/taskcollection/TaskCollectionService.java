package com.gocaspi.taskfly.taskcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
/**
 * Class for TaskCollectionService
 */
@Service
public class TaskCollectionService {
    @Autowired
    private TaskCollectionRepository repo;

    private final HttpClientErrorException httpNotFoundError = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
    /**
     * Constractor for TaskCollectionService
     *
     * @param repo variable for the interface TaskCollectionRepository
     */
    public TaskCollectionService(TaskCollectionRepository repo){
        this.repo = repo;
    }
    /**
     * throws an error if not all necessary fields of the provided TaskCollection are assigned. If all fields are validated the TaskCollection is saved to the db
     *
     * @param body TaskCollection to get validated and saved
     * @throws HttpClientErrorException Exception if not all fields are filled
     */
    public TaskCollection createTaskCollection(TaskCollection body) throws HttpClientErrorException {
        repo.insert(body);
        return body;
    }
    /**
     * returns a TaskCollection that are assigned to the provided user id. If the taskcollection list is empty assigned to the user id then
     * an exception is thrown.
     * @param userID assigned to the taskcollection
     * @return the list taskcollection to the provided user id
     */
    public List<TaskCollectionGetQuery> getTaskCollectionsByUser(String userID) {
        List<TaskCollectionGetQuery> tc = repo.findByOwnerID(userID);
        if(tc.isEmpty()){
            throw httpNotFoundError;
        }
        return tc;    }
    /**
     * returns a TaskCollection that are assigned to the provided id. If the id to the Taskcollection is not found then
     * an exception is thrown.
     * @param collID assigned to the taskcollection
     * @return taskcollection to the provided id
     */
    public TaskCollectionGetQuery getTaskCollectionByID(String collID){
        TaskCollectionGetQuery tc = repo.findByID(collID);
        if(Objects.isNull(tc)){
            throw httpNotFoundError;
        }
        return tc;
    }
    /**
     * returns a TaskCollection that are assigned to the provided team id. If the taskcollection list is empty assigned to the team id then
     * an exception is thrown.
     * @param teamID assigned to the taskcollection
     * @return the list taskcollection to the provided team id
     */
    public List<TaskCollectionGetQuery> getTaskCollectionByTeamID(String teamID){
        List<TaskCollectionGetQuery> tc = repo.findByTeamID(teamID);
        if(tc.isEmpty()){
            throw httpNotFoundError;
        }
        return tc;
    }
    /**
     * If there is no taskcollection to the provided id then an exception is thrown that the taskcollection does not exist else
     * that taskcollection will be removed from the mongoDB
     * @param id id of the taskcollection
     */
    public void deleteTaskCollectionByID(String id){
        if (!repo.existsById(id)){
            throw httpNotFoundError;
        }
        repo.deleteById(id);
    }
    /**
     * If there is no taskcollection to the provided id then an exception is thrown that the taskcollection does not exist else
     * the taskcollection will be updated to the existing taskcollection assigned to the id
     * @param id id of the taskcollection
     * @param update the taskcollection what should be updated
     */
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