package com.gocaspi.taskfly.user;

        import com.google.gson.Gson;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.client.HttpClientErrorException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;
        import java.util.Optional;
public class UserService {

    @Autowired
    private UserRepository repo;
    private final HttpClientErrorException exceptionnotFound;
    private final HttpClientErrorException exceptionbadRequest;
    public UserService(UserRepository repo){

        this.repo = repo ;
        this.exceptionnotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionbadRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }

    public HttpClientErrorException getNotFound() {

        return this.exceptionbadRequest;
    }

    public UserRepository getRepo(){
        return repo;
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
            if (update.getTeam() != null) {
                t.setTeam(update.getTeam());
            }
            if (update.getPassword() != null) {
                t.setPassword(update.getPassword());
            }
            if (update.getListId() != null) {
                t.setListId(update.getListId());
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
    public void postService(User t) throws HttpClientErrorException {
        if(!validateTaskFields(new Gson().toJson(t))){
            throw exceptionbadRequest;
        }
        getRepo().insert(t);
    }
    public boolean validateTaskFields(String jsonPayload){
        User user = jsonToUser(jsonPayload);
        return !Objects.equals(user.getFirstName(), null) && !Objects.equals(user.getLastName(), null) && !Objects.equals(user.getListId(), null) && !Objects.equals(user.getEmail(), null)&& !Objects.equals(user.getTeam(), null);
    }
    public User jsonToUser(String jsonPayload){return new Gson().fromJson(jsonPayload, User.class);}

    public User getServicebyid(String id)throws HttpClientErrorException.NotFound{
        if(!getRepo().existsById(id)){ throw exceptionnotFound;}
        return getRepo().findById(id).isPresent() ? getRepo().findById(id).get() : new User("","","","","","","");

    }
    public List<User> getServiceAllUser(){
        List<User> users = getRepo().findAll();
        List<User> usersToId = new ArrayList<>();
        for (User t : users){
                usersToId.add(t);
        }
        return usersToId;
    }
}


