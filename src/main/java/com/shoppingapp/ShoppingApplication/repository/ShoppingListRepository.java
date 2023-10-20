package com.shoppingapp.ShoppingApplication.repository;

import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {

    @Modifying
    @Query("DELETE FROM ShoppingList sl WHERE sl.timeOfLastEditing < :date")
    int deleteOldShoppingList(@Param("date")Instant date);

    @Query("SELECT sl FROM ShoppingList sl WHERE sl.timeOfLastEditing < :date")
    List<ShoppingList> getOldShoppingList(@Param("date")Instant date);

    List<ShoppingList> findAllByTimeOfLastEditingLessThan(Instant instant);

    void deleteAllByTimeOfLastEditingLessThan(Instant instant);

//    @Modifying
//    @Query(value = "DELETE FROM Shopping_List sl WHERE sl.time_act < :date", nativeQuery = true)
//    int deleteOldShoppingList2(@Param("date")Instant date);


}
