package com.shoppingapp.ShoppingApplication.repository;

import com.shoppingapp.ShoppingApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

}
