package com.gocaspi.taskfly.reset;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ResetService {
    @Autowired
    private UserRepository repo;
    private final HttpClientErrorException exceptionNotFound;
    private final HttpClientErrorException exceptionBadRequest;
    public ResetService (UserRepository repo){
        this.repo = repo;
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

    public Optional<User> resetPwdOfUser(String userId, String newPwd){
        var user = getRepo().findById(userId);

        if (!getRepo().existsById(userId)) {
            throw exceptionNotFound;
        }

        user.ifPresent(t -> {
            if (t.getReseted()){
              t.setPassword(newPwd);
               // t.setPassword(hashStr(newPwd));
              t.setReseted(false);
              getRepo().save(t);
            }
        });
        return user;
    }

    public List<User> getUserByEmail(String hashMail, String lastName) throws HttpClientErrorException{
        List<User> users = getRepo().findUserByEmail(hashMail);
        if (users.isEmpty()){
            throw this.exceptionNotFound;
        }
        if(!Objects.equals(users.get(0).getLastName(), lastName)){
            throw  this.exceptionBadRequest;
        }

        EnablePwdReset(users.get(0).getId(),true);

        return users;
    }

    public void EnablePwdReset(String id, Boolean status) throws HttpClientErrorException {
        Optional<User> user = getRepo().findById(id);

        if (!getRepo().existsById(id)) {
            throw this.exceptionNotFound;
        }
        user.ifPresent(t -> {

            if(!status){
                t.setReseted(false);
            }
            if(status){
                t.setReseted(true);
            }
            getRepo().save(t);
        });

    }
    public String hashStr(String str)  {
        String sha256hex = Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();

        return  sha256hex;

    }


}