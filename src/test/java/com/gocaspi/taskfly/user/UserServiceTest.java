package com.gocaspi.taskfly.user;


import com.gocaspi.taskfly.task.Task;
import com.gocaspi.taskfly.task.TaskService;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class UserServiceTest {
    UserRepository mockRepo = mock(UserRepository.class);
    HttpClientErrorException er = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "no User are assigned to the provided userId", null, null, null);
    String mockFistName = "topic1";
    String mockLastName = "prio1";
    String mockEmail = "desc1";
    String mockPassword = "11-11-2022";
    String mocksrole ="ADMIN";
    ObjectId mockObject_Id = new ObjectId();
    User.Userbody mockbody =new User.Userbody("mockTeam","mockListId","mockUserId");
    User mockUser = new User(mocksrole, mockFistName, mockLastName, mockEmail, mockPassword,mockbody);
    UserService ts = new UserService(mockRepo);

    @Test
     void getService_AllUser() {

        UserService t = new UserService(mockRepo);
        User[] mockUserArr = new User[]{mockUser, mockUser };
        ArrayList<User> mockList = new ArrayList<>();
        for (User user : mockUserArr) { mockList.add(user); }

        class Testcase {
            final String id;
            final List<User> dbReturn;
            final List<User> expected;

            public	Testcase(String id, List<User> dbReturn, List<User> expected) {
                this.id = id;
                this.dbReturn = dbReturn;
                this.expected = expected;
            }
        }
        Testcase[] testcases = new Testcase[]{
                new Testcase("123", mockList,mockList),
                new Testcase("1",new ArrayList<User>(),new ArrayList<User>())
        };
        for(Testcase tc : testcases){
            when(mockRepo.findAll()).thenReturn(tc.dbReturn);
            List actual = t.getServiceAllUser();
            assertEquals(tc.expected, actual);
        }
    }

     @Test
      void validateTaskFields() {
         UserService t = new UserService(mockRepo);
         class Testcase {
             final User userInput;
             final boolean expected;

             public Testcase(User testUser, boolean expected) {
                 this.userInput = testUser;
                 this.expected = expected;
             }
         }

         Testcase[] testcases = new Testcase[]{
                 new Testcase(mockUser, true),
                 new Testcase(new User(null,null,null,null,null,null),false),
                 new Testcase(new User("test","test","test","test","test",mockbody),true)
         };

         for (Testcase tc : testcases) {
             boolean actualOut = t.validateTaskFields(new Gson().toJson(tc.userInput));
             assertEquals(tc.expected, actualOut);
         }
     }
    @Test
     void updateUserService(){
        UserService s = new UserService(mockRepo);
        var emptyBody = new User.Userbody("","","");
        var emptyUser = new User("", "", "", "","", emptyBody);
        class Testcase {
            final User mockUser;
            final String mockID;
            final Boolean exists;

            public Testcase(User user, String mockID, Boolean exists) {
                this.mockUser = user;
                this.mockID = mockID;
                this.exists = exists;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(emptyUser, mockObject_Id.toHexString(), true),
                new Testcase(mockUser, mockObject_Id.toHexString(), true),
                new Testcase(mockUser, mockObject_Id.toHexString(), false)
        };
        for (Testcase tc : testcases) {
            try {
                Optional<User> taskCollection = Optional.ofNullable(tc.mockUser);
                when(mockRepo.findById(tc.mockID)).thenReturn(taskCollection);
                when(mockRepo.existsById(tc.mockID)).thenReturn(tc.exists);
                s.updateService(tc.mockID, tc.mockUser);
                if (tc.exists){
                    verify(mockRepo, times(1)).save(tc.mockUser);
                }
            } catch (Exception e) {

            }

        }
    }
}
   /* @Test
    void getdetail() {
        UserService t = new UserService(mockRepo);
        class Testcase {
            final User userInput;
            final boolean expected;

            public Testcase(User testUser, boolean expected) {
                this.userInput = testUser;
                this.expected = expected;
            }
        }

        Testcase[] testcases = new Testcase[]{
                new Testcase(mockUser, false),
                new Testcase(new User(null,null,null,null,null,null),false),
                new Testcase(new User("test","test","test","test","test",mockbody),true)
        };
        for(Testcase tc : testcases){
            when(mockRepo.findByEmail(mockEmail)).thenReturn(mockUser);
            User actual = t.getDetails(mockEmail);
            assertEquals(tc.expected, actual);
        }
    }
*/

