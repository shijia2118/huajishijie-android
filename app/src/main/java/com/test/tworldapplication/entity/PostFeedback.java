package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/8.
 */

public class PostFeedback {
    private String session_token;
    private String title;
    private String content;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
