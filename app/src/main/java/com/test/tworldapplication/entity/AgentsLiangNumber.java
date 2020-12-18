package com.test.tworldapplication.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dasiy on 17/5/16.
 */

public class AgentsLiangNumber implements Serializable {
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
    private Package[] packages;
    //    private List<Package> packages;
    private String provinceName;
    private String cityName;
    private Integer liangStatus;
    private String createDate;
    private Package selectPackage;
    private Promotion selectPromotion;

    public Package getSelectPackage() {
        return selectPackage;
    }

    public void setSelectPackage(Package selectPackage) {
        this.selectPackage = selectPackage;
    }

    public Promotion getSelectPromotion() {
        return selectPromotion;
    }

    public void setSelectPromotion(Promotion selectPromotion) {
        this.selectPromotion = selectPromotion;
    }
    //    private Package packages;
//    private Promotion promotions;

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

    public Package[] getPackages() {
        return packages;
    }

    public void setPackages(Package[] packages) {
        this.packages = packages;
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

    //    public Package[] getPackages() {
//        return packages;
//    }
//
//    public void setPackages(Package[] packages) {
//        this.packages = packages;
//    }
//
//    public Promotion[] getPromotions() {
//        return promotions;
//    }
//
//    public void setPromotions(Promotion[] promotions) {
//        this.promotions = promotions;
//    }
}
