package com.gocaspi.taskfly.auth;

import com.gocaspi.taskfly.security.UserAuthentificationProvider;
import com.gocaspi.taskfly.user.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags ="Authentication")
@RestController
@RequestMapping(path ="api/public")
public class AuthUser {
    private final AuthenticationManager  authenticationManager;
    private final JwtTokenFilter jwtTokenFilter;
    private final UserDetailsImpl userDetails;

    public AuthUser(AuthenticationManager authenticationManager,
                   JwtTokenFilter jwtTokenFilter,
                   UserDetailsImpl userDetails) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenFilter = jwtTokenFilter;
        this.userDetails = userDetails;
    }

    public AuthUser(AuthenticationManager authenticationManager, JwtTokenFilter jwtTokenFilter, UserDetailsImpl userDetails) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenFilter = jwtTokenFilter;
        this.userDetails = userDetails;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenFilter.generateAccessToken(user)
                    )
                    .body(userDetails.toUserView(user));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
