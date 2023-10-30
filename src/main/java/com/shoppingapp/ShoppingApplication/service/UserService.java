package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.model.UserRole;
import com.shoppingapp.ShoppingApplication.repository.UserRepository;
import com.shoppingapp.ShoppingApplication.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserRoleService userRoleService;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional
    public User addUser(User user) throws InstanceAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())) {
            if (validateEmail(user)) {
                User savedUser = userRepository.save(user);
                UserRole userRole = userRoleService.addUserRole(savedUser, Role.USER);
                savedUser.setUserRoles(new ArrayList<>(List.of(userRole)));
                return userRepository.save(savedUser);
            } else {
                throw new IllegalArgumentException("Invalid email address!");
            }
        } else {
            throw new InstanceAlreadyExistsException("User already exists!");
        }
    }


    boolean validateEmail(User user) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(user.getEmail());
        return matcher.matches();
    }



}
