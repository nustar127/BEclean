package com.clean.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.clean.demo.entity.Image;
import com.clean.demo.entity.Inventory;
import com.clean.demo.repository.ImageRepository;
import com.clean.demo.repository.InventoryRepository;

@org.springframework.stereotype.Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    public Inventory updateService(Long id, Inventory newInventory) {
        return inventoryRepository.findById(id)
                .map(inventory -> {

                    if (newInventory.getName() != null) {
                        inventory.setName(newInventory.getName());
                    }
                    if (newInventory.getDescription() != null) {
                        inventory.setDescription(newInventory.getDescription());
                    }
                    if (newInventory.getAmount() != null) {
                        inventory.setAmount(newInventory.getAmount());
                    }
                    if (newInventory.getUnit() != null) {
                        inventory.setUnit(newInventory.getUnit());
                    }

                    if (newInventory.getFeaturedImage() != null &&
                            newInventory.getFeaturedImage().getId() != null) {

                        Image managedImage = imageRepository
                                .findById(newInventory.getFeaturedImage().getId())
                                .orElseThrow(() -> new RuntimeException("Image not found"));

                        inventory.setFeaturedImage(managedImage);
                    }

                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

}
