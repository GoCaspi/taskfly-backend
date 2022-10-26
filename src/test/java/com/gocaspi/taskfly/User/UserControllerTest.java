package com.gocaspi.taskfly.User;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class UserControllerTest {
   UserRepository mockRepo= mock(UserRepository.class);
@Test
    public void  getuserbyid(){

      UserController Usercontolle = new UserController(mockRepo);

      String testid="234";
      Usercontolle.getUser(testid);

}


}
