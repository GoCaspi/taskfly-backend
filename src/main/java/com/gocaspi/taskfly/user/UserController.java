package com.gocaspi.taskfly.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;
@RestController
@ResponseBody
@CrossOrigin("*")
@RequestMapping("/user")

public class UserController {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository repository;
    private final UserService service;

    public UserController(UserRepository repository) {
        super();
        this.repository = repository;
        this.service = new UserService(repository);
        this.encoder = new BCryptPasswordEncoder();
    }
    /**
     * Any user can access this API - No Authentication required
     * @param body
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<String> handlerCreateUser(@RequestBody String body) throws HttpClientErrorException.BadRequest {
        var user = jsonToUser(body);
        user.setEmail(user.getEmail());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setSrole(user.getSrole());
        getService().postService(user);
        var msg = "Successfully created User";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    /**
     * User who has logged in successfully can access this API
     * @param email
     * @return
     */
    @GetMapping("/userInfo")
    public User getUserInfo(@RequestParam("email")String email){
        return service.getDetails(email);
    }

    /**
     * User Login
     * @return
     */
  @PostMapping("/login")

    public String login()throws AuthenticationException {

     return "Successfully logged in by user :"+ SecurityContextHolder.getContext().getAuthentication().getName();
    }
    /**
     * User who has the role ROLE_WRITE can only access this API
     * @param email
     * @return
     */
    @GetMapping("/getUserRoles")
    public String getUserRoles(@RequestParam("email")String email){
        return service.getUserRoles(email);
    }

    public User jsonToUser(String jsonPayload) {
        return new Gson().fromJson(jsonPayload, User.class);
    }

    /**
     * This endpoint couverts to get a User Data with UserID.
     *
     * @param id
     * @return Json of a User,that contain Users Data to the given userID id
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> handlerGetUsreById(@PathVariable String id) throws HttpClientErrorException.NotFound {
        var user = getService().getServicebyid(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * this endpoint couverts to delete a User with UserID.With Error msg if there are no UserID in the DB.
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) throws HttpClientErrorException.NotFound {
        getService().deleteService(id);
        var msg = "Successfully deleted User with id: " + id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    public UserService getService() {
        return this.service;
    }

    /**
     * this endpoint couverts to getall User from DB.
     *
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<User>> handleGetAllUsers() throws HttpClientErrorException.NotFound {
        var users = getService().getServiceAllUser();
        if (users.isEmpty()) {throw  getService().getNotFound();}
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    /**
     * this endpoint couverts to Update all data User from DB.
     * @param id
     * @param body
     * @return
     * @throws HttpClientErrorException.NotFound
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> handleUpdateUser(@PathVariable String id, @RequestBody String body) throws HttpClientErrorException.NotFound {
        var update = jsonToUser(body);
        getService().updateService(id, update);
        var msg = "Successfully update User with id :" + id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
}