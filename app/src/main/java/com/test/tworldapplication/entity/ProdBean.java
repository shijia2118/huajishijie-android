package com.test.tworldapplication.entity;

import java.util.ArrayList;
import java.util.List;

public class ProdBean {
    private String memo2;
    private Integer isSelect = 0;
    private List<ProductBeanDetail> prods = new ArrayList<>();

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public List<ProductBeanDetail> getProds() {
        return prods;
    }

    public void setProds(List<ProductBeanDetail> prods) {
        this.prods = prods;
    }
}
