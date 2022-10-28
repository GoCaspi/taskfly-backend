package com.gocaspi.taskfly.services;

import com.gocaspi.taskfly.Task.Task;
import com.gocaspi.taskfly.Task.TaskRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class taskService {
    @Autowired
    private TaskRepository repo;

    public taskService (TaskRepository repo){
    this.repo = repo;
    }

    public TaskRepository getRepo() {
        return repo;
    }

    public Object postService(Task t) throws RuntimeException{
        if(!validateTaskFields(new Gson().toJson(t))){
            throw new RuntimeException("not all neccassary fields are provided");
        }
        return getRepo().insert(t);
    }
    /**
     * given a requestbody (Json of a Task) the method checks if all fields are null-safe with the exception of the fields: priority and deadline, which must not be set.
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that Task-payload, else return false
     */
    public boolean validateTaskFields(String jsonPayload){
        Task task = jsonToTask(jsonPayload);
        return !Objects.equals(task.getUserIds(), null) && !Objects.equals(task.getListId(), null) && !Objects.equals(task.getTopic(), null) && !Objects.equals(task.getDescription(), null);
    }

    /**
     * returns a Task from a json String
     * @param jsonPayload String
     * @return Task
     */
    public Task jsonToTask(String jsonPayload){ return new Gson().fromJson(jsonPayload, Task.class);}
}
