package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dasiy on 16/12/11.
 */
public class Value_one {
    @XStreamAlias("key")
    private String id;
    @XStreamAlias("string")
    private String id_value;
    @XStreamAlias("key")
    private String name;
    @XStreamAlias("string")
    private String name_value;
    @XStreamAlias("key")
    private String province;
    @XStreamAlias("array")
    private List<Value_two> list;

    public Value_one() {
    }

    public Value_one(String id, String id_value, String name, String name_value, String province, List<Value_two> list) {
        this.id = id;
        this.id_value = id_value;
        this.name = name;
        this.name_value = name_value;
        this.province = province;
        this.list = list;
    }
}
