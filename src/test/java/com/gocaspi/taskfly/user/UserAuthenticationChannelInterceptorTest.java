package com.gocaspi.taskfly.user;

import com.gocaspi.taskfly.auth.UserAuthenticationChannelInterceptor;
import com.gocaspi.taskfly.taskcollection.TaskCollectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class UserAuthenticationChannelInterceptorTest {
    final private AuthenticationProvider authManagerMock = mock(AuthenticationProvider.class);
    final private User TestUser1 = new User("Peter", "Schmidt", "test123@gmail.com", "test123", "", null, false);
    @Test
    void TestPreSend(){
        UserAuthenticationChannelInterceptor interceptor = new UserAuthenticationChannelInterceptor(authManagerMock);
        MessageChannel testChannel = new MessageChannel() {
            @Override
            public boolean send(Message<?> message, long timeout) {
                return false;
            }
        };
        class Testcase {
            final Message<?> mockMessage;
            final MessageChannel mockChannel;
            final String userEmail;
            final String userPassword;
            final UsernamePasswordAuthenticationToken userAuthReqMock;
            final User mockUser;
            final Boolean exception;
            final String exceptionMessage;

            public Testcase(Message<?> mockMessage, MessageChannel mockChannel, User mockUser, String userEmail, String userPassword, Boolean exception, String exceptionMessage) {
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
                new Testcase(buildValidConnectionMessage("test123@gmail.com", "test123"), testChannel, TestUser1, "test", "1234", false, ""),

        };
        for (Testcase tc: testcases){
            when(authManagerMock.authenticate(tc.userAuthReqMock)).thenReturn(tc.userAuthReqMock);
            Message<?> actual = interceptor.preSend(tc.mockMessage, tc.mockChannel);
            assertEquals(tc.mockMessage, actual);

        }
    }
    Message<?> buildValidConnectionMessage(String email, String password){
        NativeMessageHeaderAccessor fakeConnectionHeader = StompHeaderAccessor.create(StompCommand.CONNECT);
        fakeConnectionHeader.setNativeHeader("username", email);
        fakeConnectionHeader.setNativeHeader("password", password);
        MessageBuilder<String> fakeConnectionMessage = MessageBuilder.withPayload("123");
        fakeConnectionMessage.setHeaders(fakeConnectionHeader);
        return fakeConnectionMessage.build();
    }
}
