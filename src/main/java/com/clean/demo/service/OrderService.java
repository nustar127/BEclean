package com.clean.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.clean.demo.dto.CleanerAvailabilitySlot;
import com.clean.demo.dto.OrderCheckRequest;
import com.clean.demo.dto.OrderCreationRequest;
import com.clean.demo.entity.Order;
import com.clean.demo.entity.OrderStatus;
import com.clean.demo.entity.Service;
import com.clean.demo.entity.CartLine;
import com.clean.demo.entity.Cleaner;
import com.clean.demo.entity.Person;
import com.clean.demo.repository.CleanerRepository;
import com.clean.demo.repository.OrderRepository;
import com.clean.demo.repository.ServiceRepository;
import com.clean.demo.repository.PersonRepository;

@org.springframework.stereotype.Service
public class OrderService {
    private static final int MAX_AVAILABLE_CLEANERS = 10;
    private static final int DEFAULT_SLOT_DURATION_MINUTES = 60;
    private static final int TRAVEL_TIME_MINUTES = 60;
    private static final int DAYS_IN_MONTH = 30;
    private static final int WORKDAY_START_HOUR = 9;
    private static final int WORKDAY_END_HOUR = 21;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CleanerRepository cleanerRepository;

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByCleanerId(Long cleanerId) {
        return orderRepository.findByCleanersId(cleanerId);
    }

    public List<Order> findOrdersWithUnassignedCleanerSlots(Long cleanerId) {
        Cleaner cleaner = cleanerRepository.findById(cleanerId)
                .orElseThrow(() -> new RuntimeException("Cleaner not found: " + cleanerId));

        return findAll().stream()
                .filter(Order::isClaimable)
                .filter(order -> isCleanerAvailableForOrder(cleaner, order))
                .toList();
    }

    public Order addCleaner(Long orderId, Long cleanerId) {
        Cleaner cleaner = cleanerRepository.findById(cleanerId)
                .orElseThrow(() -> new RuntimeException("Cleaner not found: " + cleanerId));
        return addCleaner(orderId, cleaner);
    }

    public Order addCleaner(Long id, Cleaner cleaner) {
        return orderRepository.findById(id)
                .map(order -> {
                    if (!order.isClaimable()) {
                        throw new RuntimeException("Order is not available for cleaner assignments");
                    }
                    order.addCleaner(cleaner);
                    if (order.getRemainingCleanerSlots() == 0) {
                        order.setStatus(OrderStatus.CONFIRMED);
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

        long totalCleanerCount = cleanerRepository.count();
        if (totalCleanerCount == 0 || requested > totalCleanerCount || requested > MAX_AVAILABLE_CLEANERS) {
            return new ArrayList<>();
        }

        int duration = tempOrder.getTotalTime() != null ? tempOrder.getTotalTime() : DEFAULT_SLOT_DURATION_MINUTES;
        int durationWithTravel = duration + TRAVEL_TIME_MINUTES;
        LocalDateTime firstSlotStart = tempOrder.getAppointmentDate() != null
                ? tempOrder.getAppointmentDate()
                : LocalDateTime.now().plusDays(1).withHour(WORKDAY_START_HOUR).withMinute(0).withSecond(0).withNano(0);

        Double totalPrice = tempOrder.getTotalPrice() != null ? tempOrder.getTotalPrice() : 0.0;

        List<CleanerAvailabilitySlot> slots = new ArrayList<>();
        for (int day = 0; day < DAYS_IN_MONTH; day++) {
            LocalDateTime currentDayStart = day == 0
                    ? normalizeFirstSlotStart(firstSlotStart)
                    : firstSlotStart.plusDays(day).withHour(WORKDAY_START_HOUR).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime currentDayEnd = currentDayStart.withHour(WORKDAY_END_HOUR);

            LocalDateTime slotStart = currentDayStart;
            while (!slotStart.plusMinutes(durationWithTravel).isAfter(currentDayEnd)) {
                LocalDateTime slotEnd = slotStart.plusMinutes(duration);
                int availableCleaners = countAvailableCleaners(slotStart, durationWithTravel, totalCleanerCount);
                if (availableCleaners >= requested) {
                    slots.add(new CleanerAvailabilitySlot(slotStart, slotEnd, availableCleaners, totalPrice));
                }
                slotStart = slotStart.plusMinutes(durationWithTravel);
            }
        }

        return slots;
    }

    private LocalDateTime normalizeFirstSlotStart(LocalDateTime firstSlotStart) {
        LocalDateTime normalized = firstSlotStart.withSecond(0).withNano(0);
        if (normalized.getHour() < WORKDAY_START_HOUR) {
            return normalized.withHour(WORKDAY_START_HOUR).withMinute(0);
        }
        if (normalized.getHour() >= WORKDAY_END_HOUR) {
            return normalized.plusDays(1).withHour(WORKDAY_START_HOUR).withMinute(0);
        }
        return normalized;
    }

    private int countAvailableCleaners(LocalDateTime slotStart, int durationWithTravel, long totalCleanerCount) {
        LocalDateTime slotBusyUntil = slotStart.plusMinutes(durationWithTravel);
        int reservedCleaners = 0;

        for (Order existingOrder : findAll()) {
            if (!blocksCleanerAvailability(existingOrder)) {
                continue;
            }

            LocalDateTime existingStart = existingOrder.getAppointmentDate();
            int existingDuration = existingOrder.getTotalTime() != null ? existingOrder.getTotalTime() : DEFAULT_SLOT_DURATION_MINUTES;
            LocalDateTime existingBusyUntil = existingStart.plusMinutes(existingDuration + TRAVEL_TIME_MINUTES);

            if (slotStart.isBefore(existingBusyUntil) && existingStart.isBefore(slotBusyUntil)) {
                reservedCleaners += getReservedCleanerCount(existingOrder);
            }
        }

        return Math.max(0, (int) totalCleanerCount - reservedCleaners);
    }

    private boolean blocksCleanerAvailability(Order order) {
        if (order.getAppointmentDate() == null) {
            return false;
        }

        OrderStatus status = order.getStatus();
        return status != OrderStatus.CANCELLED && status != OrderStatus.COMPLETED;
    }

    private int getReservedCleanerCount(Order order) {
        Integer requestedCleanerCount = order.getRequestedCleanerCount();
        return requestedCleanerCount != null && requestedCleanerCount > 0 ? requestedCleanerCount : 1;
    }

    private boolean isCleanerAvailableForOrder(Cleaner cleaner, Order candidateOrder) {
        if (candidateOrder.getCleaners() != null && candidateOrder.getCleaners().contains(cleaner)) {
            return false;
        }
        if (candidateOrder.getAppointmentDate() == null) {
            return false;
        }

        LocalDateTime candidateStart = candidateOrder.getAppointmentDate();
        int candidateDuration = candidateOrder.getTotalTime() != null ? candidateOrder.getTotalTime() : DEFAULT_SLOT_DURATION_MINUTES;
        LocalDateTime candidateBusyUntil = candidateStart.plusMinutes(candidateDuration + TRAVEL_TIME_MINUTES);

        return findByCleanerId(cleaner.getId()).stream()
                .filter(this::blocksCleanerAvailability)
                .noneMatch(existingOrder -> overlaps(existingOrder, candidateStart, candidateBusyUntil));
    }

    private boolean overlaps(Order existingOrder, LocalDateTime candidateStart, LocalDateTime candidateBusyUntil) {
        LocalDateTime existingStart = existingOrder.getAppointmentDate();
        int existingDuration = existingOrder.getTotalTime() != null ? existingOrder.getTotalTime() : DEFAULT_SLOT_DURATION_MINUTES;
        LocalDateTime existingBusyUntil = existingStart.plusMinutes(existingDuration + TRAVEL_TIME_MINUTES);

        return candidateStart.isBefore(existingBusyUntil) && existingStart.isBefore(candidateBusyUntil);
    }

    public Order createOrder(OrderCreationRequest request) {
        // Validate request
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        if (request.getCustomerId() == null && (request.getEmail() == null || request.getFirstName() == null || request.getLastName() == null)) {
            throw new IllegalArgumentException("Either customerId or person details (email, firstName, lastName) must be provided");
        }

        // Create or find customer/person
        Person customer;
        if (request.getCustomerId() != null) {
            customer = personRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Person not found: " + request.getCustomerId()));
        } else {
            // Create new person
            customer = Person.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .build();
            customer = personRepository.save(customer);
        }

        // Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setRequestedCleanerCount(request.getRequestedCleanerCount());
        order.setAppointmentDate(request.getAppointmentDate());
        order.setAddress(request.getAddress());

        // Create cart lines
        List<CartLine> cartLines = request.getItems().stream().map(dto -> {
            Service service = serviceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found: " + dto.getServiceId()));
            CartLine line = new CartLine();
            line.setService(service);
            line.setQuantity(dto.getQuantity());
            line.setArea(dto.getArea());
            line.setOrder(order);
            return line;
        }).collect(Collectors.toList());

        order.setItems(cartLines);
        order.recalculateTotals();

        return orderRepository.save(order);
    }

}
