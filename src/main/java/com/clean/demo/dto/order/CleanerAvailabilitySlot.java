package com.clean.demo.dto.order;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleanerAvailabilitySlot {
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer availableCleaners;
    private Double totalPrice;
}
