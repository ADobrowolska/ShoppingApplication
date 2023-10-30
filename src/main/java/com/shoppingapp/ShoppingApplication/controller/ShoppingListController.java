package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.shoppinglist.CreateShoppingListDTO;
import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTO;
import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTOMapper;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ShoppingListController {

    private ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/shopping/{id}")
    public ResponseEntity<ShoppingListDTO> getSingleShoppingList(@PathVariable int id, @RequestHeader(value = "user-id") int userId) {
        try {
            ShoppingListDTO shoppingListDTO = ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.getSingleShoppingList(id, userId));
            return ResponseEntity.ok(shoppingListDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/shopping")
    public List<ShoppingListDTO> getShoppingLists() {
        return ShoppingListDTOMapper.mapToShoppingListDTOs(shoppingListService.getShoppingLists());
    }

    @GetMapping("/shopping/users/")
    public ResponseEntity<List<ShoppingListDTO>> getUserShoppingLists(@RequestHeader(value = "user-id") int userId) {
        try {
            List<ShoppingListDTO> shoppingListDTOList =
                    ShoppingListDTOMapper.mapToShoppingListDTOs(shoppingListService.getUserShoppingLists(userId));
            return ResponseEntity.ok(shoppingListDTOList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/shopping/{id}")
    public ShoppingListDTO editSL(@RequestBody CreateShoppingListDTO shoppingListDTO) {
        ShoppingList shoppingList = ShoppingListDTOMapper.mapDTOToShoppingListModel(shoppingListDTO);
        return ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.editSL(shoppingList));
    }

    @PostMapping(value = "/shopping")
    public ShoppingListDTO addSL(@RequestBody CreateShoppingListDTO shoppingListDTO, @RequestHeader(value = "user-id") int userId) {
        ShoppingList shoppingList = ShoppingListDTOMapper.mapDTOToShoppingListModel(shoppingListDTO);
        return ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.addSL(shoppingList, userId));
    }

    @DeleteMapping("/shopping/{id}")
    public void deleteSLById(@PathVariable int id) {
        shoppingListService.deleteSLById(id);
    }

    @DeleteMapping("/shopping")
    public void deleteAllSLs() {
        shoppingListService.deleteAllSLs();
    }

    @DeleteMapping("/shopping/old")
    public void deleteOldShoppingList() {
        shoppingListService.deleteOldShoppingList2(Instant.now());
    }
}





