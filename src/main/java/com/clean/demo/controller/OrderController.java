package com.clean.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.dto.OrderCheckRequest;
import com.clean.demo.dto.OrderClaimRequest;
import com.clean.demo.dto.OrderCreationRequest;
import com.clean.demo.dto.OrderResponse;
import com.clean.demo.dto.OrderStatusChangeRequest;
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
    @PreAuthorize("permitAll()")
    public ApiResponse<Iterable<OrderResponse>> findAll() {
        Iterable<OrderResponse> orders = orderService.findAll().stream()
                .map(OrderResponse::fromOrder)
                .toList();
        return ApiResponse.success(orders, "Founded");
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ApiResponse<OrderResponse> findById(@PathVariable("id") Long id) {
        return orderService.findById(id)
                .map(order -> ApiResponse.success(OrderResponse.fromOrder(order), "Founded"))
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<Iterable<OrderResponse>> findByCustomerId(@PathVariable("customerId") Long customerId) {
        Iterable<OrderResponse> orders = orderService.findByCustomerId(customerId).stream()
                .map(OrderResponse::fromOrder)
                .toList();
        return ApiResponse.success(orders, "Founded");
    }

    @GetMapping("/cleaner/{cleanerId}")
    public ApiResponse<Iterable<OrderResponse>> findByCleanerId(@PathVariable("cleanerId") Long cleanerId) {
        Iterable<OrderResponse> orders = orderService.findByCleanerId(cleanerId).stream()
                .map(OrderResponse::fromOrder)
                .toList();
        return ApiResponse.success(orders, "Founded");
    }

    @GetMapping("/unassigned/{cleanerId}")
    public ApiResponse<Iterable<OrderResponse>> findOrdersWithUnassignedCleanerSlots(@PathVariable("cleanerId") Long cleanerId) {
        Iterable<OrderResponse> orders = orderService.findOrdersWithUnassignedCleanerSlots(cleanerId).stream()
                .map(OrderResponse::fromOrder)
                .toList();
        return ApiResponse.success(orders, "Founded");
    }

    @PostMapping("/{id}/claim")
    public ApiResponse<OrderResponse> claimOrder(@PathVariable("id") Long id, @RequestBody OrderClaimRequest request) {
        return ApiResponse.success(OrderResponse.fromOrder(orderService.addCleaner(id, request.getCleanerId())), "Order claimed by cleaner");
    }

    @PostMapping("/{id}/change-status")
    public ApiResponse<OrderResponse> changeStatus(@PathVariable("id") Long id, @RequestBody OrderStatusChangeRequest request) {
        return ApiResponse.success(OrderResponse.fromOrder(orderService.changeStatus(id, request.getStatus())), "Status updated");
    }

    @PostMapping("/available-slots")
    @PreAuthorize("permitAll()")
    public ApiResponse<?> availableSlots(@RequestBody OrderCheckRequest request) {
        return ApiResponse.success(orderService.getAvailableSlots(request), "Available slots calculated");
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public ApiResponse<OrderResponse> newOrder(@RequestBody OrderCreationRequest request) {
        return ApiResponse.success(OrderResponse.fromOrder(orderService.createOrder(request)), "Order created");
    }
}
