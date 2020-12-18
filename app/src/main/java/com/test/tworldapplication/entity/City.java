package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/12/9.
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class City {
    private String c_id;
    private String c_name;

    public City(String c_name) {
        this.c_name = c_name;
    }

    public City(String c_id, String c_name) {
        this.c_id = c_id;
        this.c_name = c_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return c_name.contains(city.c_name);
    }

    public String getC_id() {
        return c_id;
    }

    public String getC_name() {
        return c_name;
    }
}
