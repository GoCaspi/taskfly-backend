
package com.gocaspi.taskfly.taskcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@ResponseBody
@RequestMapping("/tc")
public class TaskCollectionController {
    @Autowired
    private TaskCollectionService service;
    public TaskCollectionController(TaskCollectionService taskCollectionService){
        this.service = taskCollectionService;
    }
    @PostMapping
    public ResponseEntity<TaskCollection> createTaskCollectionEndpoint(@Valid @RequestBody TaskCollection tc){
        var result = service.createTaskCollection(tc);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/owner/{ownerID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByOwnerID(@PathVariable String ownerID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByOwnerID(ownerID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<TaskCollectionGetQuery> getTaskCollectionByID(@PathVariable String id){
        TaskCollectionGetQuery tc = service.getTaskCollectionByID(id);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    @GetMapping("/team/{teamID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByTeamID(@PathVariable String teamID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionByTeamID(teamID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionByUserID(@PathVariable String userID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByUserID(userID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskCollectionByID(@PathVariable String id){
        service.deleteTaskCollectionByID(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public ResponseEntity<TaskCollection> patchTaskCollectionByID(@RequestParam("id") String id, @RequestBody TaskCollection tc){
        service.updateTaskCollectionByID(id, tc);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}