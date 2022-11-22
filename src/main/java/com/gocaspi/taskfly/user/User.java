package com.gocaspi.taskfly.user;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Document
public class User implements UserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String team;
    private String listId;
@Id
    private String id;
    private String userId;
    private String srole;


    public User(String firstName, String lastName, String email, String password, String team, String listId, String userId,String srole) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
        this.userId = userId;
        this.srole= srole;
    }
    public String getUserId(){return this.userId;}

    public String getFirstName(){ return this.firstName;}
    public String getLastName(){ return  this.lastName;}
    public String getEmail(){ return this.email;}
    public String getListId(){ return this.listId;}
    public String getTeam(){ return this.team;}
    public String getId() {return this.id;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword(){return this.password;}

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setUserId(String str){ this.userId = str;}
    public void setFirstName(String str){ this.firstName = str;}
    public void setLastName(String str){ this.lastName = str;}
    public void setEmail(String str){ this.email = str;}
    public void setListId(String str){ this.listId = str;}
    public void setTeam(String str){ this.team = str;}
   public void setId(String id){this.id = id;}

    public void setPassword(String password) {
        this.password = password;
    }
    public String getSrole() {
        return srole;
    }

    public void setSrole(String srole) {
        this.srole = srole;
    }


}