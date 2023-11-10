package com.shoppingapp.ShoppingApplication.repository;

import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    void deleteAllByShoppingListId(int shoppingListId);

    void deleteAllByShoppingList(ShoppingList shoppingList);

    List<Product> findAllByShoppingListId(Integer id);

    boolean existsByName(String name);

}
