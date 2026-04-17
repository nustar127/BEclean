package com.clean.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.clean.demo.dto.CleanerAvailabilitySlot;
import com.clean.demo.dto.OrderCheckRequest;
import com.clean.demo.entity.Order;
import com.clean.demo.entity.OrderStatus;
import com.clean.demo.entity.Service;
import com.clean.demo.entity.CartLine;
import com.clean.demo.entity.Cleaner;
import com.clean.demo.repository.OrderRepository;
import com.clean.demo.repository.ServiceRepository;

@org.springframework.stereotype.Service
public class OrderService {
    private static final int MAX_AVAILABLE_CLEANERS = 10;
    private static final int DEFAULT_SLOT_DURATION_MINUTES = 60;
    private static final int DAYS_IN_MONTH = 30;
    private static final int WORKDAY_START_HOUR = 9;
    private static final int WORKDAY_END_HOUR = 18;

    @Autowired
    private OrderRepository orderRepository;

     @Autowired
    private ServiceRepository serviceRepository;

    public Order addCleaner(Long id, Cleaner cleaner) {
        return orderRepository.findById(id)
                .map(order -> {
                    if (!order.isClaimable()) {
                        throw new RuntimeException("Order is not available for cleaner assignments");
                    }
                    order.addCleaner(cleaner);
                    if (order.getRemainingCleanerSlots() == 0) {
                        order.setStatus(OrderStatus.ACCEPTED);
                    }
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order changeStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<CleanerAvailabilitySlot> getAvailableSlots(OrderCheckRequest request) {
        Order tempOrder = new Order();
        List<CartLine> lines = request.getItems().stream().map(dto -> {
            CartLine line = new CartLine();

            Service service = serviceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Сервис не найден: " + dto.getServiceId()));

            line.setService(service);
            line.setQuantity(dto.getQuantity());
            line.setArea(dto.getArea());
            line.setOrder(tempOrder); 
            return line;
        }).collect(Collectors.toList());

        tempOrder.setItems(lines);
        tempOrder.setRequestedCleanerCount(request.getRequestedCleanerCount());
        tempOrder.setAppointmentDate(request.getAppointmentDate());
        tempOrder.setAddress(request.getAddress());
        tempOrder.recalculateTotals();

        int requested = tempOrder.getRequestedCleanerCount() != null && tempOrder.getRequestedCleanerCount() > 0
                ? tempOrder.getRequestedCleanerCount()
                : 1;

        if (requested > MAX_AVAILABLE_CLEANERS) {
            return new ArrayList<>();
        }

        int duration = tempOrder.getTotalTime() != null ? tempOrder.getTotalTime() : DEFAULT_SLOT_DURATION_MINUTES;
        LocalDateTime firstSlotStart = tempOrder.getAppointmentDate() != null
                ? tempOrder.getAppointmentDate()
                : LocalDateTime.now().plusDays(1).withHour(WORKDAY_START_HOUR).withMinute(0).withSecond(0).withNano(0);

        Double totalPrice = tempOrder.getTotalPrice() != null ? tempOrder.getTotalPrice() : 0.0;

        List<CleanerAvailabilitySlot> slots = new ArrayList<>();
        for (int day = 0; day < DAYS_IN_MONTH; day++) {
            LocalDateTime currentDayStart = firstSlotStart.plusDays(day).withHour(WORKDAY_START_HOUR).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime currentDayEnd = currentDayStart.withHour(WORKDAY_END_HOUR);

            LocalDateTime slotStart = currentDayStart;
            while (!slotStart.plusMinutes(duration).isAfter(currentDayEnd)) {
                LocalDateTime slotEnd = slotStart.plusMinutes(duration);
                slots.add(new CleanerAvailabilitySlot(slotStart, slotEnd, MAX_AVAILABLE_CLEANERS - requested, totalPrice));
                slotStart = slotStart.plusMinutes(duration);
            }
        }

        return slots;
    }
}
