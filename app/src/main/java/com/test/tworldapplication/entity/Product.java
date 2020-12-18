package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 17/5/31.
 */

public class Product implements Serializable {
    private String productId;
    private String productName;
    private String prodOfferId;
    private String prodOfferName;
        private String prodOfferDesc;
    private String ifSelectable;
    private String isModel;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(String prodOfferId) {
        this.prodOfferId = prodOfferId;
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

    public String getIfSelectable() {
        return ifSelectable;
    }

    public void setIfSelectable(String ifSelectable) {
        this.ifSelectable = ifSelectable;
    }

    public String getIsModel() {
        return isModel;
    }

    public void setIsModel(String isModel) {
        this.isModel = isModel;
    }
}
