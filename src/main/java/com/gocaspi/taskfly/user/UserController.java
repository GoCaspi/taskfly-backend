package com.gocaspi.taskfly.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

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
    public ResponseEntity<String> handlerCreateUser(@RequestBody String body)throws HttpClientErrorException.BadRequest{
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
    public ResponseEntity<User> handlerGetUsreById(@PathVariable String id)throws HttpClientErrorException.NotFound{
        User user =getService().getServicebyid(id);
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
    public ResponseEntity<List<User>> handleGetAllUsers()throws HttpClientErrorException.NotFound{
        List<User> users =getService().getServiceAllUser();
        if (users.isEmpty()){ throw HttpClientErrorException.create(HttpStatus.NOT_FOUND,"There are No User in the DB",null,null,null);}
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> handleUpdateUser(@PathVariable String id,@RequestBody String body) throws HttpClientErrorException.NotFound{
        User update = jsonToUser(body);
        getService().updateService(id,update);
        String msg ="Successfully update User with id :" +id ;
        return new ResponseEntity<>(msg,HttpStatus.ACCEPTED);
    }


}