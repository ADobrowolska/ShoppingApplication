package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceUnitTest {

    @Test
    void emailCorrect() {
        UserService userService = new UserService(null, null);
        User user = new User();
        user.setEmail("abcdZ.12-x@abc.com");
        assertTrue(userService.validateEmail(user));
    }

    @Test
    void emailIncorrect() {
        UserService userService = new UserService(null, null);
        User user = new User();
        user.setEmail("abcabc.com");
        assertFalse(userService.validateEmail(user));
    }


}
