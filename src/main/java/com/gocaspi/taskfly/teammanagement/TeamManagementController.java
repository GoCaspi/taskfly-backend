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
    @Autowired
    private final TeamManagementService service;

    public TeamManagementController(TeamManagementRepository repository, TeamManagementService service){
        super();
        this.repository = repository;
        this.service = service;
    }

    public TeamManagementService getService() {
        return service;
    }

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody String body) throws HttpClientErrorException.BadRequest {
        var insert = jsonToTeamManagement(body);
        getService().insertService(insert);
        var msg = "successfully created Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @PostMapping("/{id}/{body}")
    public ResponseEntity<String> addTeamMember(@PathVariable String id, @PathVariable String body) throws HttpClientErrorException.BadRequest {
        String newMemberId = body;
        var team = getService().getTeamById(id);
        getService().addMemberService(id ,team.getMembers(),newMemberId, team);
        var msg = "successfully created new Member in Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{id}/{member}")
    public ResponseEntity<String> deleteTeamMember(@PathVariable String id, @PathVariable String member) throws HttpClientErrorException.BadRequest {
        var team = getService().getTeamById(id);
        getService().deleteMemberService(id ,team.getMembers(), team, member);
        var msg = "successfully delete Member from Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable String id,@RequestBody String body) throws HttpClientErrorException.NotFound {
        var update = jsonToTeamManagement(body);
        getService().updateService(id,update);
        var msg = "successfully updated task";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable String id) throws HttpClientErrorException.NotFound {
        getService().deleteService(id);
        var msg = "successfully deleted team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }

}
