package com.gocaspi.taskfly.teammanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import javax.validation.Valid;



/**
 * Class for TeamManagementController
 */
@RestController
@ResponseBody
@RequestMapping("/teammanagement")
public class TeamManagementController {
    @Autowired //teilt spring mit wo es Objekte in anderen klassen anlegen soll
    private TeamManagementRepository repository;
    @Autowired
    private final TeamManagementService service;

    /**
     * Constractor for TeamManagementController
     *
     * @param repository variable for the interface TeamManagementRepository
     * @param service variable for the class TeamManagementService
     */
    public TeamManagementController(TeamManagementRepository repository, TeamManagementService service){
        super();
        this.repository = repository;
        this.service = service;
    }
    public static class TeamRequest{
        private String teamName;

        private String[] members;

        private String userID;
        @Id
        private String id;
        public  TeamRequest(String userID, String teamName, String[] members, String id) {
            this.userID = userID;
            this.teamName = teamName;
            this.members = members;
            this.id = id;
        }

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
     * @param teamRequest json of the team that should be created
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
    @PostMapping
    public ResponseEntity<String> createTeam(@Valid @RequestBody TeamRequest teamRequest) throws HttpClientErrorException.BadRequest {
        TeamManagement teamManagement =  new TeamManagement(teamRequest.userID,teamRequest.teamName,teamRequest.members,teamRequest.id);
        getService().insertService(teamManagement);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * If there is a team to the provided id (path variable), then the method calls the service to validate that there
     * is a team assigned to the provided id and adds a new team member to the team.
     * If the service doesn't throw an exception then a success message and HttpStatus:202 will be returned,
     * else the exception from the service is thrown.
     * @param id id of the team that should be insert a team Member to the team
     * @param body id of the new team Member for the team
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
    @PostMapping("/{id}/{body}")
    public ResponseEntity<String> addTeamMember(@PathVariable String id, @PathVariable String body) throws HttpClientErrorException.BadRequest {
        String newMemberId = body;
        var team = getService().getTeamById(id);
        getService().addMemberService(id ,team.getMembers(),newMemberId, team);
        var msg = "successfully created new Member in Team";
        return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
    }
    /**
     * If there is a team to the provided id (path variable), then the method calls the service to validate that there
     * is a team assigned to the provided id and delete a team member to the team.
     * If the service doesn't throw an exception then a success message and HttpStatus:202 will be returned,
     * else the exception from the service is thrown.
     * @param id id of the team that should be updated
     * @param member delete of the team Member to the provided id
     * @return ResponseEntity containing a success message along with the http status code
     * @throws HttpClientErrorException.BadRequest Exception if the provided requestbody is missing fields
     */
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
     * @param teamRequest update of the team to the provided id
     * @return ResponseEntity containing success message and updated team id and the http status code
     * @throws HttpClientErrorException.NotFound Exception if no team to the id was found
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable String id,@RequestBody TeamRequest teamRequest) throws HttpClientErrorException.NotFound {
        TeamManagement teamManagement =  new TeamManagement(teamRequest.userID,teamRequest.teamName,teamRequest.members,teamRequest.id);
        getService().updateService(id,teamManagement);
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


}
