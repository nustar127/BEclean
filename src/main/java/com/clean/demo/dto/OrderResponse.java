package com.clean.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clean.demo.entity.Customer;
import com.clean.demo.entity.Order;
import com.clean.demo.entity.Person;
import com.clean.demo.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private PersonResponse customer;
    private List<CleanerResponse> cleaners;
    private String status;
    private Double totalPrice;
    private Integer totalTime;
    private LocalDateTime appointmentDate;
    private Integer requestedCleanerCount;
    private String address;
    private Integer remainingCleanerSlots;

    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customer(toPersonResponse(order.getCustomer()))
                .cleaners(order.getCleaners().stream()
                        .map(CleanerResponse::createCustomer)
                        .toList())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .totalPrice(order.getTotalPrice())
                .totalTime(order.getTotalTime())
                .appointmentDate(order.getAppointmentDate())
                .requestedCleanerCount(order.getRequestedCleanerCount())
                .address(order.getAddress())
                .remainingCleanerSlots(order.getRemainingCleanerSlots())
                .build();
    }

    private static PersonResponse toPersonResponse(Person person) {
        if (person == null) {
            return null;
        }
        if (person instanceof Customer customer) {
            return CustomerResponse.createCustomer(customer);
        }
        if (person instanceof User user) {
            return UserResponse.createUser(user);
        }
        return PersonResponse.createPerson(person);
    }
}
