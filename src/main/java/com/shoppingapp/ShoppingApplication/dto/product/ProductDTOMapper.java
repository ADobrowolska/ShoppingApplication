package com.shoppingapp.ShoppingApplication.dto.product;

import com.shoppingapp.ShoppingApplication.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTOMapper {

    public static List<ProductDTO> mapToProductDTOs(List<Product> products) {
        return products.stream()
                .map(product -> mapToProductDTO(product))
                .collect(Collectors.toList());
    }

    public static ProductDTO mapToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .categoryName(product.getCategory() == null? null : product.getCategory().getName())
                .build();
    }

    public static Product mapDTOToProductModel(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .quantity(productDTO.getQuantity())
                .build();
    }

    public static List<Product> mapDTOsToProductModelList(List<ProductDTO> productDTOList) {
        return productDTOList.stream()
                .map(productDTO -> mapDTOToProductModel(productDTO))
                .collect(Collectors.toList());
    }

}
