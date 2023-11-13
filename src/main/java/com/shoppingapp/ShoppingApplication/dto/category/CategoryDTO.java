package com.shoppingapp.ShoppingApplication.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryDTO {

    private Integer id;
    private String name;

}
