package com.gocaspi.taskfly.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class UserService {
    @Autowired
    private UserRepository repo;
    private final HttpClientErrorException exception_notFound;
    private final HttpClientErrorException exception_badRequest;
    public  UserService (UserRepository repo){
        this.repo = repo ;
        this.exception_notFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND,"not found",null,null,null);
        this.exception_badRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND,"bad payload",null,null,null);
    }

}
