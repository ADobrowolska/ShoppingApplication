package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ShoppingListRepository shoppingListRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, ShoppingListRepository shoppingListRepository) {
        this.productRepository = productRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    private ShoppingList findingShoppingListById(int shoppingListId) {
        return shoppingListRepository.findById(shoppingListId).orElseThrow();
    }

    public Product getSingleProduct(int id, int shoppingListId) {
        ShoppingList shoppingList = findingShoppingListById(shoppingListId);
        return shoppingList.getProducts().stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElseThrow();
    }


    public List<Product> getProductsFromShoppingList(int shoppingListId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new NoSuchElementException("NoSuchElementException!"));
        return productRepository.findAllByShoppingListId(shoppingListId);
    }


    public Product addProductToShoppingList(Product product, int shoppingListId) {
        ShoppingList shoppingList = findingShoppingListById(shoppingListId);
        shoppingList.setTimeOfLastEditing(Instant.now());
        Product savedProduct = productRepository.save(product);
        getProductsFromShoppingList(shoppingListId).add(savedProduct);
        return savedProduct;
    }

    @Transactional
    public Product editProduct(int shoppingListId, Product product) {
        ShoppingList shoppingList = findingShoppingListById(shoppingListId);
        Product editedProduct = shoppingList.getProducts().stream()
                        .filter(product1 -> product1.getId() == product.getId())
                                .findFirst().orElseThrow();
        editedProduct.setName(product.getName());
        editedProduct.setQuantity(product.getQuantity());
        return editedProduct;
    }

    public void deleteProduct(int shoppingListId, int id) {
        ShoppingList shoppingList = findingShoppingListById(shoppingListId);
        Product deletedProduct = shoppingList.getProducts().stream()
                .filter(prod -> prod.getId() == id)
                .findFirst()
                .orElseThrow();
        productRepository.deleteById(deletedProduct.getId());
    }



}
