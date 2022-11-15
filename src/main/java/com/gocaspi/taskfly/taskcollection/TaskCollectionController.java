package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @PostMapping
    public ResponseEntity<TaskCollection> createTaskCollectionEndpoint(@RequestBody TaskCollection tc){
        TaskCollection result = service.createTaskCollection(tc);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(params = {"userid"})
    public ResponseEntity<List<TaskCollectionGetQuery>> getTaskCollectionsByUserID(@RequestParam("userid") String userID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByUser(userID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }
    @GetMapping(params = {"id"})
    public ResponseEntity<TaskCollectionGetQuery> getTaskCollectionByID(@RequestParam ("id") String id){
        TaskCollectionGetQuery tc = service.getTaskCollectionByID(id);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTaskCollectionByID(@RequestParam String id){
        service.deleteTaskCollectionByID(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public ResponseEntity<TaskCollection> patchTaskCollectionByID(@RequestParam("id") String id, @RequestBody TaskCollection tc){
        service.updateTaskCollectionByID(id, tc);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
