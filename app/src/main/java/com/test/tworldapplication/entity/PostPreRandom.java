package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 2019/8/13.
 */

public class PostPreRandom {
    private String session_token;
    private String province;
    private String city;
    private String numberRule;
    private String number;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumberRule() {
        return numberRule;
    }

    public void setNumberRule(String numberRule) {
        this.numberRule = numberRule;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
