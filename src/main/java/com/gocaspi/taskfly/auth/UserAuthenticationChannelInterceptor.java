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

    /**
     * the constructor takes an authenticationProvider as an input which is required to log a user in using springs auth system.
     *
     * @param authManager the authenticationProvider used in this spring project.
     */

    public UserAuthenticationChannelInterceptor(AuthenticationProvider authManager){
        this.authManager = authManager;
    }

    /**
     * this interceptor intercepts all connect messages and extracts a username and password
     * from the stomp headers and authenticates the connection. otherwise the request gets rejected.
     *
     * @param message the websocket message from the client.
     * @param channel the websocket channel which can be used to send messages back to the client
     * @return the original message with the user object appended to it for later authentication
     */
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

    /**
     * securely extracts the username from the stomp headers
     * @param accessor the stomp headers accessor created from the message
     * @return the extracted username
     */
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
    /**
     * securely extracts the password from the stomp headers
     * @param accessor the stomp headers accessor created from the message
     * @return the extracted password
     */
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
