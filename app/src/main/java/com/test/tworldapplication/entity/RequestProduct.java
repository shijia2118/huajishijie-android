package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 2018/1/5.
 */

public class RequestProduct {
    private List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
