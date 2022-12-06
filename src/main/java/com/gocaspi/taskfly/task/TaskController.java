package com.gocaspi.taskfly.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.*;


/**
 * Class for TaskController
 */
@RestController
@CrossOrigin("*")
@ResponseBody
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private final TaskService service;

    /**
     * Constractor for TaskController
     *
     * @param service variable for the interface taskrepository
     */

   public TaskController (TaskService service){
       this.service = service;
   }
    /**
     * given a request body this endpoint converts the body to a Task and validates the input Data against set criteria (see method below)
     * If criteria are matched returns HttpStatus:202, else throws an exception.
     *
     * @param task which is being sent as a json to the controller via http
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
    @PostMapping
    public ResponseEntity<String> handleCreateNewTask(@Valid @RequestBody Task task) throws HttpClientErrorException.BadRequest {
        service.postService(task);
        String msg = "successfully created task with id: " + task.getId();
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

    @GetMapping("/priority/{userid}")
    public ResponseEntity<List<Task>> handleGetTaskByUserIDandPriority(@PathVariable String userid) throws HttpClientErrorException.NotFound{
        var taskList = service.getTasksByHighPriorityService(userid);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    /**
     *
     * @param userid
     * @return
     * @throws HttpClientErrorException.NotFound
     */
    @GetMapping("/private/{userid}")
    public ResponseEntity<List<Task>> handleGetPrivateTasksByUser(@PathVariable String userid) throws  HttpClientErrorException.NotFound{
        var taskList = service.getPrivateTasks(userid);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    /**
     *
     * @param userid, identifier for the user of interest
     * @return ResponseEntity, containing a List of Tasks and the http status code.
     * @throws HttpClientErrorException.NotFound Exception
     */
    @GetMapping("/shared/{userid}")
    public ResponseEntity<List<Task>> handleGetSharedTasksByUser(@PathVariable String userid) throws  HttpClientErrorException.NotFound{
        var taskList = service.getSharedTasks(userid);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    /**
     *
     * @param userid, identifier for the user of interest
     * @return ResponseEntity, containing a List of Tasks and the http status code.
     * @throws HttpClientErrorException.NotFound Exception if no matching task is found.
     */
    @GetMapping("/scheduled/week/{userid}")
    public ResponseEntity<List<Task>> handleTasksScheduledForOneWeekByUser(@PathVariable String userid) throws HttpClientErrorException.NotFound{
        var taskList = service.getTasksScheduledForOneWeek(userid);
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
     * @throws HttpClientErrorException.NotFound Exception if no task to the id was found
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> handleUpdateTask(@PathVariable String id,@RequestBody Task body) throws HttpClientErrorException.NotFound {
        service.updateService(id,body);
        var msg = "successfully updated task with id: "+ id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

}
