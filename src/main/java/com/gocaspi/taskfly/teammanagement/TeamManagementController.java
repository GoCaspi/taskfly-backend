package com.gocaspi.taskfly.teammanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@ResponseBody
@RequestMapping("/teammanagement")
public class TeamManagementController {
    @Autowired //teilt spring mit wo es Objekte in anderen klassen anlegen soll
    private TeamManagementRepository repository;

    private final TeamManagementService service;

    public TeamManagementController(TeamManagementRepository repository){
        super();
        this.repository = repository;
        this.service = new TeamManagementService(repository);
    }

    public TeamManagementService getService() {
        return service;
    }

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody String body) throws HttpClientErrorException.BadRequest {
        TeamManagement insert = jsonToTeamManagement(body);
        getService().insertService(insert);
        String msg = "successfully created Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    /*hier weiter machen
    @PostMapping
    public ResponseEntity<String> addMemberInTeam(@PathVariable String id, @RequestBody String body) throws HttpClientErrorException.NotFound {
        return
    }*/
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable String id,@RequestBody String body) throws HttpClientErrorException.NotFound {

        TeamManagement update = jsonToTeamManagement(body);
        getService().updateService(id,update);
        String msg = "successfully updated task";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}/{member}")
    public ResponseEntity<String> deleteTeamMember(@PathVariable String id, @PathVariable String member) throws HttpClientErrorException.BadRequest {
        TeamManagement team = getService().getTeamById(id);
        getService().deleteMemberService(id ,team.getMembers(), team, member);
        String msg = "successfully delete Member from Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable String id) throws HttpClientErrorException.NotFound {
        getService().deleteService(id);
        String msg = "successfully deleted team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }

}
