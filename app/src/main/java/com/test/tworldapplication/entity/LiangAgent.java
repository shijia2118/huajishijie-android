package com.test.tworldapplication.entity;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by dasiy on 17/5/15.
 */

public class LiangAgent implements Serializable {
    private Integer id;
    private String number;
    private Integer liangRuleId;
    private String liangType;
    private String liangCardType;
    private String numberMemo;
    private String minConsumption;
    private String prestore;
    private String provinceCode;
    private String cityCode;
    private String operatorCode;
    private String userCode;
    private String userName;
    private String userTel;
    private Integer liangStatus;


    public Integer getLiangStatus() {
        return liangStatus;
    }

    public void setLiangStatus(Integer liangStatus) {
        this.liangStatus = liangStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getLiangRuleId() {
        return liangRuleId;
    }

    public void setLiangRuleId(Integer liangRuleId) {
        this.liangRuleId = liangRuleId;
    }

    public String getLiangType() {
        return liangType;
    }

    public void setLiangType(String liangType) {
        this.liangType = liangType;
    }

    public String getLiangCardType() {
        return liangCardType;
    }

    public void setLiangCardType(String liangCardType) {
        this.liangCardType = liangCardType;
    }

    public String getNumberMemo() {
        return numberMemo;
    }

    public void setNumberMemo(String numberMemo) {
        this.numberMemo = numberMemo;
    }

    public String getMinConsumption() {
        return minConsumption;
    }

    public void setMinConsumption(String minConsumption) {
        this.minConsumption = minConsumption;
    }

    public String getPrestore() {
        return prestore;
    }

    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }
}
