package com.test.tworldapplication.entity;

import java.util.List;

public class RequestAuditList {
    private List<OrderAudit> orders;
    //    private OrderKH[] order;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<OrderAudit> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderAudit> orders) {
        this.orders = orders;
    }
}
