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

@RestController
public class ShoppingListController {

    private ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/shopping/{id}")
    public ResponseEntity<ShoppingListDTO> getSingleShoppingList(@PathVariable int id, @RequestHeader(value = "user-id") int userId) {
            ShoppingListDTO shoppingListDTO = ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.getSingleShoppingList(id, userId));
            return ResponseEntity.ok(shoppingListDTO);
    }

    @GetMapping("/shopping")
    public ResponseEntity<List<ShoppingListDTO>> getShoppingLists() {
        List<ShoppingListDTO> shoppingListDTOs = ShoppingListDTOMapper.mapToShoppingListDTOs(shoppingListService.getShoppingLists());
        return ResponseEntity.ok(shoppingListDTOs);
    }

    @GetMapping("/shopping/users/")
    public ResponseEntity<List<ShoppingListDTO>> getUserShoppingLists(@RequestHeader(value = "user-id") int userId) {
            List<ShoppingListDTO> shoppingListDTOList =
                    ShoppingListDTOMapper.mapToShoppingListDTOs(shoppingListService.getUserShoppingLists(userId));
            return ResponseEntity.ok(shoppingListDTOList);
        }


    @PutMapping("/shopping/{id}")
    public ResponseEntity<ShoppingListDTO> editSL(@RequestBody CreateShoppingListDTO shoppingListDTO) {
        ShoppingList shoppingList = ShoppingListDTOMapper.mapDTOToShoppingListModel(shoppingListDTO);
        ShoppingListDTO shoppingListDTO1 = ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.editSL(shoppingList));
        return ResponseEntity.ok(shoppingListDTO1);
    }

    @PostMapping(value = "/shopping")
    public ResponseEntity<ShoppingListDTO> addSL(@RequestBody CreateShoppingListDTO shoppingListDTO, @RequestHeader(value = "user-id") int userId) {
        ShoppingList shoppingList = ShoppingListDTOMapper.mapDTOToShoppingListModel(shoppingListDTO);
        ShoppingListDTO shoppingListDTO1 = ShoppingListDTOMapper.mapToShoppingListDTO(shoppingListService.addSL(shoppingList, userId));
        return ResponseEntity.ok(shoppingListDTO1);
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





