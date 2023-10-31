package com.shoppingapp.ShoppingApplication.dto.shoppinglist;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateShoppingListDTO {

    private Integer id;
    private String name;

}
