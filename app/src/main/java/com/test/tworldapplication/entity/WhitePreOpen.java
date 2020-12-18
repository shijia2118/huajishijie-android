package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 17/5/8.
 */

public class WhitePreOpen implements Serializable{
    private String phone;
    private String status;

    public WhitePreOpen(String phone, String status) {
        this.phone = phone;
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
