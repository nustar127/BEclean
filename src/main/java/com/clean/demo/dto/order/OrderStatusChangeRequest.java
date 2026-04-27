package com.clean.demo.dto.order;

import com.clean.demo.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangeRequest {
    private OrderStatus status;
}
