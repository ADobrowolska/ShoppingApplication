package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.BaseTest;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.InstanceAlreadyExistsException;
import java.time.Instant;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingListServiceTest extends BaseTest {

    @Autowired
    protected UserService userService;

    @Autowired
    protected ShoppingListService shoppingListService;

    @Autowired
    protected ShoppingListRepository shoppingListRepository;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    protected ShoppingList createShoppingList(String name, Instant time, User user) {
        ShoppingList newShoppingList = new ShoppingList();
        newShoppingList.setName(name);
        newShoppingList.setUser(user);
        newShoppingList.setTimeOfLastEditing(time);
        return shoppingListRepository.save(newShoppingList);
    }

    protected User createUser(String firstName, String lastName, String email) throws InstanceAlreadyExistsException {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return userService.addUser(user);
    }

    @Test
    void deleteOlderThan() throws InstanceAlreadyExistsException {
        User user1 = createUser("Anna", "Nowak", "an@x.com");
        User user2 = createUser("Jan", "Kowalski", "jk@x.com");
        createShoppingList("List1", Instant.now().minus(Period.ofDays(10)), user1);
        createShoppingList("List2", Instant.now().minus(Period.ofDays(1)), user2);
        assertThat(shoppingListRepository.findAll()).isNotEmpty();
        assertEquals(2, shoppingListRepository.findAll().size());
        shoppingListService.deleteOlderThan(Instant.now().minus(Period.ofDays(3)));
        assertThat(shoppingListRepository.findAll()).isNotEmpty();
        assertEquals(1, shoppingListRepository.findAll().size());
    }
}