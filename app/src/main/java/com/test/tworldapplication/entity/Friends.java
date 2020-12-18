package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by dasiy on 16/12/11.
 */
//@XStreamAlias("朋友")
public class Friends {
//    @XStreamAlias("姓名")
    private String name;
//    @XStreamAlias("年龄")
    private int age;

    public Friends() {

    }

    public Friends(String name, int age) {
        this.name = name;
        this.age = age;

    }

    @Override
    public String toString() {
        return "name:" + name + ",age:" + age;
    }
}
