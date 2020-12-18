package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/27.
 */

public class QdsOrder {
    private String name;
    private String phone;
    private String number;
    private String qdName;
    public QdsOrder(String name,String phone,String number,String qdName){
        this.name = name;
        this.phone = phone;
        this.number = number;
        this.qdName = qdName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQdName() {
        return qdName;
    }

    public void setQdName(String qdName) {
        this.qdName = qdName;
    }
}
