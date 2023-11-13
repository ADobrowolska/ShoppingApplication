package com.shoppingapp.ShoppingApplication.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleDTO> roles;
}
