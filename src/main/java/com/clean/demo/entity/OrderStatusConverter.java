package com.clean.demo.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Short> {

    @Override
    public Short convertToDatabaseColumn(OrderStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return (short) attribute.ordinal();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Short dbData) {
        if (dbData == null) {
            return null;
        }

        OrderStatus[] values = OrderStatus.values();
        if (dbData < 0 || dbData >= values.length) {
            throw new IllegalArgumentException("Unknown OrderStatus value: " + dbData);
        }

        return values[dbData];
    }
}
