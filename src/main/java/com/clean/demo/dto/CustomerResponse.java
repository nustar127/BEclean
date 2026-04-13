package com.clean.demo.dto;

import com.clean.demo.entity.Customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CustomerResponse extends UserResponse {
    private String discountCard;
    private String address;

    public static CustomerResponse createCustomer(Customer user) {
        return CustomerResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .discountCard(user.getDiscountCard())
                .role(user.getRole().name())
                .build();
    }
}
