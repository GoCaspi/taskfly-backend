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

    @PostMapping("/createTeam")
    public ResponseEntity<String> createTeam(@RequestBody String body) throws HttpClientErrorException.BadRequest {
        TeamManagement teamManagement = jsonToTeamManagement(body);
        getService().postService(teamManagement);
        String msg = "successfully created Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }

    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }

}
