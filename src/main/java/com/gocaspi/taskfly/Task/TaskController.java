package com.gocaspi.taskfly.Task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.util.*;

@RestController
@ResponseBody
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskRepository repository;
    private final TaskService service;

   public TaskController (TaskRepository repository){
       super();
       this.repository = repository;
       this.service = new TaskService(repository);
   }

    public TaskService getService() {
        return this.service;
    }


    /**
     * given a request body this endpoint converts the body to a Task and validates the input Data against set criteria (see method below)
     * If criteria are matched returns status 200 OK, else throws an exception
     * @param body, request body
     */
    @PostMapping
    public void Handle_createNewTask(@RequestBody String body) throws RuntimeException{
        Task task = jsonToTask(body);
        try{
            getService().postService(task);
        }
        catch (RuntimeException r){
            throw r;
        }
    }

    /**
     * calls the db via TaskRepository interface. If no tasks are assigned to the provided id an exception is thrown,
     * else return an ArrayList with all tasks to that id as a json
     * @param id of the user
     * @return String, json of all tasks or err
     */
    @GetMapping("/v3/{id}")
    public String Handle_getAllTasks(@PathVariable String id) throws RuntimeException {
        List<Task> tasks = getService().getService_AllTasksOfUser(id);
        if(tasks.size() == 0){ throw new RuntimeException("no tasks are assigned to the provided id");}
        return new Gson().toJson(tasks);
    }

    /**
     * if there is a task to the provided id (path variable) then that task is removed from the mongoDB, else an exception is thrown
     * @param id, identifier of the task of intereset
     */
    @DeleteMapping("/{id}")
    public void Handle_deleteTask(@PathVariable String id) throws RuntimeException{
       try { getService().deleteService(id); }
       catch (RuntimeException r){ throw r; }
    }

    /**
     * to a given path-variable id , search the injected repository if there is a task to that provided id.
     * If that holds true, checks which fields of the request-body (body) are not null and therefore should be updated, then saves
     * the updated task to the repository.
     * Else throws an exception.
     * @param id, id of the task that should be updated
     * @param body, update for the task with the id id
     * @return String, message if update process was successfull
     */
    @PutMapping("/{id}")
    public void Handle_updateTask(@PathVariable String id,@RequestBody String body){

        Task update = jsonToTask(body);
        try {
            getService().updateService(id,update);
        }
        catch (RuntimeException r){
            throw r;
        }
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
