package com.gocaspi.taskfly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
/**
 * Class for SecurityConfiguration
 */
@Configuration
public class SecurityConfiguration {
    /**
     * commit access to the endpoint user, task, tc and actuator
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/task/**").permitAll()
                .antMatchers("/reset/**").permitAll()
                .antMatchers("/tc/**").permitAll()
                .antMatchers("/actuator/**")
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }


}
