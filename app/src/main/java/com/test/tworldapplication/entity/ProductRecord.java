package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 2018/1/4.
 */

public class ProductRecord implements Serializable {
    private String number;
    private String prodOfferName;
    private String prodOfferDesc;
    private String orderStatusName;
    private String createDate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getProdOfferDesc() {
        return prodOfferDesc;
    }

    public void setProdOfferDesc(String prodOfferDesc) {
        this.prodOfferDesc = prodOfferDesc;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
