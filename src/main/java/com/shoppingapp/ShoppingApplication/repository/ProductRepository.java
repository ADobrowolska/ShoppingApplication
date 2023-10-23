package com.shoppingapp.ShoppingApplication.repository;

import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    void deleteAllByShoppingListId(int shoppingListId);

    void deleteAllByShoppingList(ShoppingList shoppingList);

    List<Product> findAllByShoppingListId(Integer id);

}
