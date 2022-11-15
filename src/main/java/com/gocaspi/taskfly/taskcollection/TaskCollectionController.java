package com.gocaspi.taskfly.taskcollection;


import jdk.jfr.Experimental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/tc")
public class TaskCollectionController {
    @Autowired
    private TaskCollectionService service;
    @Autowired
    private TaskCollectionRepository repo;
    public TaskCollectionController(TaskCollectionService taskCollectionService){
        this.service = taskCollectionService;
        this.repo = repo;
    }
    @PostMapping
    public ResponseEntity<String> createTaskCollectionEndpoint(@RequestBody TaskCollection tc){
        String result = service.createTaskCollection(tc);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(params = {"userid"})
    public ResponseEntity getTaskCollectionsByUserID(@RequestParam("userid") String userID){
        List<TaskCollectionGetQuery> tc = service.getTaskCollectionsByUser(userID);
        return new ResponseEntity<>(tc, HttpStatus.OK);
    }
    @GetMapping(params = {"id"})
    public ResponseEntity getTaskCollectionByID(@RequestParam ("id") String id){
        service.getTaskCollectionByID(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
