package com.gocaspi.taskfly.reset;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.UUID;

class ResetNewPasswordTest {
    String mockToken = UUID.randomUUID().toString();
    String mockPassword = "123";

    @Test
    void TestResetNewPasswordConstructor(){
        ResetNewPassword resetNewPassword = new ResetNewPassword(mockPassword, mockToken);
        assertEquals(mockToken, resetNewPassword.getToken());
        assertEquals(mockPassword, resetNewPassword.getPwd());
    }

    @Test
    void TestResetNewPasswordSetter(){
        ResetNewPassword resetNewPassword = new ResetNewPassword();
        resetNewPassword.setPwd(mockPassword);
        resetNewPassword.setToken(mockToken);
        assertEquals(mockToken, resetNewPassword.getToken());
        assertEquals(mockPassword, resetNewPassword.getPwd());
    }
}
