package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by dasiy on 16/12/9.
 */

public class Area {
    private List<Province> list;

    public Area(List<Province> list) {
        this.list = list;
    }

    public List<Province> getList() {
        return list;
    }
}
