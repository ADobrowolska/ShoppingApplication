package com.shoppingapp.ShoppingApplication.model;

import com.sun.istack.NotNull;
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
    private Integer id;
    private String name;
    private int quantity;

    @ToString.Exclude
    @ManyToOne
    private ShoppingList shoppingList;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
