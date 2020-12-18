package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 17/5/16.
 */

public class PostAgentsLiang {
    private String session_token;
    private String page;
    private String linage;
    private String provinceCode;
    private String cityCode;
    private String operatorCode;
    private Integer liangRuleId;
    private String number;
    private String liangStatus;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLiangStatus() {
        return liangStatus;
    }

    public void setLiangStatus(String liangStatus) {
        this.liangStatus = liangStatus;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public Integer getLiangRuleId() {
        return liangRuleId;
    }

    public void setLiangRuleId(Integer liangRuleId) {
        this.liangRuleId = liangRuleId;
    }
}
