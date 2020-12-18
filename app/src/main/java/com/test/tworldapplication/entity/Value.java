package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dasiy on 16/12/11.
 */
@XStreamAlias("dict")
public class Value {
    @XStreamImplicit
    private List<Value_zero> list;

    public Value() {
    }

    public Value(List<Value_zero> list) {
        this.list = list;
    }
}
