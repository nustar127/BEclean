package com.clean.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.clean.demo.entity.Order;
import com.clean.demo.entity.OrderStatus;
import com.clean.demo.entity.Cleaner;
import com.clean.demo.repository.OrderRepository;
import com.clean.demo.repository.UserRepository;

@org.springframework.stereotype.Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public Order addCleaner(Long id, Cleaner cleaner) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.addCleaner(cleaner);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order changeStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
