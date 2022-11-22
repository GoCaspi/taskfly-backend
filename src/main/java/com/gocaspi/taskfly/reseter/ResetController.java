package com.gocaspi.taskfly.reseter;

import com.gocaspi.taskfly.task.TaskRepository;
import com.gocaspi.taskfly.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequestMapping("/reset")
public class ResetController {
    @Autowired
    private ResetRepository repository;
    private final ResetService service;

    public ResetController (ResetRepository repository){
        super();
        this.repository = repository;
        this.service = new ResetService(repository);
    }

    /**
     * returns the service  of type ResetService
     *
     * @return ResetService that is injected in the Controller
     */
    public ResetService getService() {
        return this.service;
    }
}
