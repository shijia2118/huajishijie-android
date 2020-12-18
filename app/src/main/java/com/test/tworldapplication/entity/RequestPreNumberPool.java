package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 17/5/20.
 */

public class RequestPreNumberPool {
    private List<NumberPool> numberpool;
    private List<NumberType> numberType;

    public List<NumberPool> getNumberpool() {
        return numberpool;
    }

    public void setNumberpool(List<NumberPool> numberpool) {
        this.numberpool = numberpool;
    }

    public List<NumberType> getNumberType() {
        return numberType;
    }

    public void setNumberType(List<NumberType> numberType) {
        this.numberType = numberType;
    }
}
