
package com.gocaspi.taskfly.taskcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/tc")
public class TaskCollectionController {
    @Autowired
    private TaskCollectionService service;
    public TaskCollectionController(TaskCollectionService taskCollectionService){
        this.service = taskCollectionService;
    }

    /**
     * takes a json formatted task collection as an input and saves it in the database via the service
     * @param tc a TaskCollection Object
     * @return the newly created taskcollection object
     */
    @PostMapping
    public ResponseEntity<TaskCollection> createTaskCollectionEndpoint(@Valid @RequestBody TaskCollection tc){
        var result = service.createTaskCollection(tc);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * takes an userid as an input and returns all taskcollections where this user is assigned as the owner
     * @param ownerID the userid of the corresponding user
     * @return a list of TaskCollections where the user is assigned as the owner
     */
    @GetMapping("/owner/{ownerID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByOwnerID(@PathVariable String ownerID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByOwnerID(ownerID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    /**
     * returns the task collection that has the same id as supplied as a param.
     * @param id the id of the TaskCollection
     * @return a TaskCollection that have been identified by the supplied id.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<TaskCollectionGetQuery> getTaskCollectionByID(@PathVariable String id){
        TaskCollectionGetQuery tc = service.getTaskCollectionByID(id);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    /**
     * takes a teamID as an input and returns all tasks collections which have been shared to the team.
     * @param teamID the teamid of the corresponding team
     * @return a list of TaskCollectionsGetQuery where the supplied teamID matches
     */
    @GetMapping("/team/{teamID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByTeamID(@PathVariable String teamID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionByTeamID(teamID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    /**
     * takes a userID as an input and returns all task collections which have been assigned to the user
     * @param userID the userid of the corresponding user
     * @return a list of TaskCollectionsGetQuery which have been assigned to the user
     */
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionByUserID(@PathVariable String userID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByUserID(userID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    /**
     * takes the id of a task collection as an input and deletes the corresponding task collection.
     * @param id the id of the taskcollection which should get deleted
     * @return status 202 if the task collection have been successfully deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskCollectionByID(@PathVariable String id){
        service.deleteTaskCollectionByID(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * updates a taskcollection by supplying the taskcollection id and a taskcollection body
     * @param id the id of the to be updated taskcollection
     * @param tc the body of an taskcollection with the to be changed fields
     * @return status code 202 if the taskCollection have been updated successfully
     */
    @PatchMapping
    public ResponseEntity<TaskCollection> patchTaskCollectionByID(@RequestParam("id") String id, @RequestBody TaskCollection tc){
        service.updateTaskCollectionByID(id, tc);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * handles stomp messages which are being sent to task collections. all messages sent to MessageMapping are being relayed
     * to the sendTo Annotation
     * @param message the recieved websocket message
     * @return the payload of the original message, as it contains the to be redirected data.
     */
    @MessageMapping("/collection/broker/{collectionID}")
    @SendTo("/collection/{collectionID}")
    public String sendTaskCollectionMessage(Message<String> message){
        return message.getPayload();
    }




}