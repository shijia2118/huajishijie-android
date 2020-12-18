package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 17/5/26.
 */

public class DailiAgentsLiangNumber {
    private Integer count;
    private String number;
    private String provinceCode;
    private String cityCode;
    private String liangType;
    private String prestore;
    private String minConsumption;
    private String userName;
    private String userTel;
    private String numberMemo;
    private String network;
    private List<Package> packages;
    private List<Promotion> promotions;

    private String provinceName;
    private String cityName;
    private Integer liangStatus;
    private String createDate;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getLiangType() {
        return liangType;
    }

    public void setLiangType(String liangType) {
        this.liangType = liangType;
    }

    public String getPrestore() {
        return prestore;
    }

    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    public String getMinConsumption() {
        return minConsumption;
    }

    public void setMinConsumption(String minConsumption) {
        this.minConsumption = minConsumption;
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

    public String getNumberMemo() {
        return numberMemo;
    }

    public void setNumberMemo(String numberMemo) {
        this.numberMemo = numberMemo;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getLiangStatus() {
        return liangStatus;
    }

    public void setLiangStatus(Integer liangStatus) {
        this.liangStatus = liangStatus;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
