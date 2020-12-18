package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/22.
 */

public class PostCaptcha {
    private String session_token;
    private String tel;
    private String captcha;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
