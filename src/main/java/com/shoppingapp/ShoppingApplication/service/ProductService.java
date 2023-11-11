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


    public Product addProductToShoppingList(Product product, int shoppingListId) throws InstanceAlreadyExistsException {
        ShoppingList shoppingList = findingShoppingListById(shoppingListId);
        if (!productRepository.existsByName(product.getName())) {
            shoppingList.setTimeOfLastEditing(Instant.now());
            product.setCategory(categoryRepository.findById(product.getCategory().getId()).orElseThrow());
            shoppingList.addProduct(product);
            return productRepository.save(product);
        } else {
            throw new InstanceAlreadyExistsException("Product is on the list");
        }

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
        shoppingList.getProducts().remove(deletedProduct);
        productRepository.deleteById(deletedProduct.getId());
    }


}
