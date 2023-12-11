package com.shoppingapp.ShoppingApplication.repository;

import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {

    @Modifying
    @Query("DELETE FROM ShoppingList sl WHERE sl.timeOfLastEditing < :date")
    int deleteOldShoppingList(@Param("date")Instant date);

    @Query("SELECT sl FROM ShoppingList sl WHERE sl.timeOfLastEditing < :date")
    List<ShoppingList> getOldShoppingLists(@Param("date")Instant date);

    List<ShoppingList> findAllByTimeOfLastEditingLessThan(Instant instant);

    List<ShoppingList> findAllByTimeOfLastEditingLessThanAndUserId(Instant instant, int userId);

    List<ShoppingList> findAllByUserId(int userId);

    Optional<ShoppingList> findByIdAndUserId(int id, int userId);


}
