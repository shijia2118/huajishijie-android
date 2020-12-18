package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/4.
 */

public class PostRecharge {
    private String session_token;
    private String number;
//    private Double money;
    private String money;
    private String pay_password;

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }
    //    private String pay_pasword;
//
//    public String getPay_pasword() {
//        return pay_pasword;
//    }
//
//    public void setPay_pasword(String pay_pasword) {
//        this.pay_pasword = pay_pasword;
//    }

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
