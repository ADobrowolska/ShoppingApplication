package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.product.ProductDTO;
import com.shoppingapp.ShoppingApplication.dto.product.ProductDTOMapper;
import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/shopping/{shoppingListId}/products/{id}")
    public ProductDTO getSingleProduct(@PathVariable int id, @PathVariable int shoppingListId) {
        return ProductDTOMapper.mapToProductDTO(productService.getSingleProduct(id, shoppingListId));
    }

    @GetMapping("/shopping/{shoppingListId}/products")
    public List<ProductDTO> getProductsOnShoppingList(@PathVariable int shoppingListId) {
        return ProductDTOMapper.mapToProductDTOs(productService.getProductsFromShoppingList(shoppingListId));
    }

    @GetMapping(value = "/shopping/products2", params = {"shoppingListId"})
    public ResponseEntity<List<ProductDTO>> getProductsOnShoppingList2(@RequestParam int shoppingListId) {
        try {
            List<ProductDTO> productDTOList =
                    ProductDTOMapper.mapToProductDTOs(productService.getProductsFromShoppingList(shoppingListId));
            return ResponseEntity.ok(productDTOList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/shopping/{shoppingListId}/products")
    public ProductDTO addProductToShoppingList(@RequestBody Product product, @PathVariable int shoppingListId) {
        return ProductDTOMapper.mapToProductDTO(productService.addProductToShoppingList(product, shoppingListId));
    }

//    record ProductRequestDTO (Integer id, String name, Integer categoryId, int quantity, Integer shoppingListId){}
    @PutMapping("/shopping/{shoppingListId}/products")
    public ProductDTO editProduct(@PathVariable int shoppingListId, @RequestBody Product product) {
        return ProductDTOMapper.mapToProductDTO(productService.editProduct(shoppingListId, product));
    }

    @DeleteMapping("/shopping/{shoppingListId}/products/{id}")
    public void deleteProduct(@PathVariable int shoppingListId, @PathVariable int id) {
        productService.deleteProduct(shoppingListId, id);
    }
}

