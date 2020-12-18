package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/12/8.
 */

public class RequestRechargeDiscount {
    private Discount[] discount;
    private int count;

    public Discount[] getDiscount() {
        return discount;
    }

    public void setDiscount(Discount[] discount) {
        this.discount = discount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
