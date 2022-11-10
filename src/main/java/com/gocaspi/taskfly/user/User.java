package com.gocaspi.taskfly.user;



import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@EntityScan
public class User implements UserDetails {


    @Id
    @GeneratedValue
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String team;
    private String listId;

    private AppUserRole appUserRole;
    private Boolean locked;
    private Boolean enabled;


    private String id;
    private String userId;


    public User(String firstName, String lastName, String email, String password, String team, String listId, String userId,Boolean locked,Boolean enabled) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.listId = listId;
        this.team = team;
        this.locked=locked;
        this.enabled=enabled;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        SimpleGrantedAuthority authority =new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }
    public String getUserId(){
        return this.userId;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return  this.lastName;
    }
    public String getEmail(){
        return this.email;
    }
    public String getListId(){
        return this.listId;
    }
    public String getTeam(){
        return this.team;
    }
    public String getId() {
        return this.id;
    }
    public String getPassword(){
        return this.password;
    }
    public Boolean getEnabled(){
        return this.enabled;
    }
    public Boolean getLocked(){
        return this.locked;
    }

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

    public void setUserId(String str){
        this.userId = str;
    }
    public void setFirstName(String str){ this.firstName = str;}
    public void setLastName(String str){ this.lastName = str;}
    public void setEmail(String str){ this.email = str;}
    public void setListId(String str){ this.listId = str;}
    public void setTeam(String str){ this.team = str;}
   public void setId(String id){this.id = id;}

    public void setPassword(String password) {
        this.password = password;
    }



}