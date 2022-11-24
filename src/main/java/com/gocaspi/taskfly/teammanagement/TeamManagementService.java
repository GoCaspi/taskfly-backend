package com.gocaspi.taskfly.teammanagement;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
@Service
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
    /**
     * returns the TaskRepository that was set in the constructor
     *
     * @return TaskRepository
     */
    public TeamManagementRepository getRepository() { return repository; }
    /**
     * throws an error if not all necessary fields of the provided team are assigned. If all fields are validated the team is saved to the db
     *
     * @param insert team to get validated and saved
     * @throws RuntimeException Exception if not all fields are filled
     */
    public void insertService(TeamManagement insert) throws HttpClientErrorException {
        if(!validateTeamManagementFields(new Gson().toJson(insert))){
            throw exceptionBadRequest;
        }
        getRepository().insert(insert);
    }

    /**
     * returns a team that are assigned to the provided id. If there are no teams assigned to the id then
     * an exception is thrown.
     * @param id of the team
     * @return the new team what was created
     */
    public TeamManagement getTeamById(String id) throws HttpClientErrorException {
        var teamManagement = getRepository().findById(id);
        if(teamManagement.isEmpty()){
            throw exceptionNotFound;
        }
        return teamManagement.get();
    }
    public void addMemberService(String id, String[] members, String newMember, TeamManagement team) throws HttpClientErrorException {
        if(Arrays.asList(members).contains(newMember)){
            throw exceptionBadRequest;
        }
        var newMembers = new ArrayList<>(Arrays.asList(members));
        newMembers.add(newMember);
        //Liste zu Array
        var a = new String[newMembers.size()];
        newMembers.toArray(a);

        team.setMembers(a);

        updateService(id, team);
    }
    public void deleteMemberService(String id, String[] members, TeamManagement team, String member) throws HttpClientErrorException {
        if(!getRepository().existsById(id)){ throw exceptionNotFound; }

        List<String> mem = new ArrayList<>();
        for (String t: members){
            if (!Objects.equals(t, member)) {
                mem.add(t);
            }
        }

        var a = new String[mem.size()];
        mem.toArray(a);

        team.setMembers(a);

        updateService(id, team);
    }
    public void deleteService(String id) throws HttpClientErrorException {
        if (!getRepository().existsById(id)) {
            throw exceptionNotFound;
        }
        getRepository().deleteById(id);
    }
    public void updateService(String id, TeamManagement update) throws HttpClientErrorException{
        var teammanagement = getRepository().findById(id);

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
    /**
     * given a requestbody (Json of a team) the method checks if all fields are null-safe.
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that team-payload, else return false
     */
    public boolean validateTeamManagementFields(String jsonPayload){
        var teamManagement = jsonToTeamManagement(jsonPayload);

        return  teamManagement.getMembers() != null  && teamManagement.getTeamName() != null
                && teamManagement.getUserID() != null;
    }
    /**
     * returns a team from a json String
     * @param jsonPayload String
     * @return team
     */
    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }
}
