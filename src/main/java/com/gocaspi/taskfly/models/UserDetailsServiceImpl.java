package com.gocaspi.taskfly.models;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found with username:"+username));

        return UserDetailsImpl.build(user);
    }


}
