package com.test.tworldapplication.entity;

import java.io.Serializable;

public class LiangAgents implements Serializable{
    private Integer id;
    private String orderNumber;
    private Integer applySum;
    private Integer actualSum;
    private String auditStatusName;
    private String createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
