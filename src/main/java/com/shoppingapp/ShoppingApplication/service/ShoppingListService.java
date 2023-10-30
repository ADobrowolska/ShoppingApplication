package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import com.shoppingapp.ShoppingApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShoppingListService {

    private ShoppingListRepository shoppingListRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;


    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ShoppingList getSingleShoppingList(int id, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("No such element exception!"));
        return shoppingListRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoSuchElementException("No such element exception!"));
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingListRepository.findAll();
    }


    @Transactional
    public ShoppingList editSL(ShoppingList shoppingList) {
        ShoppingList editedSL = shoppingListRepository.findById(shoppingList.getId()).orElseThrow();
        editedSL.setName(shoppingList.getName());
        editedSL.setTimeOfLastEditing(Instant.now());
        return editedSL;
    }

    public ShoppingList addSL(ShoppingList shoppingList, int userId) {
        shoppingList.setTimeOfLastEditing(Instant.now());
        shoppingList.setUser(userRepository.findById(userId).orElseThrow());
        shoppingList.setProducts(new ArrayList<>());
        return shoppingListRepository.save(shoppingList);
    }

    public void deleteSLById(int id) {
        shoppingListRepository.deleteById(id);
    }

    public void deleteAllSLs() {
        shoppingListRepository.deleteAll();
    }

    @Transactional
    public void deleteOldShoppingList2(Instant instant) {
        List<ShoppingList> shoppingLists = shoppingListRepository.findAllByTimeOfLastEditingLessThan(instant);
        shoppingListRepository.deleteAll(shoppingLists);
    }

    public List<ShoppingList> getUserShoppingLists(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("NoSuchElementException!"));
        return shoppingListRepository.findAllByUserId(userId);
    }

}
