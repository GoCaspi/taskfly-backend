package com.gocaspi.taskfly.User;


import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {
    UserRepository mockRepo = mock(UserRepository.class);

/*
   @Test
   public void getuserbyid() {

      UserController Usercontolle = new UserController(mockRepo);
      User mockUser = new User("Luis","Müller");
      String testid = "234";

      when(mockRepo.findById(testid)).thenReturn(Optional.of(mockUser));
      Usercontolle.getUser(testid);
   }


@Test
     public void delteuserbyid(){

    UserController DeleteUser = new UserController(mockRepo);
    String id="2345";
    DeleteUser.deleteUser(id);
}

*/
}

