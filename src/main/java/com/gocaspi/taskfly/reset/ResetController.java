package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;



@RestController
@CrossOrigin("*")
@ResponseBody
@RequestMapping("/reset")
public class ResetController {
    @Autowired
    private UserRepository repository;
    private final ResetService service;
    private JavaMailSender emailSender;

    public ResetController (UserRepository repository){
        super();
        this.repository = repository;
        this.service = new ResetService(repository);
        this.emailSender = getJavaMailSender();
    }
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.sendgrid.net");
        mailSender.setPort(465);

        mailSender.setUsername("apikey");
        mailSender.setPassword("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");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.checkserveridentity",true);

        return mailSender;
    }

    /**
     * returns the service  of type ResetService
     *
     * @return ResetService that is injected in the Controller
     */
    public ResetService getService() {
        return this.service;
    }
    public class UIdAndPwdBody{
        private String pwd;
        private String userId;
        public UIdAndPwdBody(String pwd, String userId){
            this.userId = userId;
            this.pwd = pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getPwd() {
            return pwd;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }
    }
    @PostMapping("/setNew")
    public ResponseEntity<String> handleSetNewUserPwd(@RequestBody String body){

        UIdAndPwdBody jsonPayload = new Gson().fromJson(body, UIdAndPwdBody.class);

        getService().resetPwdOfUser(jsonPayload.getUserId(), jsonPayload.getPwd());
        return new ResponseEntity<>("healthy",HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<List<User>> handleReset(@RequestBody String body) {
        Reset resetRequest = jsonToReset(body);
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
            //    this.sendResetMail(resetRequest.getEmail(), "Password reset for TaskFly","Your Password has been reseted. Please copy your userId : "+ userId +" and follow the link: to assign a new password. ");
            //  For testing: send email to host: taskfly.info@gmail.com
            this.sendResetMail("taskfly.info@gmail.com", "!Password reset for TaskFly!", "Your Password has been reseted. Please copy your userId : " + userId + " and follow the link: to assign a new password. ");


        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
        }

    }

    public void sendResetMail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("taskfly.info@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * returns a Reset(body) from a Json
     *
     * @param jsonPayload String
     * @return Task fetched from the jsonPayload
     */
    public Reset jsonToReset(String jsonPayload){ return new Gson().fromJson(jsonPayload, Reset.class);}
}
