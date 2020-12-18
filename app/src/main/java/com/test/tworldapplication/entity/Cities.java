package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

public class Cities {
    @XStreamImplicit(itemFieldName = "province")
    private ArrayList<Province> provinces;

    public ArrayList<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(ArrayList<Province> provinces) {
        this.provinces = provinces;
    }
}