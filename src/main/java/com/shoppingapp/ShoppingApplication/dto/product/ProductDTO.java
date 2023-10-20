package com.shoppingapp.ShoppingApplication.dto.product;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductDTO {

    private int id;
    private String name;
    private int quantity;
    private String categoryName;


}
