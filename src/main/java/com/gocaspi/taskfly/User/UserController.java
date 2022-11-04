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
import com.gocaspi.taskfly.User.User;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;

    @PostMapping
    public void postPerson(@RequestBody String body){
        User user = new Gson().fromJson(body, User.class);
        repository.insert(user);
    }
    @GetMapping("/{id}")
    public String getPerson(@PathVariable String id){
        User user = repository.findByLastName(id);
        return new Gson().toJson(user);
    }
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable String id){
        repository.deleteById(id);
    }
    @GetMapping
    public String getAllPersons(){
        List<User> userList = repository.findAll();
        return new Gson().toJson(userList);
    }
}
