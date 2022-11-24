package com.gocaspi.taskfly.teammanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    /**
     * returns the service of type TeamManagementService
     *
     * @return TeamManagementServoce that is injected in the Controller
     */
    public TeamManagementService getService() {
        return service;
    }
    /**
     * given a request body this endpoint converts the body to a Team and validates the input Data against set criteria (see method below)
     * If criteria are matched returns HttpStatus:202, else throws an exception.
     *
     * @param body json of the team that should be created
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
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
    /**
     * Decodes the Requestbody into a team, which will be the update to the existing team assigned to the Pathvariable id.
     * Then the method calls the service to validate that there is a team assigned to the provided id and update that team with the Requestbody.
     * If the service doesn't throw an exception then a success message and HttpStatus:202 will be returned,
     * else the exception from the service is thrown.
     *
     * @param id id of the team that should be updated
     * @param body update of the team to the provided id
     * @return ResponseEntity containing success message and updated team id and the http status code
     * @throws ChangeSetPersister.NotFoundException Exception if no team to the id was found
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable String id,@RequestBody String body) throws HttpClientErrorException.NotFound {
        var update = jsonToTeamManagement(body);
        getService().updateService(id,update);
        var msg = "successfully updated Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    /**
     * if there is a team to the provided id (path variable) then that team is removed from the mongoDB, else an exception is thrown
     *
     * @param id, identifier of the team of intereset
     * @return ResponseEntity, containing the team from the db and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no team to the id was found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable String id) throws HttpClientErrorException.NotFound {
        getService().deleteService(id);
        var msg = "successfully deleted Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    /**
     * returns a team from a Json
     *
     * @param jsonPayload String
     * @return team fetched from the jsonPayload
     */
    public TeamManagement jsonToTeamManagement(String jsonPayload){
        return new Gson().fromJson(jsonPayload, TeamManagement.class);
    }

}
