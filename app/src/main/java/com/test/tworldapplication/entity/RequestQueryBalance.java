package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/12/2.
 */

public class RequestQueryBalance {
    private Double money;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
