package com.shoppingapp.ShoppingApplication.repository;

import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    void deleteAllByShoppingListId(int shoppingListId);

    void deleteAllByShoppingList(ShoppingList shoppingList);

    List<Product> findAllByShoppingListId(Integer id);
//    List<Product> findAllByNameAndShoppingListId(String name, Integer listId);

//    "select * from product where id=1"
//    "select * from product where id in (1, 2,3,5,7)"
}
