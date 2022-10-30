package com.gocaspi.taskfly.Task;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.MethodNotAllowedException;

import java.lang.reflect.Method;
import java.util.*;

public class TaskService {
    @Autowired
    private TaskRepository repo;
    public TaskService (TaskRepository repo){
        this.repo = repo;
    }

    /**
     * returns the TaskRepository that was set in the constructor
     *
     * @return TaskRepository
     */
    public TaskRepository getRepo() {
        return repo;
    }

    /**
     * throws an error if not all necessary fields of the provided task are assigned. If all fields are validated the task is saved to the db
     *
     * @param t task to get validated and saved
     * @throws RuntimeException
     */
    public void postService(Task t) throws ChangeSetPersister.NotFoundException {
        if(!validateTaskFields(new Gson().toJson(t))){
            throw new ChangeSetPersister.NotFoundException();
        }
         getRepo().insert(t);
    }

    /**
     * returns all tasks that are assigned to the provided id. If there are no tasks assigned the length of the list will be 0
     *
     * @param id of the user
     * @return ArrayList containing the tasks of the user with the id id
     */
    public List<Task> getService_AllTasksOfUser(String id){
        List<Task> tasks = getRepo().findAll();
        List<Task> tasksToId = new ArrayList<Task>();
        for(Task t : tasks){
            if (Objects.equals(t.getUserId(), id)){
                tasksToId.add(t);
            }
        }
        return tasksToId;
    }

    public void deleteService(String id) throws ChangeSetPersister.NotFoundException {
        if(!getRepo().existsById(id)){ throw new ChangeSetPersister.NotFoundException(); }
        getRepo().deleteById(id);
    }

    public void updateService(String id,Task update) throws ChangeSetPersister.NotFoundException {
        Optional<Task> task =  getRepo().findById(id);

        if(!getRepo().existsById(id)){ throw new ChangeSetPersister.NotFoundException(); }
        task.ifPresent( t->{
            if(update.getDescription() != null){t.setDescription(update.getDescription());}
            if(update.getUserId() != null){t.setUserId(update.getUserId());}
            if(update.getTopic() != null){t.setTopic(update.getTopic());}
            if(update.getTeam() != null){t.setTeam(update.getTeam());}
            if(update.getDeadline() != null){t.setDeadline(update.getDeadline());}
            if(update.getListId() != null){t.setListId(update.getListId());}
            getRepo().save(t);
                });
    }

    /**
     * given a requestbody (Json of a Task) the method checks if all fields are null-safe with the exception of the fields: priority and deadline, which must not be set.
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that Task-payload, else return false
     */
    public boolean validateTaskFields(String jsonPayload){
        Task task = jsonToTask(jsonPayload);
        return !Objects.equals(task.getUserId(), null) && !Objects.equals(task.getListId(), null) && !Objects.equals(task.getTopic(), null) && !Objects.equals(task.getDescription(), null);
    }

    /**
     * returns a Task from a json String
     * @param jsonPayload String
     * @return Task
     */
    public Task jsonToTask(String jsonPayload){ return new Gson().fromJson(jsonPayload, Task.class);}
}

