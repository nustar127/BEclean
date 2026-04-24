package com.clean.demo.dto.analytics;

public class MonthlyOrdersChartItem {
    private String month;
    private Long orders;

    public MonthlyOrdersChartItem() {
    }

    public MonthlyOrdersChartItem(String month, Long orders) {
        this.month = month;
        this.orders = orders;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getOrders() {
        return orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }
}
