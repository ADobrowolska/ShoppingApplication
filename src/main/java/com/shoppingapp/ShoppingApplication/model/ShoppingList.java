package com.shoppingapp.ShoppingApplication.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Builder
public class ShoppingList {

    public ShoppingList(int id, String name, Instant timeOfLastEditing, List<Product> products, User user) {
        this.id = id;
        this.name = name;
        this.timeOfLastEditing = timeOfLastEditing;
        this.products = products;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "time_act")
    private Instant timeOfLastEditing;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "shopping_list_id")
    @JsonManagedReference
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private User user;

}
