package org.fffd.l23o6.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void testListOrders(){
        System.out.println(orderService.listOrders("李白"));
    }

    @Test
    void test(){

    }

}
