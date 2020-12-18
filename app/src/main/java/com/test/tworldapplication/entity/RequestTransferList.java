package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 16/11/5.
 */

public class RequestTransferList {
    private List<OrderGH> order;
    private Integer count;

    public List<OrderGH> getOrder() {
        return order;
    }

    public void setOrder(List<OrderGH> order) {
        this.order = order;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
