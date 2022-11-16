package com.gocaspi.taskfly.security;

import com.gocaspi.taskfly.models.UserDetailsImpl;
import com.gocaspi.taskfly.models.UserDetailsServiceImpl;
import com.gocaspi.taskfly.security.jwt.AuthEntryPointJwt;
import com.gocaspi.taskfly.security.jwt.AuthTokenFilter;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        //securedEnabled = true ,
        // jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwttokenFilter(){
        return new AuthTokenFilter();
    }

    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
   @Override
   protected void  configure(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().disable().authorizeRequests()
                .antMatchers("/user/getUserRoles").hasAuthority("ROLE_WRITE")
                .antMatchers("/user/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/task/**").authenticated()
                .antMatchers("/actuator/**")
                .authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(authenticationJwttokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter();
    }
}
