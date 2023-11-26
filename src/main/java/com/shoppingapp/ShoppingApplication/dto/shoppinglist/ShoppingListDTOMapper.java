package com.shoppingapp.ShoppingApplication.dto.shoppinglist;

import com.shoppingapp.ShoppingApplication.dto.product.ProductDTOMapper;
import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShoppingListDTOMapper {

    public static ShoppingList mapDTOToShoppingListModel(RequestShoppingListDTO shoppingListDTO) {
        return ShoppingList.builder()
                .name(shoppingListDTO.getName())
                .build();
    }

    public static List<ShoppingList> mapDTOsToShoppingListModelList(List<RequestShoppingListDTO> shoppingListDTOList) {
        return shoppingListDTOList.stream()
                .map(shoppingListDTO -> mapDTOToShoppingListModel(shoppingListDTO))
                .toList();
    }

    public static List<ShoppingListDTO> mapToShoppingListDTOs(List<ShoppingList> shoppingLists) {
        return shoppingLists.stream()
                .map(shL -> mapToShoppingListDTO(shL))
                .collect(Collectors.toList());
    }

    public static ShoppingListDTO mapToShoppingListDTO(ShoppingList shoppingList) {
        return ShoppingListDTO.builder()
                .id(shoppingList.getId())
                .name(shoppingList.getName())
                .timeOfLastEditing(shoppingList.getTimeOfLastEditing())
                .userId(shoppingList.getUser().getId())
                .products(shoppingList.getProducts() != null ? shoppingList.getProducts().stream()
                        .map(ProductDTOMapper::mapToProductDTO)
                        .toList()
                        : new ArrayList<>())
                .categories(getCategories(shoppingList.getProducts()))
                .build();
    }

    public static RequestShoppingListDTO mapToCreateShoppingListDTO(ShoppingList shoppingList) {
        return RequestShoppingListDTO.builder()
                .name(shoppingList.getName())
                .build();
    }

    private static List<ShoppingListCategoryDTO> getCategories(List<Product> products) {
        Map<Category, List<Product>> productsGroupByCategoryMap = new HashMap<>();
        if (products == null) {
            return null;
        }
        for (Product product : products) {
            if (productsGroupByCategoryMap.containsKey(product.getCategory())) {
                List<Product> existedListOfProducts = productsGroupByCategoryMap.get(product.getCategory());
                existedListOfProducts.add(product);
            } else {
                List<Product> nonExistedListOfProducts = new ArrayList<>();
                nonExistedListOfProducts.add(product);
                productsGroupByCategoryMap.put(product.getCategory(), nonExistedListOfProducts);
            }
        }

        List<ShoppingListCategoryDTO> shoppingListCategoryDTOS = new ArrayList<>();
        for (Map.Entry<Category, List<Product>> entry : productsGroupByCategoryMap.entrySet()) {
            Category category = entry.getKey();
            List<Product> productList = entry.getValue();
            ShoppingListCategoryDTO shoppingListCategoryDTO = ShoppingListCategoryDTO.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .products(ProductDTOMapper.mapToProductDTOs(productList))
                    .build();
            shoppingListCategoryDTOS.add(shoppingListCategoryDTO);
        }
        return shoppingListCategoryDTOS;
    }





    private static List<ShoppingListCategoryDTO> getCategoriesV2(List<Product> products) {
        if (products == null) {
            return null;
        }
        Map<Category, List<Product>> productsGroupedByCategoryMap = products.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory()));

        return toShoppingListCategoryDTOList(productsGroupedByCategoryMap);
    }

    private static List<ShoppingListCategoryDTO> toShoppingListCategoryDTOList(Map<Category, List<Product>> categoryListMap) {

        return categoryListMap.entrySet().stream()
                .map(entry -> {
                    Category category = entry.getKey();
                    List<Product> productList = entry.getValue();
                    return ShoppingListCategoryDTO.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .products(ProductDTOMapper.mapToProductDTOs(productList))
                            .build();
                })
                .toList();
    }

}
