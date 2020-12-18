package com.test.tworldapplication.entity;

import com.google.gson.Gson;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.utils.Util;

/**
 * Created by dasiy on 16/10/31.
 */

public class HttpPostNew {
    private String app_key;
    private String app_sign;
    private Photo photo;
    private Object parameter;
    private Gson gson = new Gson();

    public HttpPostNew(Object parameter) {
        this.app_key = Util.GetMD5Code(BaseCom.APP_KEY);
        this.app_sign = Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(parameter) + BaseCom.APP_PWD);
        this.parameter = parameter;
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

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }
}
