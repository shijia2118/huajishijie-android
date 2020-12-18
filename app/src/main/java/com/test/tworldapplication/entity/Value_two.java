package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by dasiy on 16/12/11.
 */
@XStreamAlias("dict")
public class Value_two {
    @XStreamAlias("key")
    private String id;
    @XStreamAlias("string")
    private String id_value;
    @XStreamAlias("key")
    private String name;
    @XStreamAlias("string")
    private String name_value;

    public Value_two() {
    }

    public Value_two(String id, String id_value, String name, String name_value) {
        this.id = id;
        this.id_value = id_value;
        this.name = name;
        this.name_value = name_value;
    }
}
