package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 16/11/4.
 */

public class Promotion implements Serializable{
    private Integer id;
    private String name;
    private String productDetails;

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
