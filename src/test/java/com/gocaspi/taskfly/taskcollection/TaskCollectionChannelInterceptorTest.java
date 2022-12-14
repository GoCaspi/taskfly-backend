package com.gocaspi.taskfly.taskcollection;

import com.gocaspi.taskfly.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.channels.Channel;
import java.security.Principal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
class TaskCollectionChannelInterceptorTest {
    final private TaskCollectionRepository mockRepo = mock(TaskCollectionRepository.class);
    final private User TestUser1 = new User("Peter", "Schmidt", "test123@gmail.com", "test123", "", null, false);

    @Test
    void TestPreSend(){

        TaskCollectionChannelInterceptor interceptor = new TaskCollectionChannelInterceptor(mockRepo);
        MessageChannel testChannel = new MessageChannel() {
            @Override
            public boolean send(Message<?> message, long timeout) {
                return false;
            }
        };

        class Testcase {
            final Message<?> mockMessage;
            final MessageChannel mockChannel;
            final Boolean hasAccessToReturns;
            final User mockUser;
            final String collectionID;
            final Boolean exception;
            final String exceptionMessage;

            public Testcase(Message<?> mockMessage, MessageChannel mockChannel, Boolean hasAccessToReturns, User mockUser, String collectionID, Boolean exception, String exceptionMessage) {
                this.mockMessage = mockMessage;
                this.mockChannel = mockChannel;
                this.mockUser = mockUser;
                this.collectionID = collectionID;
                this.hasAccessToReturns = hasAccessToReturns;
                this.exception = exception;
                this.exceptionMessage = exceptionMessage;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(buildValidSubscriptionMessage(TestUser1, "1234"), testChannel, true, TestUser1, "1234", false, ""),
                new Testcase(buildValidSubscriptionMessage(TestUser1, "1234"), testChannel, false, TestUser1, "1234", true, "no access"),
                new Testcase(buildInvalidSubscriptionMessageWithInvalidUrl(TestUser1), testChannel, true, TestUser1, "", true, "invalid subscription url. valid: /collection/enter_id_here"),
                new Testcase(buildInvalidSubscriptionMessageWithNullUser("1234"), testChannel, true, null, "1234", true, "Are you currently logged in?"),
                new Testcase(buildInvalidSubscriptionMessageWithNullDestination(TestUser1), testChannel, true, TestUser1, "", true, "Invalid or missing stomp headers"),
                new Testcase(buildValidAlternativeSubscriptionMessage(TestUser1), testChannel, false, TestUser1, "", false, ""),
                new Testcase(buildValidConnectionMessage(), testChannel, false, null, "", false, "")
        };

        for(Testcase tc: testcases){
            if(!Objects.isNull(tc.mockUser)){
                when(mockRepo.hasAccessToCollection(tc.mockUser.getId(), tc.collectionID)).thenReturn(tc.hasAccessToReturns);
            }
            if (tc.exception){
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

    public Message<?> buildValidSubscriptionMessage(User user, String collectionID){
        Authentication fakeUserPrincipal = new UsernamePasswordAuthenticationToken(user, "");
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        fakeSubscriptionHeader.setHeader("simpDestination", "/collection/" + collectionID);
        fakeSubscriptionHeader.setUser(fakeUserPrincipal);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload("Test123");
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }
    public Message<?> buildInvalidSubscriptionMessageWithInvalidUrl(User user){
        Authentication fakeUserPrincipal = new UsernamePasswordAuthenticationToken(user, "");
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        fakeSubscriptionHeader.setHeader("simpDestination", "/collection/");
        fakeSubscriptionHeader.setUser(fakeUserPrincipal);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload("Test123");
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }
    public Message<?> buildInvalidSubscriptionMessageWithNullUser(String collectionID){
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        fakeSubscriptionHeader.setHeader("simpDestination", "/collection/" + collectionID);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload("Test123");
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }
    public Message<?> buildInvalidSubscriptionMessageWithNullDestination(User user){
        Authentication fakeUserPrincipal = new UsernamePasswordAuthenticationToken(user, "");
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        fakeSubscriptionHeader.setUser(fakeUserPrincipal);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload("Test123");
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }

    public Message<?> buildValidAlternativeSubscriptionMessage(User user){
        Authentication fakeUserPrincipal = new UsernamePasswordAuthenticationToken(user, "");
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        fakeSubscriptionHeader.setHeader("simpDestination", "/zeitgeist/");
        fakeSubscriptionHeader.setUser(fakeUserPrincipal);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload("Test123");
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }

    public Message<?> buildValidConnectionMessage(){
        StompHeaderAccessor fakeSubscriptionHeader = StompHeaderAccessor.create(StompCommand.CONNECT);
        MessageBuilder<String> fakeSubscriptionMessage = MessageBuilder.withPayload("Test123");
        fakeSubscriptionMessage.setHeaders(fakeSubscriptionHeader);
        return fakeSubscriptionMessage.build();
    }
}
