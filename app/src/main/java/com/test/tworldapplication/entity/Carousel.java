package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 16/11/7.
 */

public class Carousel implements Serializable{
    private String url;
    private String jumpUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
