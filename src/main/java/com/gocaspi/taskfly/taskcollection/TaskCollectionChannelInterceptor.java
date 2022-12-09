package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.user.User;
import com.gocaspi.taskfly.user.UserPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TaskCollectionChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend (Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (!Objects.isNull(accessor)){
            StompCommand command = accessor.getCommand();
            SimpMessageHeaderAccessor fakeHeader = StompHeaderAccessor.create(SimpMessageType.OTHER);
            fakeHeader.setSessionId(accessor.getSessionId());
            Message fakeMsg = MessageBuilder.withPayload("Successful").setHeaders(fakeHeader).build();
            if (command == StompCommand.SUBSCRIBE ){
                if (accessor.getDestination().startsWith("/collection")){
                    String collectionID = Arrays.stream(accessor.getDestination().split("/")).toList().get(2);
                    UsernamePasswordAuthenticationToken userAuthObj = (UsernamePasswordAuthenticationToken) accessor.getUser();
                    User userObj = (User) userAuthObj.getPrincipal();
                    System.out.println(userObj.getId());
                    userObj.
                    throw new MessagingException("invalid user");
                }

            }
            return message;
        }
        return message;
    }
}
