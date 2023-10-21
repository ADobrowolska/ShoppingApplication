package com.shoppingapp.ShoppingApplication.model;

import com.shoppingapp.ShoppingApplication.util.Role;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Data
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private User user;

}


