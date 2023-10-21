package com.shoppingapp.ShoppingApplication.controller;

import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTO;
import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTOMapper;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class ShoppingListController {

    private ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/shopping/{id}")
    public ShoppingListDTO getSingleShoppingList(@PathVariable int id) {
        return ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.getSingleShoppingList(id));
    }

    @GetMapping("/shopping")
    public List<ShoppingListDTO> getShoppingLists() {
        return ShoppingListDTOMapper.mapToShoppingListDTOs(shoppingListService.getShoppingLists());
    }

    @PutMapping("/shopping/{id}")
    public ShoppingListDTO editSL(@RequestBody ShoppingListDTO shoppingListDTO) {
        ShoppingList shoppingList = ShoppingListDTOMapper.mapDTOToShoppingListModel(shoppingListDTO);
        return ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.editSL(shoppingList));
    }

    @PostMapping(value = "/shopping")
    public ShoppingListDTO addSL(@RequestBody ShoppingListDTO shoppingListDTO) {
        ShoppingList shoppingList = ShoppingListDTOMapper.mapDTOToShoppingListModel(shoppingListDTO);
        return ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.addSL(shoppingList));
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





