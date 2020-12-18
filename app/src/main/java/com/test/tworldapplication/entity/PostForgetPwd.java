package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/10.
 */

public class PostForgetPwd {
    private String userName;
    private String captcha;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
