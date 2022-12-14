package com.gocaspi.taskfly.auth;

import com.gocaspi.taskfly.auth.UserAuthenticationChannelInterceptor;
import com.gocaspi.taskfly.taskcollection.TaskCollectionRepository;
import com.gocaspi.taskfly.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



class UserAuthenticationChannelInterceptorTest {
    final private AuthenticationProvider authManagerMock = mock(AuthenticationProvider.class);
    final private User TestUser1 = new User("Peter", "Schmidt", "test123@gmail.com", "test123", "", null, false);
    @Test
    void TestPreSend(){
        QueueChannel channel = new QueueChannel();
        UserAuthenticationChannelInterceptor interceptor = new UserAuthenticationChannelInterceptor(authManagerMock);
        class Testcase {
            final Message<?> mockMessage;
            final QueueChannel mockChannel;
            final String userEmail;
            final String userPassword;
            final UsernamePasswordAuthenticationToken userAuthReqMock;
            final User mockUser;
            final Boolean exception;
            final String exceptionMessage;

            public Testcase(Message<?> mockMessage, QueueChannel mockChannel, User mockUser, String userEmail, String userPassword, Boolean exception, String exceptionMessage) {
                this.mockMessage = mockMessage;
                this.mockChannel = mockChannel;
                this.mockUser = mockUser;
                this.userEmail = userEmail;
                this.userPassword = userPassword;
                this.exception = exception;
                this.exceptionMessage = exceptionMessage;
                this.userAuthReqMock = new UsernamePasswordAuthenticationToken(this.mockUser, this.userPassword);;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase(buildValidConnectionMessage("test123@gmail.com", "test123"), channel, TestUser1, "test", "1234", false, ""),
                new Testcase(buildInvalidConnectionMessageWithNoCredentials(), channel, TestUser1, "", "", true, "username is not defined"),
                new Testcase(buildInvalidConnectionMessageWithNoPassword("test123@gmail.com"), channel, TestUser1, "test123@gmail.com", "", true, "password is not defined"),
                new Testcase(buildInvalidConnectionMessageWithInvalidHeaderAccessor(), channel, TestUser1, "", "", true, "No Stomp Headers available"),
                new Testcase(buildValidConnectionMessageWithDifferentCommand(), channel, TestUser1, "", "", false, ""),
                new Testcase(buildInvalidConnectionMessageWithEmptyPasswordHeader("test123@gmail.com"), channel, TestUser1, "", "", true, "password is empty"),
                new Testcase(buildInvalidConnectionMessageWithEmptyUsernameHeader("test123"), channel, TestUser1, "", "", true, "username is empty")

        };
        for (Testcase tc: testcases){
            if(!Objects.isNull(tc.userAuthReqMock)){
                when(authManagerMock.authenticate(tc.userAuthReqMock)).thenReturn(tc.userAuthReqMock);
            }
            if(tc.exception){
                Exception exception = assertThrows(MessagingException.class, () -> {
                    interceptor.preSend(tc.mockMessage, tc.mockChannel);
                });
                assertEquals(tc.exceptionMessage, exception.getMessage());
            } else {
                Message<?> actual = interceptor.preSend(tc.mockMessage, tc.mockChannel);
                assertEquals(tc.mockMessage, actual);
            }

        }
    }
    Message<?> buildValidConnectionMessage(String email, String password){
        StompHeaderAccessor tempStompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);

        tempStompHeaderAccessor.setNativeHeader("username", email);
        tempStompHeaderAccessor.setNativeHeader("password", password);
        MessageHeaderAccessor nativeHeaderAccessor = new MessageHeaderAccessor();
        MessageHeaders nativeHeaders = tempStompHeaderAccessor.getMessageHeaders();
        nativeHeaderAccessor.copyHeaders(tempStompHeaderAccessor.getMessageHeaders());
        nativeHeaderAccessor.setLeaveMutable(true);
        return MessageBuilder.createMessage("", nativeHeaders);
    }
    Message<?> buildInvalidConnectionMessageWithNoCredentials(){
        StompHeaderAccessor tempStompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);

        MessageHeaderAccessor nativeHeaderAccessor = new MessageHeaderAccessor();
        MessageHeaders nativeHeaders = tempStompHeaderAccessor.getMessageHeaders();
        nativeHeaderAccessor.copyHeaders(tempStompHeaderAccessor.getMessageHeaders());
        nativeHeaderAccessor.setLeaveMutable(true);
        return MessageBuilder.createMessage("", nativeHeaders);
    }
    Message<?> buildInvalidConnectionMessageWithNoPassword(String email){
        StompHeaderAccessor tempStompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);

        tempStompHeaderAccessor.setNativeHeader("username", email);
        MessageHeaderAccessor nativeHeaderAccessor = new MessageHeaderAccessor();
        MessageHeaders nativeHeaders = tempStompHeaderAccessor.getMessageHeaders();
        nativeHeaderAccessor.copyHeaders(tempStompHeaderAccessor.getMessageHeaders());
        nativeHeaderAccessor.setLeaveMutable(true);
        return MessageBuilder.createMessage("", nativeHeaders);
    }

    Message<?> buildInvalidConnectionMessageWithInvalidHeaderAccessor(){
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.CONNECT);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload("Test123");
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }

    Message<?> buildValidConnectionMessageWithDifferentCommand(){
        StompHeaderAccessor tempStompHeaderAccessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        MessageHeaderAccessor nativeHeaderAccessor = new MessageHeaderAccessor();
        MessageHeaders nativeHeaders = tempStompHeaderAccessor.getMessageHeaders();
        nativeHeaderAccessor.copyHeaders(tempStompHeaderAccessor.getMessageHeaders());
        nativeHeaderAccessor.setLeaveMutable(true);
        return MessageBuilder.createMessage("", nativeHeaders);
    }

    Message<?> buildInvalidConnectionMessageWithEmptyPasswordHeader(String email){
        StompHeaderAccessor tempStompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        tempStompHeaderAccessor.setNativeHeader("username", email);
        tempStompHeaderAccessor.setNativeHeader("password", "");
        MessageHeaderAccessor nativeHeaderAccessor = new MessageHeaderAccessor();
        MessageHeaders nativeHeaders = tempStompHeaderAccessor.getMessageHeaders();
        nativeHeaderAccessor.copyHeaders(tempStompHeaderAccessor.getMessageHeaders());
        nativeHeaderAccessor.setLeaveMutable(true);
        return MessageBuilder.createMessage("", nativeHeaders);
    }

    Message<?> buildInvalidConnectionMessageWithEmptyUsernameHeader(String password){
        StompHeaderAccessor tempStompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        tempStompHeaderAccessor.setNativeHeader("username", "");
        tempStompHeaderAccessor.setNativeHeader("password", password);
        MessageHeaderAccessor nativeHeaderAccessor = new MessageHeaderAccessor();
        MessageHeaders nativeHeaders = tempStompHeaderAccessor.getMessageHeaders();
        nativeHeaderAccessor.copyHeaders(tempStompHeaderAccessor.getMessageHeaders());
        nativeHeaderAccessor.setLeaveMutable(true);
        return MessageBuilder.createMessage("", nativeHeaders);
    }

}