package com.gocaspi.taskfly.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@RestController
@ResponseBody
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private final TaskService service;

   public TaskController (TaskRepository repository){
       this.service = new TaskService(repository);
   }

    /**
     * returns the service  of type TaskService
     *
     * @return TaskService that is injected in the Controller
     */

    /**
     * given a request body this endpoint converts the body to a Task and validates the input Data against set criteria (see method below)
     * If criteria are matched returns HttpStatus:202, else throws an exception.
     *
     * @param body json of the task that should be created
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
    @PostMapping
    public ResponseEntity<String> handleCreateNewTask(@RequestBody String body) throws HttpClientErrorException.BadRequest {
        var task = jsonToTask(body);
        service.postService(task);
        String msg = "successfully created task with id: " + task.getId().toHexString();
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    /**
     * calls the service to fetch all tasks assigned to the provided Pathvariable id (userId). If the List of tasks fetched from the service
     * has a length of zero, there are no tasks assigned to the provided id and an exception is thrown. Else a List with the tasks of the user
     * is returned along with the HttpStatus:200.
     *
     * @param id id of the user that want to fetch all his tasks
     * @return ResponseEntity containing the list with all tasks to the given id along with the http status code
     * @throws HttpClientErrorException.NotFound Exception if no task to the id was found
     */
    @GetMapping("/userId/{id}")
    public ResponseEntity<List<Task>> handleGetAllTasks(@PathVariable String id) throws HttpClientErrorException.NotFound {
        List<Task> tasks = service.getServiceAllTasksOfUser(id);
        if(tasks.isEmpty()){ throw service.getNotFound();}
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * calls the service to fetch the task of the provided id. If the service does not throw an exception (no task to the provided id was found)
     * the task to the given id is returned along with a HttpStatus:200, else an exception is thrown.
     *
     * @param id id of the task
     * @return ResponseEntity, containing the task from the db and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no task to the id was found
     */
    @GetMapping("/taskId/{id}")
    public ResponseEntity<Task> handleGetTaskById(@PathVariable String id) throws HttpClientErrorException.NotFound {
        var task = service.getServiceTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/priority/{id}")
    public ResponseEntity<List<Task>> handleGetTaskByUserIDandPriority(@PathVariable String userid) throws HttpClientErrorException.NotFound{
        var taskList = service.getTasksByPriorityService(userid);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    /**
     * if there is a task to the provided id (path variable) then that task is removed from the mongoDB, else an exception is thrown
     *
     * @param id, identifier of the task of intereset
     * @return ResponseEntity, containing the task from the db and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no task to the id was found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> handleDeleteTask(@PathVariable String id) throws HttpClientErrorException.NotFound {
        service.deleteService(id);
        var msg = "successfully deleted task with id: "+id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    /**
     * Decodes the Requestbody into a task, which will be the update to the existing task assigned to the Pathvariable id.
     * Then the method calls the service to validate that there is a task assigned to the provided id and update that task with the Requestbody.
     * If the service doesnt throw an exception then a succesmessage with the updated task id and HttpStatus:202 will be returned,
     * else the exception from the service is thrown.
     *
     * @param id id of the task that should be updated
     * @param body update of the task to the provided id
     * @return ResponseEntity containing success message and updated task id and the http status code
     * @throws ChangeSetPersister.NotFoundException Exception if no task to the id was found
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> handleUpdateTask(@PathVariable String id,@RequestBody String body) throws HttpClientErrorException.NotFound {

        var update = jsonToTask(body);
        service.updateService(id,update);
        var msg = "successfully updated task with id: "+id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    /**
     * given a requestbody (Json of a Task) the method checks if all fields are null-safe with the exception of the fields: priority and deadline, which must not be set.
     *
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that Task-payload, else return false
     */

    public boolean validateTaskFields(String jsonPayload){
        var task = jsonToTask(jsonPayload);
        return !Objects.equals(task.getUserId(), null) && !Objects.equals(task.getListId(), null) && !Objects.equals(task.getBody().getTopic(), null) && !Objects.equals(task.getBody().getDescription(), null);
    }



    /**
     * returns a Task from a Json
     *
     * @param jsonPayload String
     * @return Task fetched from the jsonPayload
     */
    public Task jsonToTask(String jsonPayload){ return new Gson().fromJson(jsonPayload, Task.class);}
}
