package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/12/8.
 */

public class RequestOtherDiscount {
    private Double actualAmount;
    private Double discountAmount;

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
