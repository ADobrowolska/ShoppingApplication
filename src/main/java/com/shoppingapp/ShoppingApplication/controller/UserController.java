package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.user.UserDTO;
import com.shoppingapp.ShoppingApplication.dto.user.UserDTOMapper;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public UserDTO getSingleUser(@PathVariable int id) {
        return UserDTOMapper.mapToUserDTO(userService.getUser(id));
    }

    @PostMapping("/users/")
    public UserDTO addNewUser(@RequestBody UserDTO userDTO) {
        User user = UserDTOMapper.mapUserDTOToUserModel(userDTO);
        return UserDTOMapper.mapToUserDTO(userService.addUser(user));
    }




}
