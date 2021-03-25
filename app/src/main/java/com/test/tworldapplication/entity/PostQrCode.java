package com.test.tworldapplication.entity;

public class PostQrCode {
    private String session_token;
    private String number;
    private String packagesId;
    private String promotionId;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(String packagesId) {
        this.packagesId = packagesId;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}
