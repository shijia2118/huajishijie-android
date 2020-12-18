package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/3.
 */

public class RequestAdmin {
    private String username;
    private String contact;
    private String tel;
    private String email;
    private String channelName;
    private String supUserName;
    private String supTel;
    private String supRecomdCode;
    private String recomdCode;
    private String address;
    private String workAddress;

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSupUserName() {
        return supUserName;
    }

    public void setSupUserName(String supUserName) {
        this.supUserName = supUserName;
    }

    public String getSupTel() {
        return supTel;
    }

    public void setSupTel(String supTel) {
        this.supTel = supTel;
    }

    public String getSupRecomdCode() {
        return supRecomdCode;
    }

    public void setSupRecomdCode(String supRecomdCode) {
        this.supRecomdCode = supRecomdCode;
    }

    public String getRecomdCode() {
        return recomdCode;
    }

    public void setRecomdCode(String recomdCode) {
        this.recomdCode = recomdCode;
    }
}
