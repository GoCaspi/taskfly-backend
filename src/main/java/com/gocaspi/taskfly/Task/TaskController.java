package com.gocaspi.taskfly.Task;


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
    private TaskRepository repository;
    private final TaskService service;

   public TaskController (TaskRepository repository){
       super();
       this.repository = repository;
       this.service = new TaskService(repository);
   }

    /**
     * returns the service  of type TaskService
     *
     * @return TaskService that is injected in the Controller
     */
    public TaskService getService() {
        return this.service;
    }

    /**
     * given a request body this endpoint converts the body to a Task and validates the input Data against set criteria (see method below)
     * If criteria are matched returns HttpStatus:202, else throws an exception.
     *
     * @param body json of the task that should be created
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
    @PostMapping
    public ResponseEntity<String> Handle_createNewTask(@RequestBody String body) throws HttpClientErrorException.BadRequest {
        Task task = jsonToTask(body);
        getService().postService(task);
        String msg = "successfully created task with id: " + task.getTaskIdString();
        return new ResponseEntity<String>(msg,HttpStatus.ACCEPTED);
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
    public ResponseEntity<List<Task>> Handle_getAllTasks(@PathVariable String id) throws HttpClientErrorException.NotFound {
        List<Task> tasks = getService().getService_AllTasksOfUser(id);
        if(tasks.size() == 0){ throw HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no tasks are assigned to the provided userId", null, null, null);}
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
    public ResponseEntity<Task> Handle_getTaskById(@PathVariable String id) throws HttpClientErrorException.NotFound {
        Task task = getService().getService_TaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * if there is a task to the provided id (path variable) then that task is removed from the mongoDB, else an exception is thrown
     *
     * @param id, identifier of the task of intereset
     * @return ResponseEntity, containing the task from the db and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no task to the id was found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> Handle_deleteTask(@PathVariable String id) throws HttpClientErrorException.NotFound {
        getService().deleteService(id);
        String msg = "successfully deleted task with id: "+id;
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
    public ResponseEntity<String> Handle_updateTask(@PathVariable String id,@RequestBody String body) throws HttpClientErrorException.NotFound {

        Task update = jsonToTask(body);
        getService().updateService(id,update);
        String msg = "successfully updated task with id: "+id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    /**
     * given a requestbody (Json of a Task) the method checks if all fields are null-safe with the exception of the fields: priority and deadline, which must not be set.
     *
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that Task-payload, else return false
     */

    public boolean validateTaskFields(String jsonPayload){
        Task task = jsonToTask(jsonPayload);
        return !Objects.equals(task.getUserId(), null) && !Objects.equals(task.getListId(), null) && !Objects.equals(task.getTopic(), null) && !Objects.equals(task.getDescription(), null);
    }



    /**
     * returns a Task from a Json
     *
     * @param jsonPayload String
     * @return Task fetched from the jsonPayload
     */
    public Task jsonToTask(String jsonPayload){ return new Gson().fromJson(jsonPayload, Task.class);}
}