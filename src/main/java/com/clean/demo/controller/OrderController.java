package com.clean.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.dto.OrderCheckRequest;
import com.clean.demo.dto.OrderCreationRequest;
import com.clean.demo.dto.OrderResponse;
import com.clean.demo.entity.Cleaner;
import com.clean.demo.entity.OrderStatus;
import com.clean.demo.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Iterable<OrderResponse>> findAll() {
        Iterable<OrderResponse> orders = orderService.findAll().stream()
                .map(OrderResponse::fromOrder)
                .toList();
        return ApiResponse.success(orders, "Founded");
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> findById(@PathVariable("id") Long id) {
        return orderService.findById(id)
                .map(order -> ApiResponse.success(OrderResponse.fromOrder(order), "Founded"))
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @PostMapping("/{id}/add-cleaner")
    public ApiResponse<OrderResponse> addCleanerToOrder(@PathVariable("id") Long id, @RequestBody Cleaner cleaner) {
        return ApiResponse.success(OrderResponse.fromOrder(orderService.addCleaner(id, cleaner)), "Cleaners updated");
    }

    @PostMapping("/{id}/claim")
    public ApiResponse<OrderResponse> claimOrder(@PathVariable("id") Long id, @RequestBody Cleaner cleaner) {
        return ApiResponse.success(OrderResponse.fromOrder(orderService.addCleaner(id, cleaner)), "Order claimed by cleaner");
    }

    @PostMapping("/{id}/change-status")
    public ApiResponse<OrderResponse> changeStatus(@PathVariable("id") Long id, @RequestBody OrderStatus status) {
        return ApiResponse.success(OrderResponse.fromOrder(orderService.changeStatus(id, status)), "Status updated");
    }

    @PostMapping("/available-slots")
    public ApiResponse<?> availableSlots(@RequestBody OrderCheckRequest request) {
        return ApiResponse.success(orderService.getAvailableSlots(request), "Available slots calculated");
    }

    @PostMapping("")
    public ApiResponse<OrderResponse> newOrder(@RequestBody OrderCreationRequest request) {
        return ApiResponse.success(OrderResponse.fromOrder(orderService.createOrder(request)), "Order created");
    }
}
