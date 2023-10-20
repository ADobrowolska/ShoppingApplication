package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ShoppingListService {

    private ShoppingListRepository shoppingListRepository;
    private ProductRepository productRepository;


    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, ProductRepository productRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.productRepository = productRepository;
    }

    public ShoppingList getSingleShoppingList(int id) {
        return shoppingListRepository.findById(id).orElseThrow();
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

    public ShoppingList addSL(ShoppingList shoppingList) {
        shoppingList.setTimeOfLastEditing(Instant.now());
        return shoppingListRepository.save(shoppingList);
    }

    public void deleteSLById(int id) {
        shoppingListRepository.deleteById(id);
    }

    public void deleteAllSLs() {
        shoppingListRepository.deleteAll();
    }

//    @Transactional
//    public void deleteOldShoppingList(Instant instant) {
//        List<ShoppingList> shoppingLists = shoppingListRepository.findAllByTimeOfLastEditingLessThan(instant);
//        shoppingLists.forEach(sl -> productRepository.deleteAllByShoppingList(sl));
//        productRepository.flush();
////        shoppingListRepository.deleteAll(shoppingLists);
//        shoppingListRepository.deleteOldShoppingList(instant);
//    }

    @Transactional
    public void deleteOldShoppingList2(Instant instant) {
        List<ShoppingList> shoppingLists = shoppingListRepository.findAllByTimeOfLastEditingLessThan(instant);
        shoppingListRepository.deleteAll(shoppingLists);
    }

//    @Transactional
//    public void deleteOldShoppingList3(Instant instant) {
//        shoppingListRepository.deleteAllByTimeOfLastEditingLessThan(instant);
//    }
}
