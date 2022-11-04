package com.gocaspi.taskfly.user;

        import com.google.gson.Gson;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.client.HttpClientErrorException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;
        import java.util.Optional;
public class userService {
    @Autowired
    private userRepository repo;
    private final HttpClientErrorException exception_notFound;
    private final HttpClientErrorException exception_badRequest;
    public userService(userRepository repo){
        this.repo = repo ;
        this.exception_notFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND,"not found",null,null,null);
        this.exception_badRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND,"bad payload",null,null,null);
    }
    public userRepository getRepo(){
        return repo;
    }
    public void deleteService(String id) throws HttpClientErrorException.NotFound{
        if (!getRepo().existsById(id)){throw exception_notFound; }
        getRepo().deleteById(id);
    }
    public void updateService(String id, user update) throws HttpClientErrorException {
        Optional<user> user = getRepo().findById(id);

        if (!getRepo().existsById(id)) {
            throw exception_notFound;
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
    public void postService(user t) throws HttpClientErrorException {
        if(!validateTaskFields(new Gson().toJson(t))){
            throw exception_badRequest;
        }
        getRepo().insert(t);
    }
    public boolean validateTaskFields(String jsonPayload){
        user user = jsonToUser(jsonPayload);
        return !Objects.equals(user.getFirstName(), null) && !Objects.equals(user.getLastName(), null) && !Objects.equals(user.getListId(), null) && !Objects.equals(user.getEmail(), null)&& !Objects.equals(user.getTeam(), null);
    }
    public user jsonToUser(String jsonPayload){return new Gson().fromJson(jsonPayload, user.class);}

    public user getServicebyid(String id)throws HttpClientErrorException.NotFound{
        if(!getRepo().existsById(id)){ throw exception_notFound;}
        user user = getRepo().findById(id).get();
        return user;
    }
    public List<user> getServiceAllUser(){
        List<user> users = getRepo().findAll();
        List<user> usersToId = new ArrayList<>();
        for (user t : users){
                usersToId.add(t);
        }
        return usersToId;
    }
}


