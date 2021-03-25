package com.test.tworldapplication.entity;

public class PostBkApply {
    private String session_token;
    private String name;
    private String tel;
    private String deliveryAddress;
    private String applySum;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getApplySum() {
        return applySum;
    }

    public void setApplySum(String applySum) {
        this.applySum = applySum;
    }
}
