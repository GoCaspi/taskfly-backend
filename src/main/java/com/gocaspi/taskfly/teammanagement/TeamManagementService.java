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
    public TeamManagementRepository getRepository() { return repository; }
    public HttpClientErrorException getNotFound() { return exceptionNotFound; }

    public void insertService(TeamManagement insert) throws HttpClientErrorException {
        if(!validateTeamManagementFields(new Gson().toJson(insert))){
            throw exceptionBadRequest;
        }
        getRepository().insert(insert);
    }

    public TeamManagement getTeamById(String id) throws HttpClientErrorException {
        //if(!getRepository().existsById(id)){ throw exceptionNotFound; }

        Optional<TeamManagement> teamManagement = getRepository().findById(id);
        if(teamManagement.isEmpty()){
            throw exceptionNotFound;
        }

        return teamManagement.get();
        //return getRepository().findById(id).isPresent() ? getRepository().findById(id).get() : new TeamManagement(null,null,null,null);

    }

    public void addMemberService(String id, String[] members, String newMember, TeamManagement team) throws HttpClientErrorException {
        if(Arrays.asList(members).contains(newMember)){
            throw exceptionBadRequest;
        }

        List<String> newMembers = new ArrayList<>(Arrays.asList(members));
        newMembers.add(newMember);
        //Liste zu Array
        String[] a = new String[newMembers.size()];
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

        String[] a = new String[mem.size()];
        mem.toArray(a);

        team.setMembers(a);

        updateService(id, team);
    }
    public void deleteService(String id) throws HttpClientErrorException {
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

        return /*!Objects.equals(teamManagement.getId(), null) &&*/ !Objects.equals(teamManagement.getMembers(), null ) &&
                !Objects.equals(teamManagement.getTeamName(), null) && !Objects.equals(teamManagement.getUserID(), null);
    }
    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }

}
