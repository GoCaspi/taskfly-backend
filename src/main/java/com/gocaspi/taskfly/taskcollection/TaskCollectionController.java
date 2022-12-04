
package com.gocaspi.taskfly.taskcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


import javax.validation.Valid;
import java.util.List;
/**
 * Class for TaskCollectionController
 */
@RestController
@ResponseBody
@RequestMapping("/tc")
public class TaskCollectionController {
    @Autowired
    private TaskCollectionService service;
    /**
     * Constractor for TaskCollectionController
     *
     * @param taskCollectionService variable for the class TaskCollectionService
     */
    public TaskCollectionController(TaskCollectionService taskCollectionService){
        this.service = taskCollectionService;
    }
    /**
     * given a request body this endpoint converts the body to a Team and validates the input Data against set criteria (see method below)
     *
     * @param tc json of the TaskCollection that should be created
     * @return ResponseEntity containing a success message along with the http status code
     */
    @PostMapping
    public ResponseEntity<TaskCollection> createTaskCollectionEndpoint(@Valid @RequestBody TaskCollection tc){
        var result = service.createTaskCollection(tc);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    /**
     * calls the service to fetch the TaskCollection of the provided user id.
     * the TaskCollection to the given id is returned along with a HttpStatus:200, else an exception is thrown.
     *
     * @param userID id of the user to the TaskCollection
     * @return ResponseEntity, containing the TaskCollection from the db and the http status code
     */
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByUserID(@PathVariable String userID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByUser(userID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }
    /**
     * calls the service to fetch the TaskCollection of the provided id. If the service does not throw an exception (no TaskCollection to the provided id was found)
     * the TaskCollection to the given id is returned along with a HttpStatus:200, else an exception is thrown.
     *
     * @param id id of the TaskCollection
     * @return ResponseEntity, containing the TaskCollection from the db and the http status code
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<TaskCollectionGetQuery> getTaskCollectionByID(@PathVariable String id){
        TaskCollectionGetQuery tc = service.getTaskCollectionByID(id);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }
    /**
     * calls the service to fetch the TaskCollection of the provided team id.
     * the TaskCollection to the given id is returned along with a HttpStatus:200, else an exception is thrown.
     *
     * @param teamID id of the team to the TaskCollection
     * @return ResponseEntity, containing the TaskCollection from the db and the http status code
     */
    @GetMapping("/team/{teamID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByTeamID(@PathVariable String teamID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionByTeamID(teamID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }
    /**
     * if there is a TaskCollection to the provided id (path variable) then that TaskCollection is removed from the mongoDB, else an exception is thrown
     *
     * @param id, identifier of the TaskCollection of intereset
     * @return ResponseEntity, containing the team from the db and the http status code
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskCollectionByID(@PathVariable String id){
        service.deleteTaskCollectionByID(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    /**
     * Decodes the Requestbody into a TaskCollection, which will be the update to the existing TaskCollection assigned to the Pathvariable id.
     * Then the method calls the service to validate that there is a TaskCollection assigned to the provided id and update that TaskCollection with the Requestbody.
     * If the service doesn't throw an exception then a success message and HttpStatus:202 will be returned,
     * else the exception from the service is thrown.
     *
     * @param id id of the TaskCollection that should be updated
     * @param tc update of the TaskCollection to the provided id
     * @return ResponseEntity containing success message and updated TaskCollection id and the http status code
     */

    @PatchMapping
    public ResponseEntity<TaskCollection> patchTaskCollectionByID(@RequestParam("id") String id, @RequestBody TaskCollection tc){
        service.updateTaskCollectionByID(id, tc);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}