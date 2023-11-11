package com.shoppingapp.ShoppingApplication.dto.shoppinglist;

import com.shoppingapp.ShoppingApplication.dto.product.ProductDTO;
import com.shoppingapp.ShoppingApplication.model.User;
import lombok.*;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ShoppingListDTO {

    private Integer id;
    private String name;
    private Instant timeOfLastEditing;
    private Integer userId;
    private List<ProductDTO> products;

    private List<ShoppingListCategoryDTO> categories;



}
