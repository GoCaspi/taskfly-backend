package com.gocaspi.taskfly.reseter;

import com.gocaspi.taskfly.task.TaskRepository;
import com.gocaspi.taskfly.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ResetService {
    @Autowired
    private UserRepository repo;
    private final HttpClientErrorException exceptionNotFound;
    private final HttpClientErrorException exceptionBadRequest;
    public ResetService (UserRepository repo){
        this.repo = repo;
        this.exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionBadRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }

    /**
     * returns the TaskRepository that was set in the constructor
     *
     * @return TaskRepository
     */
    public UserRepository getRepo() {
        return repo;
    }


}
