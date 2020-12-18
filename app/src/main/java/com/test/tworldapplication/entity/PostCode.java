package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/3.
 */

public class PostCode {
    private String session_token;
    private String captcha_type;
    private String tel;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getCaptcha_type() {
        return captcha_type;
    }

    public void setCaptcha_type(String captcha_type) {
        this.captcha_type = captcha_type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
