package com.test.tworldapplication.entity;

public class RequestBkSelect {
    private String orderNumber;//
    private String name;
    private String tel;
    private String deliveryAddress;
    private Integer applySum;
    private Integer actualSum;
    private String auditStatusName;
    private String auditTime;//
    private String createDate;//
    private String notAuditReasons;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Integer getApplySum() {
        return applySum;
    }

    public void setApplySum(Integer applySum) {
        this.applySum = applySum;
    }

    public Integer getActualSum() {
        return actualSum;
    }

    public void setActualSum(Integer actualSum) {
        this.actualSum = actualSum;
    }

    public String getAuditStatusName() {
        return auditStatusName;
    }

    public void setAuditStatusName(String auditStatusName) {
        this.auditStatusName = auditStatusName;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNotAuditReasons() {
        return notAuditReasons;
    }

    public void setNotAuditReasons(String notAuditReasons) {
        this.notAuditReasons = notAuditReasons;
    }
}
