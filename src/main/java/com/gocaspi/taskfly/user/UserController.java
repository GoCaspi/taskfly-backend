package com.gocaspi.taskfly.user;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Class for UserController
 */
@RestController
@ResponseBody
@RequestMapping("/user")


public class UserController {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private final UserService service;

    /**
     * Constractor for UserController
     *
     * @param userService variable for the interface userRepository
     */

    public UserController(UserService userService) {
        super();
        this.service = userService;
        this.encoder = new BCryptPasswordEncoder();
    }

    public static class UserRequest{
        private User.Userbody body;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String srole;
        private boolean reseted;
        /**
         * Any user can access this API - No Authentication required
         * @param
         * @return
         */
        public UserRequest (String firstName, String lastName, String email,String password ,String srole, User.Userbody body,Boolean reseted) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.srole = srole;
            this.body = body;
            this.reseted = reseted;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> handlerCreateUser(@Valid @RequestBody UserRequest userRequest) throws HttpClientErrorException.BadRequest {
        User user =  new User(userRequest.firstName,userRequest.lastName,userRequest.email,userRequest.password,userRequest.srole,userRequest.body,userRequest.reseted);
        user.setEmail(service.hashStr(user.getEmail()));
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
     * @param userRequest update of the user to the provided id
     * @return ResponseEntity containing success message and updated user id and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no user to the id was found
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> handleUpdateUser(@PathVariable String id, @RequestBody UserRequest userRequest) throws HttpClientErrorException.NotFound {
        User user =  new User(userRequest.firstName,userRequest.lastName,userRequest.email,userRequest.password,userRequest.srole,userRequest.body,userRequest.reseted);
        getService().updateService(id, user);
        var msg = "Successfully update User with id :" + id;
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    public String hashStr(String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();

    }

}