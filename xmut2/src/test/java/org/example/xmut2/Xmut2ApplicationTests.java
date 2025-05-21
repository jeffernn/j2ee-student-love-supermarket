package org.example.xmut2;

import org.example.xmut2.lovegoods.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Xmut2ApplicationTests {

    @Autowired
    private AuthController authController;

    @Test
    void contextLoads() {
        assertNotNull(authController);
    }
}
