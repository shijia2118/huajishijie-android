package com.test.tworldapplication.entity;

import android.content.Intent;

import com.test.tworldapplication.adapter.OrderProduct;

import java.util.List;

/**
 * Created by dasiy on 2018/1/5.
 */

public class RequestOrderProduct {
    private List<ProductRecord> orderProductList;
    private Integer count;

    public List<ProductRecord> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<ProductRecord> orderProductList) {
        this.orderProductList = orderProductList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
