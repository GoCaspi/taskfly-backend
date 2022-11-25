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
/**
 * Class for UserService
 */
public class UserService {

    @Autowired
    private UserRepository repo;
    private final HttpClientErrorException exceptionnotFound;
    private final HttpClientErrorException exceptionbadRequest;
    /**
     * Constractor for UserService
     *
     * @param repo variable for the interface userRepository
     */
    public UserService(UserRepository repo){

        this.repo = repo ;
        this.exceptionnotFound = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", new HttpHeaders(), "".getBytes(),null);
        this.exceptionbadRequest = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "bad payload", new HttpHeaders(), "".getBytes(), null);
    }
    /**
     * returns the HttpClientErrorException
     *
     * @return exceptionNotFound
     */
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
    /**
     * given a requestbody (Json of a user) the method checks if all fields are null-safe.
     * @param jsonPayload, request body
     * @return true if the mentioned criteria holds for that user-payload, else return false
     */
    public boolean validateTaskFields(String jsonPayload){
        var user = jsonToUser(jsonPayload);
        return !Objects.equals(user.getFirstName(), null) && !Objects.equals(user.getLastName(), null) && !Objects.equals(user.getListId(), null) && !Objects.equals(user.getEmail(), null)&& !Objects.equals(user.getTeam(), null);
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
}


