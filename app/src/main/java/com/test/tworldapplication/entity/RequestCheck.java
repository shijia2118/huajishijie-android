package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 16/11/4.
 */

public class RequestCheck implements Serializable{
    private String number;
    private Integer org_number_poolsId;
    private String cityName;
    private String operatorName;
    private String numberStatus;
    private Integer simId;
    private String simICCID;
    private String prestore;
    private Package[] packages;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getOrg_number_poolsId() {
        return org_number_poolsId;
    }

    public void setOrg_number_poolsId(Integer org_number_poolsId) {
        this.org_number_poolsId = org_number_poolsId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getNumberStatus() {
        return numberStatus;
    }

    public void setNumberStatus(String numberStatus) {
        this.numberStatus = numberStatus;
    }

    public Integer getSimId() {
        return simId;
    }

    public void setSimId(Integer simId) {
        this.simId = simId;
    }

    public String getSimICCID() {
        return simICCID;
    }

    public void setSimICCID(String simICCID) {
        this.simICCID = simICCID;
    }

    public String getPrestore() {
        return prestore;
    }

    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    public Package[] getPackages() {
        return packages;
    }

    public void setPackages(Package[] packages) {
        this.packages = packages;
    }
}
