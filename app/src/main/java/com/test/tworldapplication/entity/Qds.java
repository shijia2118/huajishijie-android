package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/27.
 */

public class Qds {
    private String name;
    private String number;
    private String qdsName;
    public Qds(String name,String number,String qdsName){
        this.name = name;
        this.number = number;
        this.qdsName = qdsName;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQdsName() {
        return qdsName;
    }

    public void setQdsName(String qdsName) {
        this.qdsName = qdsName;
    }
}
