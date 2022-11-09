package registration;

import org.springframework.beans.factory.annotation.Autowired;

public class RegistrationController {
    @Autowired
    private LoginRepository repository;
    private final RegistrationService service;

    public RegistrationController(LoginRepository repository){
        super();
        this.repository=repository;
        this.service=new RegistrationService();

    }
}
