package com.gocaspi.taskfly.User;

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

import java.util.List;


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
}
