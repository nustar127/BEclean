package com.clean.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.entity.Cleaner;
import com.clean.demo.entity.Order;
import com.clean.demo.entity.OrderStatus;
import com.clean.demo.repository.OrderRepository;
import com.clean.demo.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Iterable<Order>> findAll() {
        return ApiResponse.success(orderRepository.findAll(), "Founded");
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> findById(@PathVariable("id") Long id) {
        return orderRepository.findById(id)
                .map(order -> ApiResponse.success(order, "Founded"))
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @PostMapping("/{id}/add-cleaner")
    public ApiResponse<Order> addCleanerToOrder(@PathVariable("id") Long id, @RequestBody Cleaner cleaner) {
        Order updatedOrder = orderService.addCleaner(id, cleaner);
        return ApiResponse.success(orderRepository.save(updatedOrder), "Cleaners updated");
    }

    @PostMapping("/{id}/change-status")
    public ApiResponse<Order> changeStatus(@PathVariable("id") Long id, @RequestBody OrderStatus status) {
        Order updatedOrder = orderService.changeStatus(id, status);
        return ApiResponse.success(orderRepository.save(updatedOrder), "Status updated");
    }

    @PostMapping("")
    public ApiResponse<Order> newOrder(@RequestBody Order order) {
        return ApiResponse.success(orderRepository.save(order), "Order created");
    }
}
