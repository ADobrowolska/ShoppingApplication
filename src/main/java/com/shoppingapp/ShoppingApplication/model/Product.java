package com.shoppingapp.ShoppingApplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int quantity;

    @Column(name = "shopping_list_id", insertable = false, updatable = false)
    private Integer shoppingListId;

    @ToString.Exclude
    @ManyToOne
    @JsonBackReference
    private ShoppingList shoppingList;

    @ManyToOne
    private Category category; // (category_id)

}
