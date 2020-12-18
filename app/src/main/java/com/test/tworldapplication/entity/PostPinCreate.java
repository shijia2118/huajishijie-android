package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/8.
 */

public class PostPinCreate {
    private String session_token;
    private String password;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
