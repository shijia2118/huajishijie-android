package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 2018/4/25.
 */

public class PostWinningOrder {
    private String session_token;
    private String page;
    private String linage;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLinage() {
        return linage;
    }

    public void setLinage(String linage) {
        this.linage = linage;
    }

}
