package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/5.
 */

public class PostRandomNumber {
    private String session_token;
    private Integer numberpool;
    private String numberType;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public Integer getNumberpool() {
        return numberpool;
    }

    public void setNumberpool(Integer numberpool) {
        this.numberpool = numberpool;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }
}
