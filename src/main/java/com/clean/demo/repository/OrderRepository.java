package com.clean.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
    
}
