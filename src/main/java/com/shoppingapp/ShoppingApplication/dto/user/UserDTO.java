package com.shoppingapp.ShoppingApplication.dto.user;

import lombok.Builder;

import java.util.List;

@Builder
public class UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleDTO> roles;
}