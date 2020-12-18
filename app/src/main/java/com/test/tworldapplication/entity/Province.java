package com.test.tworldapplication.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Province {
    private String p_id;
    private String p_name;
    private List<City> p_list;

    public Province(String p_name) {
        this.p_name = p_name;
    }

    public Province(String p_id, String p_name, List<City> p_list) {
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_list = p_list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return p_name.contains(province.p_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p_name);
    }

    public String getP_id() {
        return p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public List<City> getP_list() {
        return p_list;
    }
}