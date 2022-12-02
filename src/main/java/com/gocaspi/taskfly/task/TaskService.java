package com.gocaspi.taskfly.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
/**
 * Class for TeskService
 */
@Service
public class TaskService {
    @Autowired
    private TaskRepository repo;
    private final HttpClientErrorException exceptionNotFound;
    /**
     * Constractor for TaskService
     *
     * @param repo variable for the interface TaskRepository
     */
    public TaskService (TaskRepository repo){
        this.repo = repo;
        this.exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
    }
    /**
     * throws an error if not all necessary fields of the provided task are assigned. If all fields are validated the task is saved to the db
     *
     * @param t task to get validated and saved
     * @throws RuntimeException Exception if not all fields are filled
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
    /**
     * returns a task that are assigned to the provided id. If there are no task assigned to the id then
     * an exception is thrown.
     * @param id of the task
     * @return the task to the provided id
     */
    public Task getServiceTaskById(String id) throws HttpClientErrorException.NotFound {
        var task = repo.findById(id);
        if (task.isEmpty()){
            throw exceptionNotFound;
        }
        return task.get();
    }
    /**
     * If there is no task to the provided id then an exception is thrown that the task does not exist else
     * that task will be removed from the mongoDB
     * @param id id of the task
     * @throws HttpClientErrorException throws an exception when the task does not exist
     */
    public void deleteService(String id) throws HttpClientErrorException {
        if(!repo.existsById(id)){ throw exceptionNotFound; }
        repo.deleteById(id);
    }
    /**
     * If there is no task to the provided id then an exception is thrown that the task does not exist else
     * the task will be updated to the existing task assigned to the id
     * @param id id of the task
     * @param update the task what should be updated
     * @throws HttpClientErrorException throws an exception when the task does not exist
     */
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
            if(!Objects.equals(update.getDeadline(), null)){
                t.setDeadline(update.getDeadline());
            }
            if(!Objects.equals(update.getListId(), "")){
                t.setListId(update.getListId());
            }
            repo.save(t);
        });
    }

    /**
     * Shows all tasks where the priority is set to true for the corresponding user id and throws an exception
     * if the list of tasks is empty
     *
     * @param userid of the user
     * @return the list of tasks for which the priority is set to true to the user id
     */
    public List<Task> getTasksByHighPriorityService(String userid) {
        var taskList = repo.getTaskByUserIdAndBody_HighPriority(userid, true);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    /**
     * Shows all tasks for a user id and throws an exception if the list of tasks is empty
     *
     * @param userid of the user
     * @return the list of tasks intended for a user id
     */
    public List<Task> getPrivateTasks(String userid){
        var taskList = repo.findPrivateTasksByUserID(userid);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    /**
     * Shows all shared tasks that multiple users can work on and throws an exception if the list of tasks is empty
     *
     * @param userid of the user
     * @return the list of tasks created for multiple users
     */
    public List<Task> getSharedTasks(String userid){
        var taskList = repo.findSharedTasksByUserID(userid);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    /**
     * Shows all tasks that are intended for a week for the assigned user id and throws an exception if the list
     * of tasks is empty
     *
     * @param userid of the user
     * @return the list of tasks planned for a week
     */
    public List<Task> getTasksScheduledForOneWeek(String userid) {
        var taskList = repo.findTasksScheduledForOneWeekByUserID(userid);
        if (taskList.isEmpty()) {
            throw exceptionNotFound;
        }
        return taskList;
    }
}

