package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/9.
 */

public class PostLockNumber {
    private String session_token;
    private String number;
    private String numberpoolId;
    private String numberType;

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

    public String getNumberpoolId() {
        return numberpoolId;
    }

    public void setNumberpoolId(String numberpoolId) {
        this.numberpoolId = numberpoolId;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }
}
