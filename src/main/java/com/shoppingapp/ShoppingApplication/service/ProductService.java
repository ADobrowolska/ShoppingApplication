package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.repository.CategoryRepository;
import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ShoppingListRepository shoppingListRepository;
    private CategoryRepository categoryRepository;



    @Autowired
    public ProductService(ProductRepository productRepository, ShoppingListRepository shoppingListRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product getSingleProduct(int id, int shoppingListId, int userId) {
        ShoppingList shoppingList = shoppingListRepository.findByIdAndUserId(shoppingListId, userId)
                .orElseThrow(() -> new NoSuchElementException("Shopping list could not be found."));
        return shoppingList.getProducts().stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }


    public List<Product> getProductsFromShoppingList(int shoppingListId, int userId) {
        ShoppingList shoppingList = shoppingListRepository.findByIdAndUserId(shoppingListId, userId)
                .orElseThrow(() -> new NoSuchElementException("NoSuchElementException!"));
        return productRepository.findAllByShoppingListId(shoppingListId);
    }


    public Product addProductToShoppingList(Product product, int shoppingListId, int userId) throws InstanceAlreadyExistsException {
        ShoppingList shoppingList = shoppingListRepository.findByIdAndUserId(shoppingListId, userId)
                .orElseThrow(() -> new NoSuchElementException("Shopping list could not be found."));
        if (!productRepository.existsByName(product.getName())) {
            shoppingList.setTimeOfLastEditing(Instant.now());
            if (product.getQuantity() == 0) {
                throw new IllegalArgumentException("Product quantity cannot be 0");
            }
            product.setCategory(categoryRepository.findById(product.getCategory().getId()).orElseThrow());
            shoppingList.addProduct(product);
            return productRepository.save(product);
        } else {
            throw new InstanceAlreadyExistsException("Product is on the list");
        }
    }

    @Transactional
    public Product editProduct(int shoppingListId, int userId, Product product) {
        ShoppingList shoppingList = shoppingListRepository.findByIdAndUserId(shoppingListId, userId)
                .orElseThrow(() -> new NoSuchElementException("Shopping list could not be found."));
        Product editedProduct = shoppingList.getProducts().stream()
                .filter(product1 -> product1.getId().equals(product.getId()))
                .findFirst().orElseThrow();
        editedProduct.setName(product.getName());
        editedProduct.setQuantity(product.getQuantity());
        return editedProduct;
    }

    public void deleteProduct(int shoppingListId, int userId, int id) {
        ShoppingList shoppingList = shoppingListRepository.findByIdAndUserId(shoppingListId, userId)
                .orElseThrow(() -> new NoSuchElementException("Shopping list could not be found."));
        Product deletedProduct = shoppingList.getProducts().stream()
                .filter(prod -> prod.getId().equals(id))
                .findFirst()
                .orElseThrow();
        shoppingList.removeProduct(deletedProduct);
        productRepository.deleteById(deletedProduct.getId());
    }


}
