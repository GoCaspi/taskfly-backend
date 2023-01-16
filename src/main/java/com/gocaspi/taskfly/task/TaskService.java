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

    /**
     * returns the task which have the same id which is supplied to the endpoint.
     *
     * @param id of the task
     * @return The Task with the matching id
     * @throws HttpClientErrorException.NotFound is thrown if no task has been found with the supplied id
     */
    public Task getServiceTaskById(String id) throws HttpClientErrorException.NotFound {
        var task = repo.findById(id);
        if (task.isEmpty()){
            throw exceptionNotFound;
        }
        return task.get();
    }

    /**
     * deletes the task with the matching id.
     *
     * @param id of the task
     * @throws HttpClientErrorException
     */
    public void deleteService(String id) throws HttpClientErrorException {
        if(!repo.existsById(id)){ throw exceptionNotFound; }
        repo.deleteById(id);
    }

    /**
     * updates the task with the corresponding id within the database with the supplied changes.
     *
     * @param id id of the task which is to be updated
     * @param update task object with all changes
     * @throws HttpClientErrorException
     */
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

    /**
     * returns all tasks of a user from the database, where the highPriority field is set to true
      * @param userid of the user
     * @return a list with all tasks where priority is set to true
     */
    public List<Task> getTasksByHighPriorityService(String userid) {
        var taskList = repo.getTaskByUserIdAndBodyHighPriority(userid, true);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    /**
     * this service calls the findPrivateTasksByUserID repository function to fetch all tasks which are only visible to the supplied user.
     * @param userid of the user
     * @return a list of all tasks which are only visible to the supplied user.
     */
    public List<Task> getPrivateTasks(String userid){
        var taskList = repo.findPrivateTasksByUserID(userid);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    /**
     * this service calls the findSharedTasksByUserID repository function to fetch all tasks which are assigned to the user
     * and also visible to other users
     * @param userid of the user
     * @return a list of tasks from a user which are visible to other users
     */
    public List<Task> getSharedTasks(String userid){
        var taskList = repo.findSharedTasksByUserID(userid);
        if(taskList.isEmpty()){
            throw exceptionNotFound;
        }
        return taskList;
    }

    /**
     * this service calls the findTasksScheduledForOneWeek repository function to fetch all tasks which have been
     * scheduled for the next week for the supplied user.
     * @param userid of the user
     * @return a list of tasks from a user whose tasks are scheduled for one week.
     */
    public List<Task> getTasksScheduledForOneWeek(String userid) {
        var taskList = repo.findTasksScheduledForOneWeekByUserID(userid);
        if (taskList.isEmpty()) {
            throw exceptionNotFound;
        }
        return taskList;
    }

    public Task toggleTaskStatus(String taskId){
        var task =  repo.findById(taskId);
        if(task.isEmpty()){ throw exceptionNotFound; }
        task.ifPresent( t->{
            var currentStatus = t.getBody().getCompletionStatus();
            t.getBody().setCompletionStatus(!currentStatus);
            repo.save(t);
        });
        return task.get();
    }

}

