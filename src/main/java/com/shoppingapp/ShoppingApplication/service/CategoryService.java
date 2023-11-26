package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByParam(String searchBy) {
        return categoryRepository.getCategoriesContainingParam(searchBy.toLowerCase());
    }
}
