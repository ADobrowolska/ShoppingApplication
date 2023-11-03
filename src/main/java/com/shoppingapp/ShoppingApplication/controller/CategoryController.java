package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.category.CategoryDTO;
import com.shoppingapp.ShoppingApplication.dto.category.CategoryDTOMapper;
import com.shoppingapp.ShoppingApplication.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/shopping/category/x")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoryDTOs = CategoryDTOMapper.mapToCategoryDTOs(categoryService.getAllCategories());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping(value = "/shopping/category", params = "searchBy")
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestParam String searchBy) {
        List<CategoryDTO> categoryDTOs = CategoryDTOMapper.mapToCategoryDTOs(categoryService.getCategoriesByParam(searchBy));
        return ResponseEntity.ok(categoryDTOs);
    }


}
