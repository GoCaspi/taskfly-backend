package com.gocaspi.taskfly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Class for SecurityConfiguration
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true)
@Configuration
public class SecurityConfiguration {

    /**
     * “/userInfo” – can be access by any user who has successfully authenticated.
     * “/getUserRoles” – can be accessed by the user who has role/authority – “ROLE_WRITE”
     * @param http HttpSecurity
     * @return http.build
     * @throws Exception Error Massage
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors().and().anonymous().and().csrf().disable().authorizeRequests()
                .antMatchers("/user/userInfo").authenticated()
                .antMatchers("/teammanagement/**").permitAll()
                .antMatchers("/user/login").authenticated()
                .antMatchers("/user/getUserRoles").hasAuthority("ROLE_WRITE")
                .antMatchers("/task/**").permitAll()
                .antMatchers("/tc/**").permitAll()
                .antMatchers("/actuator/**")
                .authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    /**
     * In our example we are going to use BCryptPasswordEncoder to encode the password and save it in database.
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
