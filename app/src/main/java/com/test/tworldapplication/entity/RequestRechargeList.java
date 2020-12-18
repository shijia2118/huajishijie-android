package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 16/11/7.
 */

public class RequestRechargeList {
    private List<OrderCz> order;
    private Integer count;

    public List<OrderCz> getOrder() {
        return order;
    }

    public void setOrder(List<OrderCz> order) {
        this.order = order;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
