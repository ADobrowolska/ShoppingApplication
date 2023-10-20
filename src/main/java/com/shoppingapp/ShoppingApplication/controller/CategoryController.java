package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.category.CategoryDTO;
import com.shoppingapp.ShoppingApplication.dto.category.CategoryDTOMapper;
import com.shoppingapp.ShoppingApplication.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<CategoryDTO> getAllCategories() {
        return CategoryDTOMapper.mapToCategoryDTOs(categoryService.getAllCategories());
    }

    @GetMapping(value = "/shopping/category", params = "searchBy")
    public List<CategoryDTO> getCategories(@RequestParam String searchBy) {
        return CategoryDTOMapper.mapToCategoryDTOs(categoryService.getCategoriesByParam(searchBy));
    }


}
