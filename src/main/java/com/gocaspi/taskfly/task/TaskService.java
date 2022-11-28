package com.gocaspi.taskfly.task;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
@Service
public class TaskService {
    @Autowired
    private TaskRepository repo;
    private final HttpClientErrorException exceptionNotFound;
    private final HttpClientErrorException exceptionBadRequest;
    public TaskService (TaskRepository repo){
        this.repo = repo;
        this.exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionBadRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }

    /**
     * returns the TaskRepository that was set in the constructor
     *
     * @return TaskRepository
     */
    public TaskRepository getRepo() {
        return repo;
    }

    public HttpClientErrorException getNotFound(){return this.exceptionNotFound;}

    /**
     * throws an error if not all necessary fields of the provided task are assigned. If all fields are validated the task is saved to the db
     *
     * @param t task to get validated and saved
     * @throws RuntimeException
     */
    public void postService(Task t) throws HttpClientErrorException {
         getRepo().insert(t);
    }

    /**
     * returns all tasks that are assigned to the provided id. If there are no tasks assigned the length of the list will be 0
     *
     * @param id of the user
     * @return ArrayList containing the tasks of the user with the id id
     */
    public List<Task> getServiceAllTasksOfUser(String id){
        List<Task> tasks = getRepo().findAll();
        List<Task> tasksToId = new ArrayList<>();
        for(Task t : tasks){
            if (Objects.equals(t.getUserId(), id)){
                tasksToId.add(t);
            }
        }
        return tasksToId;
    }

    public Task getServiceTaskById(String id) throws HttpClientErrorException.NotFound {
        if(!getRepo().existsById(id)){ throw exceptionNotFound; }
        var task = repo.findById(id);
        if (task.isEmpty()){
            throw exceptionNotFound;
        }
        return task.get();
    }

    public void deleteService(String id) throws HttpClientErrorException {
        if(!getRepo().existsById(id)){ throw exceptionNotFound; }
        getRepo().deleteById(id);
    }

    public void updateService(String id,Task update) throws HttpClientErrorException {
        var task =  repo.findById(id);

        if(!repo.existsById(id)){ throw exceptionNotFound; }
        task.ifPresent( t->{
            if(!Objects.equals(update.getBody().getDescription(), "")) {
                t.getBody().setDescription(update.getBody().getDescription());
            }
            if(!Objects.equals(update.getBody().getTopic(), "")){
                t.getBody().setTopic(update.getBody().getTopic());
            }
            if(!Objects.equals(update.getTeam(), "")){
                t.setTeam(update.getTeam());
            }
            if(!Objects.equals(update.getDeadline(), "")){
                t.setDeadline(update.getDeadline());
            }
            if(!Objects.equals(update.getListId(), "")){
                t.setListId(update.getListId());
            }
            getRepo().save(t);
        });
    }

    public List<Task> getTasksByPriorityService(String userid) {
        var taskList = repo.getTaskByUserIdAndBody_Priority(userid, true);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    public List<Task> getPrivateTasks(String userid){
        var taskList = repo.findPrivateTasksByUserID(userid);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    public List<Task> getSharedTasks(String userid){
        var taskList = repo.findSharedTasksByUserID(userid);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    public List<Task> getTasksScheduledForOneWeek(String userid){
        var taskList = repo.findTasksScheduledForOneWeekByUserID(userid);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    /**
     * given a requestbody (Json of a Task) the method checks if all fields are null-safe with the exception of the fields: priority and deadline, which must not be set.
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that Task-payload, else return false
     */
    public boolean validateTaskFields(String jsonPayload){
        var task = jsonToTask(jsonPayload);
        return !Objects.equals(task.getUserId(), null) && !Objects.equals(task.getListId(), null) && !Objects.equals(task.getBody().getTopic(), null) && !Objects.equals(task.getBody().getDescription(), null);
    }

    /**
     * returns a Task from a json String
     * @param jsonPayload String
     * @return Task
     */
    public Task jsonToTask(String jsonPayload){ return new Gson().fromJson(jsonPayload, Task.class);}
}

