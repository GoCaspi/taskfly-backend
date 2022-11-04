package com.gocaspi.taskfly.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;


@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;
    private final UserService service;

    public UserController(UserRepository repository){
        super();
        this.repository=repository;
        this.service=new UserService(repository);
    }

    @PostMapping("/create")
    public ResponseEntity<String> Handler_createUser(@RequestBody String body)throws HttpClientErrorException.BadRequest{
        User user = jsonToUser(body);
        getService().postService(user);
        String msg ="Successfully created User";
        return new ResponseEntity<>(msg,HttpStatus.ACCEPTED);
    }
    public User jsonToUser(String jsonPayload){return new Gson().fromJson(jsonPayload, User.class);}
    /**
     * This endpoint couverts to get a User Data with UserID.
     * @param id
     * @return Json of a User,that contain Users Data to the given userID id
     */

    @GetMapping("/{id}")
    public ResponseEntity<User> Handler_getUsreById(@PathVariable String id)throws HttpClientErrorException.NotFound{
        User user =getService().getService_UserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    /**
     * this endpoint couverts to delete a User with UserID.With Error msg if there are no UserID in the DB.
     * @param id
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) throws HttpClientErrorException.NotFound {
        getService().deleteService(id);
        String msg = "Successfully deleted User with id: "+id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    public UserService getService(){
        return this.service;
    }

    /**
     * this endpoint couverts to getall User from DB.
     * @return
     */

    @GetMapping()
    public ResponseEntity<List<User>> Handle_getAllUsers()throws HttpClientErrorException.NotFound{
        List<User> users =getService().getService_AllUser();
        if (users.size()==0){ throw HttpClientErrorException.create(HttpStatus.NOT_FOUND,"There are No User in the DB",null,null,null);}
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> Handle_updateUser(@PathVariable String id,@RequestBody String body) throws HttpClientErrorException.NotFound{
        User update = jsonToUser(body);
        getService().updateService(id,update);
        String msg ="Successfully update User with id :" +id ;
        return new ResponseEntity<>(msg,HttpStatus.ACCEPTED);
    }


}