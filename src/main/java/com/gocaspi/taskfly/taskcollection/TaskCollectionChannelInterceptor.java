package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
@Component
public class TaskCollectionChannelInterceptor implements ChannelInterceptor {
    @Autowired
    TaskCollectionRepository repo;

    /**
     * the constructor takes a TaskCollectionRepository as an input. the repo is needed for checking user access right on a taskcollection.
     * @param repo
     */
    public TaskCollectionChannelInterceptor(TaskCollectionRepository repo){
        this.repo = repo;
    }

    /**
     * overrides preSend from the implemented ChannelInterceptor.
     * this function is being called when a client message is being received and before its handled by the controller.
     * in this function it is being checked if a user is allowed to subscribe to the requested task collection.
     *
     * @param message the websocket message from the client.
     * @param channel the websocket channel which can be used to send messages back to the client
     * @return the newly formed message
     */
    @Override
    public Message<?> preSend (Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (command == StompCommand.SUBSCRIBE){
            String destination = accessor.getDestination();
            if (Objects.isNull(destination)){
                throw new MessagingException("Invalid or missing stomp headers");
            }
            if(destination.startsWith("/collection")){

                List<String> urlList = Arrays.stream(destination.split("/")).toList();
                if (urlList.size() != 3){
                    throw new MessagingException("invalid subscription url. valid: /collection/enter_id_here");
                }
                String collectionID = urlList.get(2);
                UsernamePasswordAuthenticationToken userAuthObj = (UsernamePasswordAuthenticationToken) accessor.getUser();
                if (Objects.isNull(userAuthObj)){
                    throw new MessagingException("Are you currently logged in?");
                }
                User userObj = (User) userAuthObj.getPrincipal();
                if(Boolean.TRUE.equals(repo.hasAccessToCollection(userObj.getId(), collectionID))){
                    return message;
                } else {
                    throw new MessagingException("no access");
                }
            }
            return message;
        }
        return message;
    }
}
