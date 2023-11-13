package com.shoppingapp.ShoppingApplication.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public static Category ofId(Integer id){
        Category category = new Category();
        category.setId(id);
        return category;
    }

}
