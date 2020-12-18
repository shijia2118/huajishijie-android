package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 17/5/9.
 */

public class CutePhone {
    private String number;
    private String address;
    private String status;
    private String preStore;
    private String less;
    private String time;

    public CutePhone(String number, String address, String status, String preStore, String less, String time) {
        this.number = number;
        this.address = address;
        this.status = status;
        this.preStore = preStore;
        this.less = less;
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreStore() {
        return preStore;
    }

    public void setPreStore(String preStore) {
        this.preStore = preStore;
    }

    public String getLess() {
        return less;
    }

    public void setLess(String less) {
        this.less = less;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
