package com.shoppingapp.ShoppingApplication.dto.shoppinglist;

import com.shoppingapp.ShoppingApplication.dto.product.ProductDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ShoppingListDTO {

    private Integer id;
    private String name;
    private Instant timeOfLastEditing;
    private List<ProductDTO> products;

    private List<ShoppingListCategoryDTO> categories;



}
