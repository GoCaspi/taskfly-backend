package com.gocaspi.taskfly.User;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {
   UserRepository mockRepo= mock(UserRepository.class);

 /* public void  getuserbyid(){

      UserController Usercontolle = new UserController(mockRepo);

      String testid="234";

    when(mockRepo.findById(testid)).thenReturn(Optional.of(mockUser));
      Usercontolle.getUser(testid);


@Test
     public void delteuserbyid(){

    UserController DeleteUser = new UserController(mockRepo);
    String id="2345";
    DeleteUser.deleteUser(id);
}

*/
}
