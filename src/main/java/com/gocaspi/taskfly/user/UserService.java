package com.gocaspi.taskfly.user;



        import com.google.common.hash.Hashing;
        import com.google.gson.Gson;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpStatus;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.HttpClientErrorException;

        import java.nio.charset.StandardCharsets;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;
        import java.util.Optional;
        @Service
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

    /**
     * returns the UserRepository that was set in the constructor
     *
     * @return UserRepository
     */
    public UserRepository getRepo(){
        return repo;
    }
    /**
     * If there is no user to the provided id then an exception is thrown that the user does not exist else
     * that user will be removed from the mongoDB
     * @param id id of the user
     * @throws HttpClientErrorException throws an exception when the user does not exist
     */
    public void deleteService(String id) throws HttpClientErrorException.NotFound{
        if (!getRepo().existsById(id)){throw exceptionnotFound; }
        getRepo().deleteById(id);
    }
    /**
     * If there is no user to the provided id then an exception is thrown that the user does not exist else
     * the user will be updated to the existing task assigned to the id
     * @param id id of the user
     * @param update the user what should be updated
     * @throws HttpClientErrorException throws an exception when the user does not exist
     */
    public void updateService(String id, User update) throws HttpClientErrorException {
        Optional<User> user = getRepo().findById(id);

        if (!getRepo().existsById(id)) {
            throw exceptionnotFound;
        }
        user.ifPresent(t -> {
           if (update.getEmail() != null) {
                   t.setEmail(hashStr(update.getEmail()));
           }

            if (update.getTeam() != null) {
                t.setTeam(update.getTeam());
            }
            if (update.getEmail() != null) {
                t.setEmail(update.getEmail());
            }
            if (update.getSrole() != null) {
                t.setSrole(update.getSrole());
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

    public boolean validateTaskFields(String jsonPayload){
        var user = jsonToUser(jsonPayload);
        return !Objects.equals(user.getFirstName(), null) && !Objects.equals(user.getLastName(), null) && !Objects.equals(user.getSrole(), null) && !Objects.equals(user.getEmail(), null);
    }
    /**
     * returns a user from a json String
     * @param jsonPayload String
     * @return user
     */
    public User jsonToUser(String jsonPayload){return new Gson().fromJson(jsonPayload, User.class);}
    /**
     * returns a user that are assigned to the provided id. If there are no user assigned to the id then
     * an exception is thrown.
     * @param id of the user
     * @return the user to the provided id
     */
    public User getServicebyid(String id)throws HttpClientErrorException.NotFound{
        if(!getRepo().existsById(id)){ throw exceptionnotFound;}
        var user = getRepo().findById(id);
        if (user.isEmpty()){
            throw exceptionnotFound;
        }
        return user.get();

    }
    /**
     * returns all users. If there are no user assigned the length of the list will be 0
     *
     * @return ArrayList containing the users
     */
    public List<User> getServiceAllUser(){
        List<User> users = getRepo().findAll();
        List<User> usersToId = new ArrayList<>();
        for (User t : users){
                usersToId.add(t);
        }
        return usersToId;
    }
    public User getDetails(String email){
        return repo.findByEmail(email);
    }
    public String getUserRoles(String email){

        return repo.findByEmail(email).getSrole();
    }
            /**
             * throws an error if not all necessary fields of the provided user are assigned. If all fields are validated the
             * user will be saved to the mongodb
             * @param t user to get validated and saved
             * @throws RuntimeException Exception if not all fields are filled
             */
    public void postService(User t) throws HttpClientErrorException {
        if(!validateTaskFields(new Gson().toJson(t))){
            throw exceptionbadRequest;
        }
        getRepo().insert(t);
    }



    public String hashStr(String str)  {

        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();

    }
}


