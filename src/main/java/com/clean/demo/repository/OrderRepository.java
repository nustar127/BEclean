package com.clean.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);
    List<Order> findByCleanersId(Long cleanerId);
}
