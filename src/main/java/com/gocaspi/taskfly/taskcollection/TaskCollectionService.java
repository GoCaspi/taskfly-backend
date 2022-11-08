package com.gocaspi.taskfly.taskcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskCollectionService {
    @Autowired
    private TaskCollectionRepository repo;

    public TaskCollectionRepository getRepo(){
        return repo;
    }

    public String createTaskCollection(String body){

    }


}
