package com.clean.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.Image;
import com.clean.demo.entity.Inventory;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    List<Inventory> findAllByFeaturedImage(Image image);
}
