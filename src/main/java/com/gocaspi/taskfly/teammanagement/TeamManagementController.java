package com.gocaspi.taskfly.teammanagement;

import com.gocaspi.taskfly.teammanagement.TeamManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/teammanagement")
public class TeamManagementController {
    @Autowired
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
}
