package com.gocaspi.taskfly.reseter;

import com.gocaspi.taskfly.task.Task;
import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@RestController
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
   //     props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

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

    @PostMapping
    public ResponseEntity<List> handleReset(@RequestBody String body) throws NoSuchAlgorithmException {
        Reset resetRequest = jsonToReset(body);
       String hashMail = resetRequest.hashStr(resetRequest.getEmail());
        if(Objects.equals(resetRequest.getLastName(), "")){
        }
        List users = getService().getUserByEmail(hashMail,resetRequest.getLastName());

        // return only userId in the messagge
        // send email (!!!to the email of the resetRequest.getEmail() !!!) with the userId in the text and a link to the frontend-form
        // next-next step: create new post-handler: posting userId and password and retypedd password to the handler will update the password assigned to the userId if the user to the userId has the field: reseted true

        if(users.size() == 1){
        //    this.sendResetMail(resetRequest.getEmail(), "Password reset for TaskFly","Your Password has been reseted. Please copy your userId : "+users.get(0) +" and follow the link: to assign a new password. ");
            this.sendResetMail("taskfly.info@gmail.com", "Password reset for TaskFly","Your Password has been reseted. Please copy your userId : "+users.get(0) +" and follow the link: to assign a new password. ");
        }



        //
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
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
