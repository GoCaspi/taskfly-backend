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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

    /**
     * Constructor for the ResetService, it takes an UserRepository
     * @param repo UserRpository
     */
    public ResetService (UserRepository repo){
        this.repo = repo;
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

        if (!getRepo().existsById(id)) {
            throw this.exceptionNotFound;
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
    /**
     * Hashes a string with the SHA256 algorithm
     *
     * @param str String that should get hashed by SHA256
     * @return hashed String of the input String
     */
    public String hashStr(String str)  {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
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


}
