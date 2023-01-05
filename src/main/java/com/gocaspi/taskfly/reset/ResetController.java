package com.gocaspi.taskfly.reset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for the Reset Controller with the request mapping /reset
 */

@RestController
@ResponseBody
@RequestMapping("/reset")
public class ResetController {
    @Autowired
    private ResetService service;

    @Value("${crossorigin.url}")
    private String frontendUrl;
    @Autowired
    private SpringTemplateEngine templateEngine;
    /**
     * Construtor for the reset controller with a injected UserRepository
     *
     * @param repository UserRepository
     */


    /**
     * setNew endpoint returns a ResponseEntity with the StatusCode of the response to the reset request of the user.
     * @param body String of UIdAndPwdBody
     * @return ResponseEntity
     */
    @PostMapping("/setNew")
    public ResponseEntity<String> handleSetNewUserPwd(@RequestBody ResetNewPassword body){

        service.resetPwdOfUser(body.getToken(), body.getPwd());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * reset endpoint takes a Reset as a string such that it contains the fields lastName and email. The email input gets hashed and the service is called with
     * the hashed mail address and the lastName. The endpoint returns a responseEntity containing the StatusCode of the request. If the input data is empty (one of the fields)
     * Status 400, BadRequest is retuned. If no exsisting user to the provide credentials can be found in the database then Status 404, NotFound is returned. Else Status will be
     * 202,
     * @param resetRequest String of Reset
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<List<User>> handleReset(@Valid @RequestBody Reset resetRequest) throws MessagingException{
        String hashMail = resetRequest.hashStr(resetRequest.getEmail());
        List<User> emptyList = new ArrayList<>();
        List<User> users = new ArrayList<>();
        try {
            users = service.getUserByEmail(hashMail, resetRequest.getLastName());
        }
        catch (HttpClientErrorException ex) { return new ResponseEntity<>(emptyList, ex.getStatusCode()); }
        if(users.size() !=1){ return new ResponseEntity<>(emptyList, HttpStatus.NOT_FOUND); }
        else{
            String userId = users.get(0).getId();

            String token = service.generateResetUserToken(userId);
            String resetUrl = frontendUrl + "/reset?token=" + token;
            Context context = new Context();
            String greetingMessage = "Hallo, " + users.get(0).getFirstName();
            context.setVariable("greetingMessage", greetingMessage);
            context.setVariable("resetUrl", resetUrl);
            var htmlBody = templateEngine.process("resetMail.html", context);
            service.sendResetMail(resetRequest.getEmail(), "!Password reset for TaskFly!", htmlBody);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

    }

    /**
     * this service takes a token as an input and checks within the redis db if it's valid.
     * when its valid true is returned otherwise false will be returned
     * @param token the token which is going to be checked
     * @return returns a boolean whether true when its valid or false if not.
     */
    @GetMapping("/valid/{token}")
    public Object checkTokenValidity(@PathVariable String token){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        Boolean isValid = service.checkTokenValidityService(token);
        rootNode.put("isValid", isValid);
        return rootNode;
    }

}
