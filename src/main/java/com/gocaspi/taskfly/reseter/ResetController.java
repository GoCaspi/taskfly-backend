package com.gocaspi.taskfly.reseter;

import com.gocaspi.taskfly.task.Task;
import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@RestController
@ResponseBody
@RequestMapping("/reset")
public class ResetController {
    @Autowired
    private UserRepository repository;
    private final ResetService service;

    public ResetController (UserRepository repository){
        super();
        this.repository = repository;
        this.service = new ResetService(repository);
    }

    /**
     * returns the service  of type ResetService
     *
     * @return ResetService that is injected in the Controller
     */
    public ResetService getService() {
        return this.service;
    }

    @PostMapping
    public ResponseEntity<List> handleReset(@RequestBody String body) throws NoSuchAlgorithmException {
        Reset resetRequest = jsonToReset(body);
       String hashMail = resetRequest.hashStr(resetRequest.getEmail());
        if(Objects.equals(resetRequest.getLastName(), "")){
        }
        List<User> users = getService().getUserByEmail(hashMail,resetRequest.getLastName());

        // return only userId in the messagge
        // send email (!!!to the email of the resetRequest.getEmail() !!!) with the userId in the text and a link to the frontend-form
        // next-next step: create new post-handler: posting userId and password and retypedd password to the handler will update the password assigned to the userId if the user to the userId has the field: reseted true

        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

    /**
     * returns a Reset(body) from a Json
     *
     * @param jsonPayload String
     * @return Task fetched from the jsonPayload
     */
    public Reset jsonToReset(String jsonPayload){ return new Gson().fromJson(jsonPayload, Reset.class);}
}
