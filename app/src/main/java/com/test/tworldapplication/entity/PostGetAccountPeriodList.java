package com.test.tworldapplication.entity;

public class PostGetAccountPeriodList {
    private String session_token;
    private String accountPeriod;
    private String type;
    private String page;
    private String linage;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLinage() {
        return linage;
    }

    public void setLinage(String linage) {
        this.linage = linage;
    }
}
