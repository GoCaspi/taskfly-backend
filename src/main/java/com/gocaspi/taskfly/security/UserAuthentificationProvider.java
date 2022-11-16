package com.gocaspi.taskfly.security;

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
import java.util.Optional;

@Component
public class UserAuthentificationProvider implements AuthenticationProvider {
     Logger logger = LoggerFactory.getLogger(UserAuthentificationProvider.class);
     private UserRepository repository;
     private PasswordEncoder encoder;

    /**
     * Get the username and password from authentication object and validate with password encoders matching method
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       String email = authentication.getName();
       String password = authentication.getCredentials().toString();


       Optional <User> user = repository.findByEmail(email);

        if (user == null){

            throw new BadCredentialsException("Details not found");
        }
        if(encoder.matches(password,user.get().getPassword())){
            logger.info("Successfully Authenticated the user");
            return new UsernamePasswordAuthenticationToken(email, password , getUserRoles(user.get().getSrole().toString()));
        }else{
            throw new BadCredentialsException("Password mismatch");
        }
    }

    private List<GrantedAuthority> getUserRoles(String userRoles){
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String[] roles = userRoles.split(",");
        for (String role : roles){
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.replaceAll("\\s+","")));
        }
        return grantedAuthorityList;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
