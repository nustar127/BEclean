package com.clean.demo.dto.order;

import java.time.LocalDateTime;
import java.util.List;

import com.clean.demo.dto.CartItemDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {
    private List<CartItemDto> items;
    private Integer requestedCleanerCount = 1;
    private LocalDateTime appointmentDate;
    private String address;

    private Long customerId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
