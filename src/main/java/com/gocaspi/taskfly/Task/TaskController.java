package com.gocaspi.taskfly.Task;

import com.google.gson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;


import java.util.*;

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

    /**
     * given a request body this endpoint converts the body to a Task and validates the input Data against set criteria (see method below)
     * If criteria are matched returns status 200 OK, else throws an exception
     * @param body, request body
     */
    @PostMapping
    public void createNewTask(@RequestBody String body){
        Task task = new Gson().fromJson(body, Task.class);
        if(validateTaskFields(body)){
            repository.insert(task);
        }
        else{
                throw new JsonParseException("invalid Payload");
        }
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
        // better Array list (nicht statisch shiehe vereinfachungen) !!
        // copy the task-list to an array
        Task[] taskArr= new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++){
            taskArr[i] = tasks.get(i);
        }
        // check if the userId from the request is contained by the userIds-Array in the taskArr
        // if true, add that task in the tasksOfUser Array, which contains only tasks assigned to that user

        //!! see above
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

    /**
     * if there is a task to the provided id (path variable) then that task is removed from the mongoDB, else an exception is thrown
     * @param id, identifier of the task of intereset
     */
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
            return "Deleted "+id+" successfully";
        }
        else {
            return "could not find matching Task to the provided id";
        }
    }

    /**
     * to a given path-variable id , search the injected repository if there is a task to that provided id.
     * If that holds true, checks which fields of the request-body (body) are not null and therefore should be updated, then saves
     * the updated task to the repository.
     * Else throws an exception.
     * @param id, id of the task that should be updated
     * @param body, update for the task with the id id
     * @return String, message if update process was successfull
     */
    @PutMapping("/{id}")
    public String updateTask(@PathVariable String id,@RequestBody String body){
        if(!repository.existsById(id)){
            return "could not find matching Task to the provided id";
        }
      Optional<Task> task =  repository.findById(id);
        Task update = new Gson().fromJson(body, Task.class);

        if(update.description != null){task.ifPresent(t -> t.setDescription(update.description));}
        if(update.userIds != null){task.ifPresent(t -> t.addUserIdToTask(update.userIds));}
        if(update.topic != null){task.ifPresent(t -> t.setTopic(update.topic));}
        if(update.team != null){task.ifPresent(t -> t.setTeam(update.team));}
        if(update.deadline != null){task.ifPresent(t -> t.setDeadline(update.deadline));}

        task.ifPresent(t -> repository.save(t));
        return "task was updated";
    }

    /**
     * Removes all Tasks that equals null from a Task-Array. Then returns the null-safe array
     * @param arr, array of tasks that may contain null elements
     * @return array, array of tasks which is null-safe
     */
    public Task[] RemoveNullElements(Task[] arr){
        // null elements gar nicht erst screiben
        List<Task> list = new ArrayList<Task>();

        for(Task t : arr) {
            if(t != null) { list.add(t); }
        }
        return list.toArray(new Task[list.size()]);
    }

    /**
     * given a requestbody (Json of a Task) the method checks if all fields are null-safe with the exception of the fields: priority and deadline, which must not be set.
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that Task-payload, else return false
     */
    public boolean validateTaskFields(String jsonPayload){
        Task task = new Gson().fromJson(jsonPayload, Task.class);
        return !Objects.equals(task.userIds, null) && !Objects.equals(task.listId, null) && !Objects.equals(task.topic, null) && !Objects.equals(task.description, null);
    }

}
