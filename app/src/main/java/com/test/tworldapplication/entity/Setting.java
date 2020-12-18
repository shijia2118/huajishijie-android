package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/20.
 */

public class Setting {
    private String key;
    private String value;

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
