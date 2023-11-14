package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.product.ProductDTO;
import com.shoppingapp.ShoppingApplication.dto.product.ProductDTOMapper;
import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/shopping/{shoppingListId}/products/{id}")
    public ResponseEntity<ProductDTO> getSingleProduct(@PathVariable int id, @PathVariable int shoppingListId, @RequestHeader(value = "user-id") int userId) {
        ProductDTO productDTO = ProductDTOMapper.mapToProductDTO(productService.getSingleProduct(id, shoppingListId, userId));
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/shopping/{shoppingListId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsOnShoppingList(@PathVariable int shoppingListId, @RequestHeader(value = "user-id") int userId) {
        List<ProductDTO> productDTOList = ProductDTOMapper.mapToProductDTOs(productService.getProductsFromShoppingList(shoppingListId, userId));
        return ResponseEntity.ok(productDTOList);
    }

    @PostMapping("/shopping/{shoppingListId}/products")
    public ResponseEntity<ProductDTO> addProductToShoppingList(@RequestBody ProductDTO productDTO, @PathVariable int shoppingListId, @RequestHeader(value = "user-id") int userId) throws InstanceAlreadyExistsException {
        Product product = ProductDTOMapper.mapDTOToProductModel(productDTO);
        ProductDTO savedProductDTO = ProductDTOMapper.mapToProductDTO(productService.addProductToShoppingList(product, shoppingListId, userId));
        return ResponseEntity.ok(savedProductDTO);
    }

    @PutMapping("/shopping/{shoppingListId}/products")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable int shoppingListId, @RequestBody ProductDTO productDTO, @RequestHeader(value = "user-id") int userId) {
        Product product = ProductDTOMapper.mapDTOToProductModel(productDTO);
        ProductDTO editedProductDTO = ProductDTOMapper.mapToProductDTO(productService.editProduct(shoppingListId, userId, product));
        return ResponseEntity.ok(editedProductDTO);
    }

    @DeleteMapping("/shopping/{shoppingListId}/products/{id}")
    public void deleteProduct(@PathVariable int shoppingListId, @PathVariable int id, @RequestHeader(value = "user-id") int userId) {
        productService.deleteProduct(shoppingListId, userId, id);
    }
}

