package com.shoppingapp.ShoppingApplication.dto.user;

import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.util.Role;

import java.util.List;

public class UserDTOMapper {

    public static List<UserDTO> mapToUserDTOs(List<User> users) {
        return users.stream()
                .map(user -> mapToUserDTO(user))
                .toList();
    }

    public static UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getUserRoles().stream()
                        .map(r -> r.getRole())
                        .map(e -> RoleDTO.valueOf(e.name()))
                        .toList())
                .build();
    }

    public static User mapUserDTOToUserModel(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .build();
    }


}
