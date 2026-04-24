package com.clean.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.dto.analytics.MonthlyOrdersChartItem;
import com.clean.demo.dto.analytics.TotalResponse;
import com.clean.demo.service.AnalyticsService;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/total")
    public ApiResponse<List<TotalResponse>> getTotalAnalytics() {
        Double totalRevenue = analyticsService.calculateTotalRevenueForLastMonth();
        Integer totalOrders = analyticsService.calculateTotalOrders();
        Integer totalCustomers = analyticsService.calculateTotalCustomers();

        List<TotalResponse> totalResponses = TotalResponse.getTotalResponse(totalRevenue, totalOrders, totalCustomers);
        return ApiResponse.success(totalResponses, "Total analytics calculated successfully");
    }

    @GetMapping("/top-cleaners")
    public ApiResponse<List<Map<String, Object>>> getTopCleaners() {
        List<Object[]> topCleaners = analyticsService.getTopCleaners();

        if (!topCleaners.isEmpty()) {
            List<Map<String, Object>> data = topCleaners.stream()
                    .map(row -> {
                        Map<String, Object> cleaner = new HashMap<>();
                        cleaner.put("id", row[0]);
                        cleaner.put("firstName", row[1]);
                        cleaner.put("lastName", row[2]);
                        cleaner.put("email", row[3]);
                        cleaner.put("phone", row[4]);
                        cleaner.put("experience", row[5]);
                        cleaner.put("rating", row[6]);
                        cleaner.put("orderCount", row[7]);
                        return cleaner;
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success(data, "Top cleaners retrieved successfully");
        }
        return ApiResponse.success(List.of(), "No cleaners found");
    }

    @GetMapping("/orders-per-month")
    public ApiResponse<List<MonthlyOrdersChartItem>> getOrdersPerMonth() {
        return ApiResponse.success(analyticsService.getOrdersPerMonth(),
                "Monthly order analytics retrieved successfully");
    }
}
