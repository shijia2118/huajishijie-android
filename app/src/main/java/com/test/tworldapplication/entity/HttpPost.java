package com.test.tworldapplication.entity;

import com.test.tworldapplication.utils.Util;

import java.security.Policy;

/**
 * Created by dasiy on 16/10/31.
 */

public class HttpPost<T> {
    private String app_key;
    private String app_sign;
    private String version;
    private Photo photo;
    private T parameter;

    public HttpPost() {
        this.version = "4.9.3";
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getApp_sign() {
        return app_sign;
    }

    public void setApp_sign(String app_sign) {
        this.app_sign = app_sign;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

}
