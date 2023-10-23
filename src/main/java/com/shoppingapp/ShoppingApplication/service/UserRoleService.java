package com.shoppingapp.ShoppingApplication.service;

import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.model.UserRole;
import com.shoppingapp.ShoppingApplication.repository.UserRoleRepository;
import com.shoppingapp.ShoppingApplication.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole addUserRole(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);
        return userRoleRepository.save(userRole);
    }


}
