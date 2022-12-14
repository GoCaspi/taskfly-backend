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
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
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
        String usernameList = Objects.requireNonNull(accessor.getNativeHeader("username")).get(0);
        if(usernameList.isBlank()){
            throw new MessagingException("username is empty");
        }
        return usernameList;
    }

    String extractPassword(StompHeaderAccessor accessor){
        if(Objects.isNull(accessor.getNativeHeader("password"))){
            throw new MessagingException("password is not defined");
        }

        String passwordList = Objects.requireNonNull(accessor.getNativeHeader("password")).get(0);
        if(passwordList.isBlank()){
            throw new MessagingException("password is empty");
        }
        return passwordList;
    }
}
