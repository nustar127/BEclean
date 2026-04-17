package com.clean.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalPrice;

    private Integer totalTime;

    private LocalDateTime appointmentDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("order")
    private List<CartLine> items = new ArrayList<>();

    @ManyToMany
    private List<Cleaner> cleaners = new ArrayList<>();

    private Integer requestedCleanerCount = 1;

    private String address;

    private static final int MAX_ORDER_TIME_MINUTES = 480; // 8 hours
    private static final int MAX_AVAILABLE_CLEANERS = 10;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public List<CartLine> getItems() {
        return items;
    }

    public void setItems(List<CartLine> items) {
        this.items = items;
    }

    public void addItem(CartLine item) {
        if (item != null) {
            item.setOrder(this);
            this.items.add(item);
        }
    }

    public void removeItem(CartLine item) {
        if (item != null) {
            item.setOrder(null);
            this.items.remove(item);
        }
    }

    public void recalculateTotals() {
        this.totalPrice = calculateTotalPrice();
        adjustCleanerCountForTime();
        this.totalTime = calculateAdjustedTotalTime();
    }

    public Double calculateTotalPrice() {
        return items.stream()
                .map(item -> item.getLineTotal())
                .filter(price -> price != null)
                .reduce(0.0, Double::sum);
    }

    public Integer calculateBaseTotalTime() {
        return items.stream()
                .filter(item -> item.getService() != null && item.getService().getTime() != null && item.getQuantity() != null)
                .mapToInt(item -> item.getService().getTime() * item.getQuantity())
                .sum();
    }

    public Integer calculateAdjustedTotalTime() {
        int baseTime = calculateBaseTotalTime();
        int cleaners = requestedCleanerCount != null && requestedCleanerCount > 0 ? requestedCleanerCount : 1;
        return baseTime / cleaners;
    }

    public void adjustCleanerCountForTime() {
        int baseTime = calculateBaseTotalTime();
        int cleaners = requestedCleanerCount != null && requestedCleanerCount > 0 ? requestedCleanerCount : 1;
        int adjustedTime = baseTime / cleaners;

        while (adjustedTime > MAX_ORDER_TIME_MINUTES && cleaners < MAX_AVAILABLE_CLEANERS) {
            cleaners++;
            adjustedTime = baseTime / cleaners;
        }

        this.requestedCleanerCount = cleaners;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Cleaner> getCleaners() {
        return cleaners;
    }

    public void setCleaners(List<Cleaner> cleaners) {
        this.cleaners = cleaners;
    }

    public Integer getRequestedCleanerCount() {
        return requestedCleanerCount;
    }

    public void setRequestedCleanerCount(Integer requestedCleanerCount) {
        this.requestedCleanerCount = requestedCleanerCount != null && requestedCleanerCount > 0 ? requestedCleanerCount : 1;
    }

    public int getRemainingCleanerSlots() {
        int requested = requestedCleanerCount != null ? requestedCleanerCount : 1;
        return Math.max(0, requested - (cleaners != null ? cleaners.size() : 0));
    }

    public boolean isClaimable() {
        return getRemainingCleanerSlots() > 0 && (status == null || status == OrderStatus.PENDING);
    }

    public void addCleaner(Cleaner cleaner) {
        if (cleaner == null) {
            return;
        }
        if (getRemainingCleanerSlots() <= 0) {
            throw new IllegalStateException("No cleaner slots available for this order");
        }
        if (!cleaners.contains(cleaner)) {
            cleaners.add(cleaner);
        }
    }
}
