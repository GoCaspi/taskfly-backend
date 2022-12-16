package com.gocaspi.taskfly.auth;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);
    private UserRepository repository;
    private PasswordEncoder encoder;
    @Value("${spring.security.user.name}")
    private String defaultUsername;
    @Value("${spring.security.user.password}")
    private String defaultPassword;
    @Value("${spring.security.user.roles}")
    private String defaultRole;
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

        if (authentication.getCredentials() == null){
            throw new BadCredentialsException("Details not found");
        }

        if (authentication.getName().equals(defaultUsername) && authentication.getCredentials().toString().equals(defaultPassword)){
            return new UsernamePasswordAuthenticationToken(defaultUsername,defaultPassword, getUserRoles(defaultRole));
        }

        String email = hashStr(authentication.getName());
        String password = authentication.getCredentials().toString();


        User user = repository.findByEmail(email);
        if (user == null){
            throw new BadCredentialsException("Details not found");
        }
        if (encoder.matches(password,user.getPassword())){
            logger.info("Successfully Authenticated the User");
            return new UsernamePasswordAuthenticationToken(user,password, getUserRoles(user.getSrole()));
        }else{
            throw new BadCredentialsException("Password mismatch");
        }
    }
    /**
     * User can have more than roles separated by ",". We are splitting each role separately(ROLE_WRITE/ROLE_READ)
     * @param userRoles
     * @return
     */
     public  List<GrantedAuthority> getUserRoles(String userRoles){

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
    public String hashStr(String str)  {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }
}
