package com.gocaspi.taskfly.auth;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.common.hash.Hashing;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/**
 * Class for UserAuthenticationProvider
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);
    private UserRepository repository;
    private PasswordEncoder encoder;
    /**
     * Constractor for UserController
     *
     * @param repository variable for the interface userRepository
     * @param encoder variable for the interface PasswordEncoder
     */
 public UserAuthenticationProvider(UserRepository repository,PasswordEncoder encoder){
     this.encoder = encoder;
     this.repository = repository;
 }
    /**
     * Get the email and password from authentication object and validate with password encoders matching method
     * @param authentication of the user
     * @return various error messages
     * @throws AuthenticationException error messages
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication.getCredentials() == null){
            throw new BadCredentialsException("Details not found");
        }
        String email = hashStr(authentication.getName());
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
    /**
     * User can have more than roles separated by ",". We are splitting each role separately(ROLE_WRITE/ROLE_READ)
     * @param userRoles the role of the user
     * @return returns the role from the user
     */
     public  List<GrantedAuthority> getUserRoles(String userRoles){

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String[] roles = userRoles.split(",");
        for (String role : roles){
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.replaceAll("\\s+","")));
        }
        return grantedAuthorityList;
     }

    /**
     * compares two tokens with each other
     *
     * @param authentication autentication
     * @return true or false
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     *
     *
     * @param str haststring
     * @return hashcode
     */
    public String hashStr(String str)  {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }
}
