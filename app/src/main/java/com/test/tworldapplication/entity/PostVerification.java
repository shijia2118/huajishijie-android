package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 2018/1/4.
 */

public class PostVerification {
    private String session_token;
    private String number;

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
}
