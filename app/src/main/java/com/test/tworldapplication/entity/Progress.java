package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/21.
 */

public class Progress {
    private String name;
    private String type;
    private String phone;
    private String state;

    public Progress(String name, String type, String phone, String state) {
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
