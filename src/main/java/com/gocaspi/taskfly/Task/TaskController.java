package com.gocaspi.taskfly.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.google.gson.Gson;
import com.gocaspi.taskfly.User.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RestController
@ResponseBody
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskRepository repository;

   public TaskController (TaskRepository repository){
       super();
       this.repository = repository;
   }

    @PostMapping
    public void postPerson(@RequestBody String body){
        Task task = new Gson().fromJson(body, Task.class);
        repository.insert(task);
    }

    /**
     * given a requestbody (Json of a Task) the method checks if all fields are null-safe with the exception of the fields: priority and deadline, which must not be set.
     * @param jsonPayload
     * @return true if the mentioned criteria holds for that Task-payload, else return false
     */
    public boolean validateTaskFields(String jsonPayload){
        Task task = new Gson().fromJson(jsonPayload, Task.class);
       if(Objects.equals(task.userIds, new String[0]) || Objects.equals(task.listId,"") || Objects.equals(task.topic,"") || Objects.equals(task.description,"")){
           return false;
       }
       return true;
    }

    /**
     * Finds all Tasks from the database and copies all Tasks that have the provided userID in their userIds information. Then that array is returned as json.
     * @param id userID
     * @return Json of a task array, that contain all tasks assigned to the given userID id
     */
    @GetMapping("/{id}")
    public String getAllTasks(@PathVariable String id){
       // fetch all Tasks from the mongoDB via accessing the TaskRepository
        List<Task> tasks = repository.findAll();
        // copy the task-list to an array
        Task[] taskArr= new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++){
            taskArr[i] = tasks.get(i);
        }
        // check if the userId from the request is contained by the userIds-Array in the taskArr
        // if true, add that task in the tasksOfUser Array, which contains only tasks assigned to that user
        Task[] tasksOfUser = new Task[taskArr.length];
        for (int j = 0; j < tasks.size(); j++){
            if(Arrays.stream(taskArr[j].userIds).anyMatch(id ::equals)){
              Task[]  arr = Arrays.copyOf(tasksOfUser, taskArr.length);
              arr[j]=taskArr[j];
                tasksOfUser = arr;
            }
        }
        Task[] response = RemoveNullElements(tasksOfUser);
        return new Gson().toJson(response);
    }
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable String id){
        repository.deleteById(id);
    }

    public Task[] RemoveNullElements(Task[] arr){
        List<Task> list = new ArrayList<Task>();

        for(Task t : arr) {
            if(t != null) {
                list.add(t);
            }
        }
        return list.toArray(new Task[list.size()]);
    }
}
