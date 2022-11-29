/*
package com.gocaspi.taskfly.user;
import com.gocaspi.taskfly.auth.UserAuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.Subject;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserAuthenticationProviderTest {
    UserRepository mockRepo = mock(UserRepository.class);
    PasswordEncoder mockEncoder = mock(PasswordEncoder.class);
    String mockFistName = "topic1";
    String mockTeam = "team1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    String mocksrole = "ADMIN";
    User.Userbody mockbody =new User.Userbody("mockTeam","mockListId","mockUserId");

    User mockUser = new User(mocksrole, mockFistName, mockLastName, mockEmail, mockPassword,mockbody);


    public class testauth implements Authentication {
        public testauth() {

        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean implies(Subject subject) {
            return Authentication.super.implies(subject);
        }
    }

    testauth test = new testauth();

    @Test
    void Authenticationtest() {
        UserAuthenticationProvider Test = new UserAuthenticationProvider(mockRepo, mockEncoder);

        class Testcase {

            final Authentication mockauthentication;
            final boolean expected;

            public Testcase(Authentication mockauthentication, boolean expected) {
                this.expected = expected;
                this.mockauthentication = mockauthentication;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase(test, false),
                new Testcase(test, true),
        };
        for (Testcase tc : testcases) {
            when(mockRepo.findByEmail(mockEmail)).thenReturn(mockUser);
            Test.authenticate(tc.mockauthentication);

        }
    }

}

*/