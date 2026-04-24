package com.clean.demo.dto.analytics;

import java.util.List;

import lombok.Data;

@Data
public class TotalResponse {
    private String title;
    private String subtitle;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public static List<TotalResponse> getTotalResponse(Double totalRevenue, Integer totalOrders, Integer totalCustomers) {
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTitle("Total Revenue");
        totalResponse.setSubtitle(String.format("%.2f", totalRevenue));

        TotalResponse ordersResponse = new TotalResponse();
        ordersResponse.setTitle("Total Orders");
        ordersResponse.setSubtitle(totalOrders.toString());

        TotalResponse customersResponse = new TotalResponse();
        customersResponse.setTitle("Total Customers");
        customersResponse.setSubtitle(totalCustomers.toString());

        return List.of(totalResponse, ordersResponse, customersResponse);
    }
}
