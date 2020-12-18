package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/12.
 */

public class PostMessage {
    private String session_token;
    private Integer id;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
