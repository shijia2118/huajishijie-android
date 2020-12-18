package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 17/5/20.
 */

public class RequestPreNumberDetails implements Serializable {
    private String number;
    private String numberStatus;
    private String numberStatusName;
    private String poolname;
    private Double amount;
    private String prestore;
    private String regFee;
    private Integer org_number_poolsId;
    private String crmCode;
    private String crmUserName;
    private String liangType;
    private String isLiang;
    private String cycle;
    private String cityName;
    private String province;
    private String operatorname;
    private String orderPrice;
    private Package[] packages;


    public String getPrestore() {
        return prestore;
    }

    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    public String getRegFee() {
        return regFee;
    }

    public void setRegFee(String regFee) {
        this.regFee = regFee;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getNumberStatusName() {
        return numberStatusName;
    }

    public void setNumberStatusName(String numberStatusName) {
        this.numberStatusName = numberStatusName;
    }

    public String getPoolname() {
        return poolname;
    }

    public void setPoolname(String poolname) {
        this.poolname = poolname;
    }



    public Package[] getPackages() {
        return packages;
    }

    public void setPackages(Package[] packages) {
        this.packages = packages;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumberStatus() {
        return numberStatus;
    }

    public void setNumberStatus(String numberStatus) {
        this.numberStatus = numberStatus;
    }

    public Integer getOrg_number_poolsId() {
        return org_number_poolsId;
    }

    public void setOrg_number_poolsId(Integer org_number_poolsId) {
        this.org_number_poolsId = org_number_poolsId;
    }

    public String getCrmCode() {
        return crmCode;
    }

    public void setCrmCode(String crmCode) {
        this.crmCode = crmCode;
    }

    public String getCrmUserName() {
        return crmUserName;
    }

    public void setCrmUserName(String crmUserName) {
        this.crmUserName = crmUserName;
    }

    public String getLiangType() {
        return liangType;
    }

    public void setLiangType(String liangType) {
        this.liangType = liangType;
    }

    public String getIsLiang() {
        return isLiang;
    }

    public void setIsLiang(String isLiang) {
        this.isLiang = isLiang;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }
}
