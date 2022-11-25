package com.gocaspi.taskfly.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;
/**
 * Class for UserController
 */
@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;
    private final UserService service;
    /**
     * Constractor for UserController
     *
     * @param repository variable for the interface userRepository
     */
    public UserController(UserRepository repository) {
        super();
        this.repository = repository;
        this.service = new UserService(repository);
    }
    /**
     * given a request body this endpoint converts the body to a user and validates the input Data against set criteria (see method below)
     * If criteria are matched returns HttpStatus:202, else throws an exception.
     *
     * @param body json of the user that should be created
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
    @PostMapping("/create")
    public ResponseEntity<String> handlerCreateUser(@RequestBody String body) throws HttpClientErrorException.BadRequest {
        var user = jsonToUser(body);
        getService().postService(user);
        var msg = "Successfully created User";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    /**
     * returns a user from a Json
     *
     * @param jsonPayload String
     * @return user fetched from the jsonPayload
     */
    public User jsonToUser(String jsonPayload) {
        return new Gson().fromJson(jsonPayload, User.class);
    }

    /**
     * calls the service to fetch the user of the provided id. If the service does not throw an exception (no user to the provided id was found)
     * the user to the given id is returned along with a HttpStatus:200, else an exception is thrown.
     *
     * @param id id of the user
     * @return ResponseEntity, containing the user from the db and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no user to the id was found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> handlerGetUsreById(@PathVariable String id) throws HttpClientErrorException.NotFound {
        var user = getService().getServicebyid(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * if there is a user to the provided id (path variable) then that user is removed from the mongoDB, else an exception is thrown
     *
     * @param id, identifier of the user of intereset
     * @return ResponseEntity, containing the user from the db and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no user to the id was found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) throws HttpClientErrorException.NotFound {
        getService().deleteService(id);
        var msg = "Successfully deleted User with id: " + id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    /**
     * returns the service of type UserService
     * @return Userservice that is injected in the Controller
     */
    public UserService getService() {
        return this.service;
    }

    /**
     * this endpoint couverts to getall User from DB.
     *
     * @return ResponseEntity containing success message and get all user and the http status code
     */
    @GetMapping()
    public ResponseEntity<List<User>> handleGetAllUsers() throws HttpClientErrorException.NotFound {
        var users = getService().getServiceAllUser();
        if (users.isEmpty()) {throw  getService().getNotFound();}
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    /**
     * Decodes the Requestbody into a user, which will be the update to the existing user assigned to the Pathvariable id.
     * Then the method calls the service to validate that there is a user assigned to the provided id and update that user with the Requestbody.
     * If the service doesn't throw an exception then a success message and HttpStatus:202 will be returned,
     * else the exception from the service is thrown.
     *
     * @param id id of the user that should be updated
     * @param body update of the user to the provided id
     * @return ResponseEntity containing success message and updated user id and the http status code
     * @throws ChangeSetPersister.NotFoundException Exception if no user to the id was found
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> handleUpdateUser(@PathVariable String id, @RequestBody String body) throws HttpClientErrorException.NotFound {
        var update = jsonToUser(body);
        getService().updateService(id, update);
        var msg = "Successfully update User with id :" + id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
}