package com.gocaspi.taskfly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true)
@Configuration
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/user/userInfo").authenticated()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/user/login").authenticated()
                .antMatchers("/getUserRoles").hasAuthority("ROLE_WRITE")
                .antMatchers("/task/**").permitAll()
                .antMatchers("/tc/**").permitAll()
                .antMatchers("/actuator/**")
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .anonymous().disable();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
