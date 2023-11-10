package com.shoppingapp.ShoppingApplication.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductDTO {

    private int id;
    private String name;
    private int quantity;
    private int categoryId;



}
