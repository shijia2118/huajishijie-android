package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/4.
 */

public class PostMoney {
    private String session_token;
    private String prestore;
    private String promotionId;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getPrestore() {
        return prestore;
    }

    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}
