package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/15.
 */

public class PostPreOpen {
    private String session_token;
    private String number;
    private String iccid;

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

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
}
