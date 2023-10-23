package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    void addUser() {
        User user = new User(
                null,
                "Anna",
                "Nowak",
                "annanowak@gmail.com",
                null
        );

        User saved = service.addUser(user);
        System.out.println("");



    }
}
