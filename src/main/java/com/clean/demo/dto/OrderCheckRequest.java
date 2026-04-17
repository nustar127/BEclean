package com.clean.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clean.demo.entity.CartLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCheckRequest {
    private List<CartItemDto> items;
    private Integer requestedCleanerCount = 1;
    private LocalDateTime appointmentDate;
    private String address;
}
