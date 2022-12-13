package com.gocaspi.taskfly.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Objects;
@Configuration
public class UserAuthenticationChannelInterceptor implements ChannelInterceptor {

    @Autowired
    public AuthenticationProvider authManager;

    public UserAuthenticationChannelInterceptor(AuthenticationProvider authManager){
        this.authManager = authManager;
    }
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (Objects.isNull(accessor)){
            throw new MessagingException("No Stomp Headers available");
        }
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String username = extractUsername(accessor);
            String password = extractPassword(accessor);
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
            Authentication user = authManager.authenticate(authReq);
            accessor.setUser(user);
        }
        return message;
    }

    String extractUsername(StompHeaderAccessor accessor){
        if(Objects.isNull(accessor.getNativeHeader("username"))){
            throw new MessagingException("username is not defined");
        }
        List<String> usernameList = accessor.getNativeHeader("username");
        if (Objects.isNull(usernameList)){
            throw new MessagingException("username list couldn't be initialized");
        }
        if (usernameList.isEmpty()) {
            throw new MessagingException("username is not defined");
        }
        return usernameList.get(0);
    }

    String extractPassword(StompHeaderAccessor accessor){
        if(Objects.isNull(accessor.getNativeHeader("password"))){
            throw new MessagingException("password is not defined");
        }
        List<String> passwordList = accessor.getNativeHeader("password");
        if (Objects.isNull(passwordList)){
            throw new MessagingException("password list couldn't be initialized");
        }
        if (passwordList.isEmpty()) {
            throw new MessagingException("password is not defined");
        }
        return passwordList.get(0);
    }
}