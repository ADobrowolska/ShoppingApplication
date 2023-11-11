package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.user.CreateUserDTO;
import com.shoppingapp.ShoppingApplication.dto.user.UserDTO;
import com.shoppingapp.ShoppingApplication.dto.user.UserDTOMapper;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;

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
    public ResponseEntity<UserDTO> addNewUser(@RequestBody CreateUserDTO userDTO)
            throws InstanceAlreadyExistsException {
        User user = UserDTOMapper.mapUserDTOToUserModel(userDTO);
        UserDTO localUserDTO = UserDTOMapper.mapToUserDTO(userService.addUser(user));
        return ResponseEntity.ok(localUserDTO);

    }


}


