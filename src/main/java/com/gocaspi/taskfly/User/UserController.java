package com.gocaspi.taskfly.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import java.util.List;
import java.util.Optional;


@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;
    public UserController(UserRepository repository){
        super();
        this.repository=repository;
    }

    @PostMapping
    public void creatUser(@RequestBody String body){
        User user = new Gson().fromJson(body, User.class);
        repository.insert(user);
    }

    /**
     * This endpoint couverts to get a User Data with UserID.
     * @param id
     * @return Json of a User,that contain Users Data to the given userID id
     */
    @GetMapping("/{id}")
    public String getUser(@PathVariable String id){
        if(repository.existsById(id)){
        User user = repository.findById(id).get();
        return new Gson().toJson(user);
        }
        else {
            return "User Id Not Existing";
        }
    }

    /**
     * this endpoint couverts to delete a User with UserID.With Error msg if there are no UserID in the DB.
     * @param id
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return "Deleted "+id+" successfully";
        }
        else {
            return "User Id not Existing";
        }
    }
    @GetMapping()
    public String getAllUser(){

      List<User> users =repository.findAll();
        return new Gson().toJson(users);

    }

    /**
     * This endpoint update User Data.
     * @param id
     * @param body
     * @return A message if update endpoint was successfull.
     */
    @PatchMapping("/{id}")
    public String updateUser(@PathVariable String id,@RequestBody String body) {

        if (!repository.existsById(id)) {
            return "There are no User with this ID";
        }
        Optional<User> users = repository.findById(id);
        User update = new Gson().fromJson(body, User.class);

        if (update.team != null) {
            users.ifPresent(t -> t.setTeam(update.team));
        }
        if (update.listId != null) {
            users.ifPresent(t -> t.setListId(update.listId));
        }
        if (update.password!= null) {
            users.ifPresent(t-> t.setPassword(update.password));
        }
        if (update.firstName!= null) {
            users.ifPresent(t-> t.setFirstName(update.firstName));
        }

        if (update.lastName!= null) {
            users.ifPresent(t-> t.setLastName(update.lastName));
        }
        if (update.email!= null) {
            users.ifPresent(t-> t.setEmail(update.email));
        }

        users.ifPresent(t -> repository.save(t));
        return "Update User Succsess";
    }

}
