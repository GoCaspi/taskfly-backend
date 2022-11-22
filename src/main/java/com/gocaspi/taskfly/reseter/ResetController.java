package com.gocaspi.taskfly.reseter;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<List> findUserByEmail(){

        List<User> users = getService().getRepo().findUserByName();
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }
}
