package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 17/5/18.
 */

public class RequestPreNumber implements Serializable {
    private String ePreNo;
    private String number;
    private Double amount;
    private String createdate;
    private String operator;
    private Integer packageId;
    private String packageName;
    private Integer promotionId;
    private String promotionName;
    private String iccid;
    private String imsi;
    private Integer preState;
    private String simId;
    private String cityName;


    public String getePreNo() {
        return ePreNo;
    }

    public void setePreNo(String ePreNo) {
        this.ePreNo = ePreNo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public Integer getPreState() {
        return preState;
    }

    public void setPreState(Integer preState) {
        this.preState = preState;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
