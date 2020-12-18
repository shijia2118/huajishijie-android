package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/19.
 */

public class RechargeRecord {
    private String id;
    private String date;
    private String type;
    private String money;
    private String phone;
    private String state;
   public RechargeRecord(String id, String date, String type, String money, String phone, String state){
       this.id = id;
       this.date = date;
       this.type = type;
       this.money = money;
       this.phone = phone;
       this.state = state;
   }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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
