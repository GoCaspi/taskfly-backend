package com.gocaspi.taskfly.Task;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void postService(Task t) throws RuntimeException{
        if(!validateTaskFields(new Gson().toJson(t))){
            throw new RuntimeException("not all neccassary fields are provided");
        }
         getRepo().insert(t);
    }

    /**
     * returns all tasks that are assigned to the provided id. If there are no tasks assigned the length of the list will be 0
     *
     * @param id of the user
     * @return ArrayList containing the tasks of the user with the id id
     */
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

    public void deleteService(String id) throws RuntimeException{
        if(!getRepo().existsById(id)){ throw new RuntimeException(); }
        getRepo().deleteById(id);
    }

    public void updateService(String id,Task update) throws RuntimeException{
        Optional<Task> task =  getRepo().findById(id);

        if(!getRepo().existsById(id)){ throw new RuntimeException(); }
        task.ifPresent( t->{
            if(update.getDescription() != null){t.setDescription(update.getDescription());}
            if(update.getUserIds() != null){t.addUserIdToTask(update.getUserIds());}
            if(update.getTopic() != null){t.setTopic(update.getTopic());}
            if(update.getTeam() != null){t.setTeam(update.getTeam());}
            if(update.getDeadline() != null){t.setDeadline(update.getDeadline());}
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
        return !Objects.equals(task.getUserIds(), null) && !Objects.equals(task.getListId(), null) && !Objects.equals(task.getTopic(), null) && !Objects.equals(task.getDescription(), null);
    }

    /**
     * returns a Task from a json String
     * @param jsonPayload String
     * @return Task
     */
    public Task jsonToTask(String jsonPayload){ return new Gson().fromJson(jsonPayload, Task.class);}
}

