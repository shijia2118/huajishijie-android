package com.test.tworldapplication.entity;

import java.util.List;

public class ProdsEntity {
    private List<ProductBeanDetail> prods;
    private String memo2;

    public List<ProductBeanDetail> getProds() {
        return prods;
    }

    public void setProds(List<ProductBeanDetail> prods) {
        this.prods = prods;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }
}
