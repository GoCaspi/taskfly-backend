package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Class of the ResetService, it provides the funtionilty to interact with the mongo database
 */
@Service
public class ResetService {
    @Autowired
    private UserRepository repo;
    private final HttpClientErrorException exceptionNotFound;
    private final HttpClientErrorException exceptionBadRequest;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JavaMailSender emailSender;

    /**
     * Constructor for the ResetService, it takes an UserRepository
     * @param repo UserRpository
     */
    public ResetService (UserRepository repo, JavaMailSender javaMailSender, RedisTemplate<String, String> redisTemplate){
        this.repo = repo;
        this.emailSender = javaMailSender;
        this.redisTemplate = redisTemplate;
        this.encoder = new BCryptPasswordEncoder();
        this.exceptionNotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionBadRequest = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }

    /**
     * returns the TaskRepository that was set in the constructor
     *
     * @return TaskRepository
     */
    public UserRepository getRepo() {
        return repo;
    }

    /**
     * the method checks if the user to the provided userId exists. If not it throws NotFound. Else checks if the field reseted of the user found
     * to that userId is true. If so it sets the input password to the password field of the user.
     * @param token String, userId of the user who wants to reset the password
     * @param newPwd String
     * @return Optional User if the user was found
     */
    public Optional<User> resetPwdOfUser(String token, String newPwd){
        String userId = redisTemplate.opsForValue().get(token);
        if (Objects.isNull(userId)){
            throw exceptionNotFound;
        }

        var user = getRepo().findById(userId);

        user.ifPresent(t -> {
            if (t.getReseted()){
              t.setPassword(encoder.encode(newPwd));
              t.setReseted(false);
              getRepo().save(t);
              redisTemplate.opsForValue().getAndDelete(token);
            }
        });
        return user;
    }

    /**
     * takes a hashed email-string and a lastName. If no user was found to the hashed mail in the database it returns NotFound exception. If the lastName is not filled, then
     * Status 400 BadRequest is Returned. Eslse the method sets the reseted field of the user to that email to true
     * @param hashMail, String hashed email address
     * @param lastName, String
     * @return User
     * @throws HttpClientErrorException, Status Code
     */
    public List<User> getUserByEmail(String hashMail, String lastName) throws HttpClientErrorException{
        List<User> users = getRepo().findUserByEmail(hashMail);
        if (users.isEmpty()){
            throw this.exceptionNotFound;
        }
        if(!Objects.equals(users.get(0).getLastName(), lastName)){
            throw  this.exceptionBadRequest;
        }

        enablePwdReset(users.get(0).getId(),true);

        return users;
    }

    /**
     *
     * @param id
     * @param status
     * @throws HttpClientErrorException
     */
    public void enablePwdReset(String id, Boolean status) throws HttpClientErrorException {
        Optional<User> user = getRepo().findById(id);

        if (user.isEmpty()){
            throw exceptionNotFound;
        }
        user.ifPresent(t -> {

            if(Boolean.FALSE.equals(status)){
                t.setReseted(false);
            }
            if(Boolean.TRUE.equals(status)){
                t.setReseted(true);
            }
            getRepo().save(t);

        });

    }


    public String generateResetUserToken(String userID){
        String token = UUID.randomUUID().toString();
        Duration timeout = Duration.ofHours(1);
        redisTemplate.opsForValue().set(token, userID, timeout);
        return token;
    }

    public Boolean checkTokenValidityService(String token){
        String userID = redisTemplate.opsForValue().get(token);
        return !Objects.isNull(userID);
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


}
