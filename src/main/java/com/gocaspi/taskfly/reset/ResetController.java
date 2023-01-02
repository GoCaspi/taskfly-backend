package com.gocaspi.taskfly.reset;

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
    private final ResetService service;
    @Autowired
    private JavaMailSender emailSender;
    @Value("${mail.username}")
    private String emailUsername;
    @Value("${mail.password}")
    private String emailPassword;
    @Value("${mail.host}")
    private String emailHost;
    @Value("${mail.port}")
    private int emailPort;
    @Autowired
    private SpringTemplateEngine templateEngine;
    /**
     * Construtor for the reset controller with a injected UserRepository
     *
     * @param repository UserRepository
     */
    public ResetController (UserRepository repository, JavaMailSender javaMailSender){
        super();
        this.service = new ResetService(repository);
        this.emailSender = javaMailSender;
    }

    /**
     * returns the service  of type ResetService
     *
     * @return ResetService that is injected in the Controller
     */
    public ResetService getService() {
        return this.service;
    }

    /**
     * Helper class UIdAndPwdBody contains the userId and password of a userinput to /reset.
     */
    public class UIdAndPwdBody{
        private String pwd;
        private String userId;

        /**
         * Constructor for UIdAndPwdBody
         *
         * @param pwd String, password input
         * @param userId String, userId input
         */
        public UIdAndPwdBody(String pwd, String userId){
            this.userId = userId;
            this.pwd = pwd;
        }

        /**
         * Sets the field pwd of the UIdAndPwdBody class to the input value
         *
         * @param pwd String, password input
         */
        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        /**
         * Returns the value of the field pwd of the UIdAndPwdBody class
         *
         * @return password String
         */
        public String getPwd() {
            return pwd;
        }

        /**
         * Sets the field userId of the UIdAndPwdBody class to the input value
         *
         * @param userId String
         */
        public void setUserId(String userId) {
            this.userId = userId;
        }

        /**
         * Returns the value of the field userId of the UIdAndPwdBody class
         *
         * @return
         */
        public String getUserId() {
            return userId;
        }
    }

    /**
     * setNew endpoint returns a ResponseEntity with the StatusCode of the response to the reset request of the user.
     * @param body String of UIdAndPwdBody
     * @return ResponseEntity
     */
    @PostMapping("/setNew")
    public ResponseEntity<String> handleSetNewUserPwd(@RequestBody String body){

        UIdAndPwdBody jsonPayload = new Gson().fromJson(body, UIdAndPwdBody.class);

        getService().resetPwdOfUser(jsonPayload.getUserId(), jsonPayload.getPwd());
        return new ResponseEntity<>("healthy",HttpStatus.OK);
    }

    /**
     * reset endpoint takes a Reset as a string such that it contains the fields lastName and email. The email input gets hashed and the service is called with
     * the hashed mail address and the lastName. The endpoint returns a responseEntity containing the StatusCode of the request. If the input data is empty (one of the fields)
     * Status 400, BadRequest is retuned. If no exsisting user to the provide credentials can be found in the database then Status 404, NotFound is returned. Else Status will be
     * 202,
     * @param body String of Reset
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<List<User>> handleReset(@RequestBody Reset resetRequest) throws MessagingException{
        String hashMail = resetRequest.hashStr(resetRequest.getEmail());
        List<User> emptyList = new ArrayList<>();
        if (Objects.equals(resetRequest.getLastName(), "")) {
            return new ResponseEntity<>(emptyList, HttpStatus.BAD_REQUEST);
        }
        List<User> users = new ArrayList<>();
        try {
            users = getService().getUserByEmail(hashMail, resetRequest.getLastName());
        }
        catch (HttpClientErrorException ex) { return new ResponseEntity<>(emptyList, ex.getStatusCode()); }
        if(users.size() !=1){ return new ResponseEntity<>(emptyList, HttpStatus.NOT_FOUND); }
        else{
            String userId = users.get(0).getId();
            //  For testing: send email to host: taskfly.info@gmail.com
            Context context = new Context();
            String greetingMessage = "Hallo, " + users.get(0).getFirstName();
            context.setVariable("greetingMessage", greetingMessage);
            var htmlBody = templateEngine.process("resetMail.html", context);
            this.sendResetMail(resetRequest.getEmail(), "!Password reset for TaskFly!", htmlBody);


        return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

    }

    /**
     * takes three Strings  as input and uses the injected emailSender to send a email from the taskFly gmail account
     * @param to String, reciever email address
     * @param subject String, topic of the message
     * @param text String, text of the message
     */
    public void sendResetMail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setFrom("wok.gocaspi@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text, true);
        this.emailSender.send(mimeMessage);
    }

    /**
     * returns a Reset(body) from a Json
     *
     * @param jsonPayload String
     * @return Task fetched from the jsonPayload
     */
    public Reset jsonToReset(String jsonPayload){ return new Gson().fromJson(jsonPayload, Reset.class);}
}
