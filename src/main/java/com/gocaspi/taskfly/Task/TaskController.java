package com.gocaspi.taskfly.Task;

import com.google.gson.JsonParseException;
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

   public TaskController (TaskRepository repository){
       super();
       this.repository = repository;
   }

    /**
     * given a request body this endpoint converts the body to a Task and validates the input Data against set criteria (see method below)
     * If criteria are matched returns status 200 OK, else throws an exception
     * @param body, request body
     */
    @PostMapping
    public void Handle_createNewTask(@RequestBody String body){
        Task task = jsonToTask(body);
        if(!validateTaskFields(body)){
            throw new JsonParseException("invalid Payload");
        }
        repository.insert(task);
    }

    /**
     * calls the db via TaskRepository interface. If no tasks are assigned to the provided id an exception is thrown,
     * else return an ArrayList with all tasks to that id as a json
     * @param id of the user
     * @return String, json of all tasks or err
     */
    @GetMapping("/v3/{id}")
    public String Handle_getAllTasks(@PathVariable String id){
        List<Task> tasks = repository.getAllTasksById(id);
        if(tasks.size() == 0){ return "no tasks were found to the provided id";}
        return new Gson().toJson(tasks);
    }



    /**
     * if there is a task to the provided id (path variable) then that task is removed from the mongoDB, else an exception is thrown
     * @param id, identifier of the task of intereset
     */
    @DeleteMapping("/{id}")
    public String Handle_deleteTask(@PathVariable String id){
        if(!repository.existsById(id)){
            return "no task to the provided id was found";
        }
        repository.deleteById(id);
        return "Deleted "+id+" successfully";
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
    public String Handle_updateTask(@PathVariable String id,@RequestBody String body){
        if(!repository.existsById(id)){
            return "could not find matching Task to the provided id";
        }
      Optional<Task> task =  repository.findById(id);
        Task update = jsonToTask(body);

        if(update.getDescription() != null){task.ifPresent(t -> t.setDescription(update.getDescription()));}
        if(update.getUserIds() != null){task.ifPresent(t -> t.addUserIdToTask(update.getUserIds()));}
        if(update.getTopic() != null){task.ifPresent(t -> t.setTopic(update.getTopic()));}
        if(update.getTeam() != null){task.ifPresent(t -> t.setTeam(update.getTeam()));}
        if(update.getDeadline() != null){task.ifPresent(t -> t.setDeadline(update.getDeadline()));}

        task.ifPresent(t -> repository.save(t));
        return "task was updated";
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

    public String taskToJson(Task t){return new Gson().toJson(t);}

}
