package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 16/11/7.
 */

public class RequestOrderList {
    private List<OrderKH> order;
//    private OrderKH[] order;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<OrderKH> getOrder() {
        return order;
    }

    public void setOrder(List<OrderKH> order) {
        this.order = order;
    }
}
