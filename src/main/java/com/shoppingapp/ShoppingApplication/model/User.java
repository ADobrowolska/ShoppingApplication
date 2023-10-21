package com.shoppingapp.ShoppingApplication.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "application_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<UserRole> userRoles;

}
