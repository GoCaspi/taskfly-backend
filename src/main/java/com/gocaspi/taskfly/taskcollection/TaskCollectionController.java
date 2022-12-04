
package com.gocaspi.taskfly.taskcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for the /tc (task collection) mapping
 */
@RestController
@ResponseBody
@RequestMapping("/tc")
public class TaskCollectionController {
    @Autowired
    private TaskCollectionService service;

    /**
     * Constructor for the task controller
     *
     * @param taskCollectionService, TaskCollection service
     */
    public TaskCollectionController(TaskCollectionService taskCollectionService){
        this.service = taskCollectionService;
    }

    /**
     * post endpoint to create a task collection. Posting a valid taskCollection body to it will save the taskCollection to
     * the database and return a HttpStatus created
     *
     * @param tc, a request body of taskCollection
     * @return ResponseEntity, contains HttpStatus of the request and the saved taskCollection as a result
     */
    @PostMapping
    public ResponseEntity<TaskCollection> createTaskCollectionEndpoint(@Valid @RequestBody TaskCollection tc){
        var result = service.createTaskCollection(tc);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * get endpoint. Providing a exsisting userId to the query will return all taskCollections that are assigned to that userID
     *
     * @param userID, id of the user as String
     * @return ResponseEntity containing a list of all taskCollections assigned to the user
     */
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByUserID(@PathVariable String userID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByUser(userID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    /**
     * get endpoint. Providing a exsisting collection id the endpoint returns that collection (assigned to the id)
     *
     * @param id, String id of the collection of interest
     * @return ResponseEntity containg the taskCollection and the HttpStatus of the request
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<TaskCollectionGetQuery> getTaskCollectionByID(@PathVariable String id){
        TaskCollectionGetQuery tc = service.getTaskCollectionByID(id);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    /**
     * get endpoint that returns all taskCollections that are assigned to the provided teamId (query)
     *
     * @param teamID, String of the teamId of interest
     * @return ResponseEntity containing the taskCollections assigned to the teamId
     */
    @GetMapping("/team/{teamID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByTeamID(@PathVariable String teamID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionByTeamID(teamID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    /**
     * delete endpoint. providing a exsisting id of a taskCollection as a query parameter will delete this collection from the database
     *
     * @param id, String of the id of the taskCollection that should be deleted
     * @return ResponseEntity containing the status of the request
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskCollectionByID(@PathVariable String id){
        service.deleteTaskCollectionByID(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * patch endpoint. Providing a exsisting taskCollection-Id as a query parameter and a request body of taskCollection will update the
     * taskCollection assigned to the provided id with  the requestbody
     *
     * @param id, String id of the collection that should be updated
     * @param tc, TaskCollection update of the collection to the id
     * @return ResponseEntity containing the status of the request
     */
    @PatchMapping
    public ResponseEntity<TaskCollection> patchTaskCollectionByID(@RequestParam("id") String id, @RequestBody TaskCollection tc){
        service.updateTaskCollectionByID(id, tc);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}