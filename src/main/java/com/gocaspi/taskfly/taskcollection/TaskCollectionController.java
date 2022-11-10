package com.gocaspi.taskfly.taskcollection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/tc")
public class TaskCollectionController {
    @Autowired
    private TaskCollectionService service;
    @PostMapping
    public void createTaskCollectionEndpoint(@RequestBody String body){
        service.createTaskCollection(body);
    }

    @GetMapping
    public String getTaskCollectionsByUserID(@RequestParam("userid") String userID){
        return service.getTaskCollectionsByUser(userID);
    }


}
