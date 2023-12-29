package com.shoppingapp.ShoppingApplication;

import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ShoppingListRepository shoppingListRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserRoleRepository userRoleRepository;

    protected void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        shoppingListRepository.deleteAll();
        userRoleRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected Category createCategory(String name) {
        Category category = new Category();
        category.setName("Pieczywo");
        return categoryRepository.save(category);
    }

}
