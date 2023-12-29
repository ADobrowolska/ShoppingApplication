package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import com.shoppingapp.ShoppingApplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShoppingListService {

    private ShoppingListRepository shoppingListRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(ShoppingListService.class);


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

    public List<ShoppingList> getShoppingLists(int userId) {
        return shoppingListRepository.findAllByUserId(userId);
    }


    @Transactional
    public ShoppingList editSL(Integer id, ShoppingList shoppingList, int userId) {
        User user = userRepository.findById(userId).orElseThrow();
        ShoppingList editedSL = shoppingListRepository.findByIdAndUserId(id, userId).orElseThrow();
        editedSL.setName(shoppingList.getName());
        editedSL.setTimeOfLastEditing(Instant.now());
        return shoppingListRepository.save(editedSL);
    }

    public ShoppingList addSL(ShoppingList shoppingList, int userId) {
        shoppingList.setTimeOfLastEditing(Instant.now());
        shoppingList.setUser(userRepository.findById(userId).orElseThrow());
        shoppingList.setProducts(new ArrayList<>());
        return shoppingListRepository.save(shoppingList);
    }

    public void deleteSLById(int id, int userId) {
        ShoppingList shoppingListToRemove = shoppingListRepository.findByIdAndUserId(id, userId).orElseThrow();
        shoppingListRepository.deleteById(shoppingListToRemove.getId());
    }

    public void deleteAllSLs(int userId) {
        List<ShoppingList> allByUserId = shoppingListRepository.findAllByUserId(userId);
        shoppingListRepository.deleteAll(allByUserId);
    }

    @Transactional
    public void deleteOldShoppingList(Instant instant, int userId) {
        List<ShoppingList> shoppingLists = shoppingListRepository.findAllByTimeOfLastEditingLessThanAndUserId(instant, userId);
        shoppingListRepository.deleteAll(shoppingLists);
    }

    public List<ShoppingList> getUserShoppingLists(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("NoSuchElementException!"));
        return shoppingListRepository.findAllByUserId(userId);
    }

    @Scheduled(cron = "0 */1 * ? * *", zone = "Europe/Warsaw")
    void deleteOlderThan() {
        deleteOlderThan(Instant.now().minus(Period.ofDays(14)));
    }

    void deleteOlderThan(Instant date) {
        List<ShoppingList> oldShoppingLists = shoppingListRepository.getOldShoppingLists(date);
        shoppingListRepository.deleteAll(oldShoppingLists);
        log.info("All Shopping Lists older than " + Instant.now().minus(Period.ofDays(14)) + " deleted");
    }
}
