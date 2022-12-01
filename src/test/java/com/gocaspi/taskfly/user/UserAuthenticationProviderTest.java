
package com.gocaspi.taskfly.user;
import com.gocaspi.taskfly.auth.UserAuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
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
    User mockUser = new User(mocksrole, mockFistName, mockLastName, mockEmail, mockPassword,mockbody, true);

    public class testauth implements Authentication {
        public testauth() {

        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
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
            try {
                Test.authenticate(tc.mockauthentication);
            }
           catch (BadCredentialsException e){
               BadCredentialsException expectedException = new BadCredentialsException("");
               assertEquals(e.getClass(),expectedException.getClass());
           }
        }
    }
    @Test
    void getUserRoles(){
        SimpleGrantedAuthority t =new SimpleGrantedAuthority("ROLE_WRITE");

        UserAuthenticationProvider Test = new UserAuthenticationProvider(mockRepo, mockEncoder);

       class Testcase {
           private final String testRole;
           private final List expected ;

           Testcase(String testRole, List<GrantedAuthority> expected) {
               this.testRole = testRole;
               this.expected = expected;
           }
       }
       Testcase[] testcases = new Testcase[]{
               new Testcase("",new ArrayList<>())
       };
       for (Testcase tc : testcases){
           tc.expected.add("ROLE_WRITE");
           List<GrantedAuthority> actual = Test.getUserRoles(t.getAuthority());
           assertEquals(tc.expected.get(0).toString(),actual.get(0).toString());
       }

    }
}
