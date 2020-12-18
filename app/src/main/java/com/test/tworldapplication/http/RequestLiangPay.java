package com.test.tworldapplication.http;

import java.io.Serializable;

/**
 * Created by dasiy on 17/6/5.
 */

public class RequestLiangPay implements Serializable{
    private String orderNo;
    private String content;
    private String request;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
