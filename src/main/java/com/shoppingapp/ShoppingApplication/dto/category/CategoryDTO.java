package com.shoppingapp.ShoppingApplication.dto.category;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryDTO {

    private int id;
    private String name;

}
