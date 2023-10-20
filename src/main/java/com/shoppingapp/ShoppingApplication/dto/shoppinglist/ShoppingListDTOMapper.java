package com.shoppingapp.ShoppingApplication.dto.shoppinglist;

import com.shoppingapp.ShoppingApplication.dto.product.ProductDTO;
import com.shoppingapp.ShoppingApplication.dto.product.ProductDTOMapper;
import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingListDTOMapper {

    public static ShoppingList mapDTOToShoppingListModel(ShoppingListDTO shoppingListDTO) {
        return ShoppingList.builder()
                .id(shoppingListDTO.getId())
                .name(shoppingListDTO.getName())
                .timeOfLastEditing(shoppingListDTO.getTimeOfLastEditing())
                .products(shoppingListDTO.getProducts() != null ? shoppingListDTO.getProducts().stream()
                        .map(productDTO -> ProductDTOMapper.mapDTOToProductModel(productDTO))
                        .toList()
                        : new ArrayList<>())
                .build();
    }

    public static List<ShoppingList> mapDTOsToShoppingListModelList(List<ShoppingListDTO> shoppingListDTOList) {
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
                .products(shoppingList.getProducts() != null ? shoppingList.getProducts().stream()
                        .map(ProductDTOMapper::mapToProductDTO)
                        .toList()
                        : new ArrayList<>())
                //               .products(ProductDTOMapper.mapToProductDTOs(new ArrayList<>(shoppingList.getProducts())))
                .categories(getCategories(shoppingList.getProducts()))
                .build();
    }

    private static List<ShoppingListCategoryDTO> getCategories(List<Product> products) {
        Map<Category, List<Product>> productsGroupByCategoryMap = new HashMap<>();
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
        /**
         * [
         *  { "categoria1" : "{produkt1, produkt2}"},
         *  { "categoria2" : "{produkt3, produkt4"},
         *  { "categoria3" : "{produkt5}"}
         * ]
         */
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

    /**
     * products: [
     * {name: chleb, kat: pieczywo},
     * {name: kawa, kat: picie},
     * {name: sol, kat: przyprawa},
     * {name: bulka, kat: pieczywo}
     * ] => [
     * {kat: pieczywo, products: [chleb, bluka]},
     * {kat: picie, products: [kawa]},
     * {kat: przyprawa, products: [sol]}
     * ]
     *
     * @param products
     * @return
     */

    //2 sposób:

//    private static List<ShoppingListCategoryDTO> getCategories2(List<Product> products) {
//        List<ShoppingListCategoryDTO> shoppingListCategoryDTOS = new ArrayList<>();
//
//        for (Product product : products) {
//            ShoppingListCategoryDTO existed = null;
//            for (ShoppingListCategoryDTO shoppingListCategoryDTO : shoppingListCategoryDTOS) {
//                if (shoppingListCategoryDTO.getId() == product.getCategory().getId()) {
//                    existed = shoppingListCategoryDTO;
//                    break;
//                }
//            }
//
//            if (existed != null) {
//                existed.getProducts().add(ProductDTOMapper.mapToProductDTO(product));
//            } else {
//                List<ProductDTO> productDTOS = new ArrayList<>();
//                productDTOS.add(ProductDTOMapper.mapToProductDTO(product));
//                ShoppingListCategoryDTO shoppingListCategoryDTO = ShoppingListCategoryDTO.builder()
//                        .id(product.getCategory().getId())
//                        .name(product.getCategory().getName())
//                        .products(productDTOS)
//                        .build();
//                shoppingListCategoryDTOS.add(shoppingListCategoryDTO);
//            }
//
//        }
//        //petla for po produktach
//        //dla kazdego produktu sprwdzamy czy juz istnieje w categoryDTOS
//        //jezeli tak to dodajemy produkt do istniejacej kategorii
//        // jezeli nie to dodajemy nowa kategorie
//
//
//        return shoppingListCategoryDTOS;
//    }

}
