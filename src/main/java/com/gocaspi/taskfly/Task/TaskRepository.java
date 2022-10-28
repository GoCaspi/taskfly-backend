package com.gocaspi.taskfly.Task;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public interface TaskRepository extends MongoRepository<Task, String> {
    /**
     * returns all tasks that have the provided id contained in their userIds array
     * @param id String
     * @return ArrayList of all tasks assigned to id
     */
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
