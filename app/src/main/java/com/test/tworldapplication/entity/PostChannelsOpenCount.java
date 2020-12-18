package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/21.
 */

public class PostChannelsOpenCount {
    private String session_token;
    private String orgCode;
    private Integer openTime;
    private String page;
    private String linage;

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

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
    }
}
