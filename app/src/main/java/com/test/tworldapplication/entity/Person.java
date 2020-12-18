package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dasiy on 16/12/11.
 */
@XStreamAlias("人")
public class Person {
    @XStreamAlias("姓名")
    private String name;
    @XStreamAlias("年龄")
    private int age;
    @XStreamImplicit
    private List friends;

    public Person() {

    }

    public Person(String name, int age, List<Friends> friends) {
        this.name = name;
        this.age = age;
        this.friends = friends;
    }

    @Override
    public String toString() {

        return "Person [name=" + name + ", age=" + age + ", friends=" + friends + "]";
    }
}
