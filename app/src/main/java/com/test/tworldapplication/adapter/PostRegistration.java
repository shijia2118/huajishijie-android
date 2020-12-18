package com.test.tworldapplication.adapter;

/**
 * Created by dasiy on 2018/4/12.
 */

public class PostRegistration {
    private String session_token;
    private String customerName;
    private String certificatesNo;
    private String address;
    private String photoFront;
    private String photoBack;
    private String memo4;
    private String orderNo;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCertificatesNo() {
        return certificatesNo;
    }

    public void setCertificatesNo(String certificatesNo) {
        this.certificatesNo = certificatesNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoFront() {
        return photoFront;
    }

    public void setPhotoFront(String photoFront) {
        this.photoFront = photoFront;
    }

    public String getPhotoBack() {
        return photoBack;
    }

    public void setPhotoBack(String photoBack) {
        this.photoBack = photoBack;
    }

    public String getMemo4() {
        return memo4;
    }

    public void setMemo4(String memo4) {
        this.memo4 = memo4;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
