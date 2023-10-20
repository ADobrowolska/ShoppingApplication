package com.shoppingapp.ShoppingApplication.dto.category;

import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListCategoryDTO;
import com.shoppingapp.ShoppingApplication.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDTOMapper {

    public static List<CategoryDTO> mapToCategoryDTOs(List<Category> categories) {
        return categories.stream()
                .map(category -> mapToCategoryDTO(category))
                .collect(Collectors.toList());
    }

    public static CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }










}
