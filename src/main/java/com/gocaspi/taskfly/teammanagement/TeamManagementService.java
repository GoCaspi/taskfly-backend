package com.gocaspi.taskfly.teammanagement;

import com.gocaspi.taskfly.teammanagement.TeamManagementRepository;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
public class TeamManagementService {
    @Autowired
    private TeamManagementRepository repo;

    public TeamManagementService(TeamManagementRepository repo){
        this.repo = repo;
        this.exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionBadRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }
    private final HttpClientErrorException exceptionNotFound;

    private final HttpClientErrorException exceptionBadRequest;

    public HttpClientErrorException getNotFound(){return this.exceptionNotFound;}

}
