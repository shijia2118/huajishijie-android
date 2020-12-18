package com.test.tworldapplication.entity;

public class ProductBeanDetail {
    private String name;
    private Integer type;
    private Integer productId;
    private Integer isCancleFlag;
    private String productDetails;
    private Double productAmount;
    private Double orderAmount;
    private String decription;
    private Integer memo1;
    private String createDate;
    private Integer isSelect=0;


    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getIsCancleFlag() {
        return isCancleFlag;
    }

    public void setIsCancleFlag(Integer isCancleFlag) {
        this.isCancleFlag = isCancleFlag;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public Double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Double productAmount) {
        this.productAmount = productAmount;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Integer getMemo1() {
        return memo1;
    }

    public void setMemo1(Integer memo1) {
        this.memo1 = memo1;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
