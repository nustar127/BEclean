package com.clean.demo.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.clean.demo.dto.analytics.MonthlyOrdersChartItem;
import com.clean.demo.repository.OrderRepository;
import com.clean.demo.repository.PersonRepository;

@org.springframework.stereotype.Service
public class AnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PersonRepository personRepository;

    public Double calculateTotalRevenueForLastMonth() {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        Double totalRevenue = orderRepository.sumOrdersFromLastMonth(monthAgo);

        return totalRevenue;
    }

    public Integer calculateTotalCustomers() {
        return personRepository.sumCustomersAmount();
    }

    public Integer calculateTotalOrders() {
        return orderRepository.sumTotalOrders();
    }

    public List<Object[]> getTopCleaners() {
        return orderRepository.findOrdersCountByCleaner();
    }

    public List<MonthlyOrdersChartItem> getOrdersPerMonth() {
        LocalDateTime endDate = LocalDateTime.now().withDayOfMonth(1).plusMonths(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime startDate = endDate.minusMonths(6);

        List<Object[]> rows = orderRepository.countOrdersGroupedByMonth(startDate, endDate);
        Map<String, Long> monthlyCounts = new LinkedHashMap<>();

        LocalDateTime cursor = startDate;
        while (cursor.isBefore(endDate)) {
            String monthName = cursor.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlyCounts.put(monthName, 0L);
            cursor = cursor.plusMonths(1);
        }

        for (Object[] row : rows) {
            Integer monthNumber = ((Number) row[1]).intValue();
            Long orderCount = ((Number) row[2]).longValue();
            String monthName = Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlyCounts.put(monthName, orderCount);
        }

        List<MonthlyOrdersChartItem> result = new ArrayList<>();
        monthlyCounts.forEach((month, orders) -> result.add(new MonthlyOrdersChartItem(month, orders)));
        return result;
    }
}
