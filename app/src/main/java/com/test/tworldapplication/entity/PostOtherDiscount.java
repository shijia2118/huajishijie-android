package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/12/8.
 */

public class PostOtherDiscount {
    private String session_token;
    private String actualAmount;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }
}
