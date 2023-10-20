package com.shoppingapp.ShoppingApplication.dto.shoppinglist;

import com.shoppingapp.ShoppingApplication.dto.product.ProductDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
public class ShoppingListCategoryDTO {

    private int id;
    private String name;
    private List<ProductDTO> products;

}
