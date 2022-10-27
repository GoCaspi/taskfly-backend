package com.gocaspi.taskfly.Task;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public interface TaskRepository extends MongoRepository<Task, String> {

    default ArrayList<Task> getAllTasksById(String id){
        List<Task> tasks = this.findAll();
        ArrayList<Task> tasksToId = new ArrayList<Task>() ;
        for(Task t : tasks){
            if (Arrays.stream(t.getUserIds()).toList().contains(id)){
                tasksToId.add(t);
            }
        }

        return tasksToId;
    }

}
