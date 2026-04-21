package com.clean.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.entity.Inventory;
import com.clean.demo.repository.InventoryRepository;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Iterable<Inventory>> findAll() {
        return ApiResponse.success(inventoryRepository.findAll(), "Founded");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Inventory> findById(@PathVariable("id") Long id) {
        return inventoryRepository.findById(id)
                .map(inventory -> ApiResponse.success(inventory, "Founded"))
                .orElseThrow(() -> new RuntimeException("Inventory item not found with id: " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Inventory> updateInventory(@PathVariable("id") Long id, @RequestBody Inventory inventory) {
        inventory.setId(id); 
        return ApiResponse.success(inventoryRepository.save(inventory), "Item updated successfully");
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Inventory> createInventory(@RequestBody Inventory inventory) {
        return ApiResponse.success(inventoryRepository.save(inventory), "Item created successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteInventory(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
        return ApiResponse.success(null, "Item deleted successfully");
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteInventories(@RequestBody Iterable<Long> ids) {
        inventoryRepository.deleteAllById(ids);
        return ApiResponse.success(null, "Selected items deleted successfully");
    }
}
