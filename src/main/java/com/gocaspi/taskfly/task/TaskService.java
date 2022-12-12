package com.gocaspi.taskfly.task;

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

    public TaskService (TaskRepository repo){
        this.repo = repo;
        this.exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
    }
    /**
     * throws an error if not all necessary fields of the provided task are assigned. If all fields are validated the task is saved to the db
     *
     * @param t task to get validated and saved
     * @throws RuntimeException
     */
    public void postService(Task t) throws HttpClientErrorException {
         repo.insert(t);
    }

    /**
     * returns all tasks that are assigned to the provided id. If there are no tasks assigned the length of the list will be 0
     *
     * @param id of the user
     * @return ArrayList containing the tasks of the user with the id id
     */
    public List<Task> getServiceAllTasksOfUser(String id){
        var taskList = repo.getTasksByUserId(id);
        if (taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    public Task getServiceTaskById(String id) throws HttpClientErrorException.NotFound {
        var task = repo.findById(id);
        if (task.isEmpty()){
            throw exceptionNotFound;
        }
        return task.get();
    }

    public void deleteService(String id) throws HttpClientErrorException {
        if(!repo.existsById(id)){ throw exceptionNotFound; }
        repo.deleteById(id);
    }

    public void updateService(String id,Task update) throws HttpClientErrorException {
        var task =  repo.findById(id);

        if(!repo.existsById(id)){ throw exceptionNotFound; }
        task.ifPresent( t->{
            if(!Objects.equals(update.getBody().getDescription(), "")) {
                t.getBody().setDescription(update.getBody().getDescription());
            }
            if(!Objects.equals(update.getBody().getHighPriority(), "")) {
                t.getBody().setHighPriority(update.getBody().getHighPriority());
            }
            if(!Objects.equals(update.getBody().getTopic(), "")){
                t.getBody().setTopic(update.getBody().getTopic());
            }
            if(!Objects.equals(update.getTeam(), "")){
                t.setTeam(update.getTeam());
            }
            if(!Objects.equals(update.getDeadline(), null)){
                t.setDeadline(update.getDeadline());
            }
            if(!Objects.equals(update.getListId(), "")){
                t.setListId(update.getListId());
            }
            repo.save(t);
        });
    }

    public List<Task> getTasksByHighPriorityService(String userid) {
        var taskList = repo.getTaskByUserIdAndBodyHighPriority(userid, true);
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

    public List<Task> getTasksScheduledForOneWeek(String userid) {
        var taskList = repo.findTasksScheduledForOneWeekByUserID(userid);
        if (taskList.isEmpty()) {
            throw exceptionNotFound;
        }
        return taskList;
    }


}

