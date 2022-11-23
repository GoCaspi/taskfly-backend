package com.gocaspi.taskfly.auth;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);
    private UserRepository repository;
    private PasswordEncoder encoder;

 public UserAuthenticationProvider(UserRepository repository,PasswordEncoder encoder){
     this.encoder = encoder;
     this.repository = repository;
 }

    /**
     * Get the email and password from authentication object and validate with password encoders matching method
     * @param authentication
     * @return
     * @throws AuthenticationException
     */

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = repository.findByEmail(email);
        if (user == null){
            throw new BadCredentialsException("Details not found");
        }
        if (encoder.matches(password,user.getPassword())){
            logger.info("Successfully Authenticated the User");
            return new UsernamePasswordAuthenticationToken(email,password, getUserRoles(user.getSrole()));
        }else{
            throw new BadCredentialsException("Password mismatch");
        }
    }
     private List<GrantedAuthority> getUserRoles(String userRoles){
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String[] roles = userRoles.split(",");
        for (String role : roles){
            logger.info("Role:"+ role);
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.replaceAll("\\s+","")));
        }
        return grantedAuthorityList;
     }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
