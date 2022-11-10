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

    public void insertService(TeamManagement insert) throws HttpClientErrorException {
        if(!validateTeamManagementFields(new Gson().toJson(insert))){
            throw exceptionBadRequest;
        }
        getRepository().insert(insert);
    }

    //hier weiter machen
    public void addMemberService(String id, TeamManagement insert) throws HttpClientErrorException {
        if(!validateTeamManagementFields(new Gson().toJson(insert))){
            throw exceptionBadRequest;
        }
        getRepository().insert(insert);
    }

    public void deleteService(String id) throws HttpClientErrorException {
        if(!getRepository().existsById(id)){ throw exceptionNotFound; }
        getRepository().deleteById(id);
    }
    public void updateService(String id, TeamManagement update) throws HttpClientErrorException{
        Optional<TeamManagement> teammanagement = getRepository().findById(id);

        if (!getRepository().existsById(id)) {
            throw exceptionNotFound;
        }

        teammanagement.ifPresent(t -> {
            if (update.getUserID() != null) {
                t.setUserID(update.getUserID());
            }
            if (update.getTeamName() != null) {
                t.setTeamName(update.getTeamName());
            }
            if (update.getMembers() != null) {
                t.setMembers(update.getMembers());
            }
            getRepository().save(t);
        });
    }

    public boolean validateTeamManagementFields(String jsonPayload){
        TeamManagement teamManagement = jsonToTeamManagement(jsonPayload);

        return !Objects.equals(teamManagement.getID(), null) && !Objects.equals(teamManagement.getMembers(), null ) &&
                !Objects.equals(teamManagement.getTeamName(), null) && !Objects.equals(teamManagement.getUserID(), null);
    }
    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }

}
