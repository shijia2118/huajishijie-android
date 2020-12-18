package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 2018/1/5.
 */

public class PostOrderProduct {
    private String session_token;
    private String number;
    private String verificationCode;
    private String productId;
    private String productName;
    private String prodOfferId;
    private String prodOfferName;
    private String prodOfferDesc;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

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
}
