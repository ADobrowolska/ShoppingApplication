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

    @ToString.Exclude
    @ManyToOne
    private ShoppingList shoppingList;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
