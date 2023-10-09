package org.fffd.l23o6.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testRegister(){
        userService.register("李白",
                "Libai114514",
                "李白",
                "320684200306300000",
                "15026997548",
                "身份证");
    }

    @Test
    void testLogin(){
        userService.login("李白", "Libai114514");
    }

    @Test
    void testEdit(){
        userService.editInfo("李白", "李白", "320625200306300000", "13916252689", "身份证");
    }

    @Test
    void testFindByName(){
        System.out.println(userService.findByUserName("李白").getName());
    }

}
