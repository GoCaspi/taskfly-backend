package com.gocaspi.taskfly.teammanagement;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
public class TeamManagementService {
    @Autowired
    private TeamManagementRepository repository;
    private final HttpClientErrorException exceptionNotFound;
    private final HttpClientErrorException exceptionBadRequest;
    public TeamManagementService(TeamManagementRepository repository){
        this.repository = repository;
        this.exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionBadRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }
    public TeamManagementRepository getRepository() { return repository; }
    public HttpClientErrorException getNotFound() { return exceptionNotFound; }

    public void postService(TeamManagement t) throws HttpClientErrorException {
        if(!validateTaskFields(new Gson().toJson(t))){
            throw exceptionBadRequest;
        }
        getRepository().insert(t);
    }

    public boolean validateTaskFields(String jsonPayload){
        TeamManagement teamManagement = jsonToTeamManagement(jsonPayload);

        return !Objects.equals(teamManagement.getTeamID(), null) && !Objects.equals(teamManagement.getMembers(), null ) &&
                !Objects.equals(teamManagement.getTeamName(), null) && !Objects.equals(teamManagement.getUserID(), null);
    }
    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }

}
