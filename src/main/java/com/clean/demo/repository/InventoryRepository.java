package com.clean.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clean.demo.entity.Image;
import com.clean.demo.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByFeaturedImage(Image image);
}