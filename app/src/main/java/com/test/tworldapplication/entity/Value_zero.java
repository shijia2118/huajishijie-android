package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Created by dasiy on 16/12/11.
 */
@XStreamAlias("dict")
public class Value_zero {
    private int key;
    @XStreamAlias("dict")
    private Value_one value_one;

    public Value_zero() {
    }

    public Value_zero(int key, Value_one value_one) {
        this.key = key;
        this.value_one = value_one;
    }
}
