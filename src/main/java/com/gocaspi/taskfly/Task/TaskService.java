package com.gocaspi.taskfly.Task;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TaskService {
    @Autowired
    private TaskRepository repo;

    public TaskService (TaskRepository repo){
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

    public ArrayList<Task> getService_AllTasksOfUser(String id){
        List<Task> tasks = getRepo().findAll();
        ArrayList<Task> tasksToId = new ArrayList<Task>() ;
        for(Task t : tasks){
            if (Arrays.stream(t.getUserIds()).toList().contains(id)){
                tasksToId.add(t);
            }
        }

        return tasksToId;
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

