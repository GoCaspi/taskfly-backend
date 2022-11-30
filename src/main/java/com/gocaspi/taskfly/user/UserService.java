package com.gocaspi.taskfly.user;

        import com.google.gson.Gson;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpStatus;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.HttpClientErrorException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;
        import java.util.Optional;
        @Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final HttpClientErrorException exceptionnotFound;
    private final HttpClientErrorException exceptionbadRequest;

    public UserService(UserRepository repo){

        this.userRepository = repo ;
        this.exceptionnotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionbadRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }

    public HttpClientErrorException getNotFound() {

        return this.exceptionbadRequest;
    }

    public UserRepository getRepo(){
        return userRepository;
    }
    public void deleteService(String id) throws HttpClientErrorException.NotFound{
        if (!getRepo().existsById(id)){throw exceptionnotFound; }
        getRepo().deleteById(id);
    }
    public void updateService(String id, User update) throws HttpClientErrorException {
        Optional<User> user = getRepo().findById(id);

        if (!getRepo().existsById(id)) {
            throw exceptionnotFound;
        }
        user.ifPresent(t -> {
            if (update.getEmail() != null) {
                t.setEmail(update.getEmail());
            }
            if (update.getSrole() != null) {
                t.setSrole(update.getSrole());
            }
            if (update.getPassword() != null) {
                t.setPassword(update.getPassword());
            }
            if (update.getFirstName() != null) {
                t.setFirstName(update.getFirstName());
            }
            if (update.getLastName() != null) {
                t.setLastName(update.getLastName());
            }
            getRepo().save(t);

        });
    }

    public boolean validateTaskFields(String jsonPayload){
        var user = jsonToUser(jsonPayload);
        return !Objects.equals(user.getFirstName(), null) && !Objects.equals(user.getLastName(), null) && !Objects.equals(user.getSrole(), null) && !Objects.equals(user.getEmail(), null);
    }
    public User jsonToUser(String jsonPayload){return new Gson().fromJson(jsonPayload, User.class);}

    public User getServicebyid(String id)throws HttpClientErrorException.NotFound{
        if(!getRepo().existsById(id)){ throw exceptionnotFound;}
        var user = getRepo().findById(id);
        if (user.isEmpty()){
            throw exceptionnotFound;
        }
        return user.get();

    }
    public List<User> getServiceAllUser(){
        List<User> users = getRepo().findAll();
        List<User> usersToId = new ArrayList<>();
        for (User t : users){
                usersToId.add(t);
        }
        return usersToId;
    }
    public User getDetails(String email){
        return userRepository.findByEmail(email);
    }
    public String getUserRoles(String email){

        return userRepository.findByEmail(email).getSrole();
    }
    public void postService(User t) throws HttpClientErrorException {
        if(!validateTaskFields(new Gson().toJson(t))){
            throw exceptionbadRequest;
        }
        getRepo().insert(t);
    }

}


