package com.gocaspi.taskfly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().disable().authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/task/**").permitAll()
                .antMatchers("/tc/**").permitAll()
                .antMatchers("/actuator/**")
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}
