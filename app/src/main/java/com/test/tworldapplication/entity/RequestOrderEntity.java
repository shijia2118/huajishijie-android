package com.test.tworldapplication.entity;

import java.util.List;

public class RequestOrderEntity {
    private Integer count;
    private List<OrderEntity> orders;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }
}
