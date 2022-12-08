package com.gocaspi.taskfly;

import org.springframework.data.mongodb.core.messaging.Subscription;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Objects;

public class WebSocketSubscriptionInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        MessageBuilder<?> errorMsg = MessageBuilder.withPayload("Permission Denied");
        errorMsg.setErrorChannel(channel);
        errorMsg.setErrorChannelName("Auth");
        errorMsg.build();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(Objects.isNull(accessor)){
            channel.send(errorMsg);
        }
        StompCommand command = accessor.getCommand();
        if(command.getMessageType() == SimpMessageType.SUBSCRIBE){
            System.out.println(accessor.getDestination());
            if (accessor.getDestination().startsWith("/collection")){

            }
        }
        return message;
    }

}
