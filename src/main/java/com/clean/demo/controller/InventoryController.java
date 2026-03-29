package com.clean.demo.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.clean.demo.entity.Inventory;
import com.clean.demo.repository.InventoryRepository;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("inventory")
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("")
    private Iterable<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

     @GetMapping("/{id}")
    public Optional<Inventory> findById(@PathVariable("id") Long id) {
        return inventoryRepository.findById(id);
    }

    @PostMapping("")
    public Inventory newInventory(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @DeleteMapping("/{id}")
    void deleteInventory(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
    }

    @DeleteMapping("")
    void deleteInventories(@RequestBody Iterable<Long> ids) {
        inventoryRepository.deleteAllById(ids);
    }
}
