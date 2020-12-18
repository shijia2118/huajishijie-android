package com.test.tworldapplication.entity;

public class RequestOpenPower {
    private Integer pattern;
    private Integer modes;
    private Integer readModes;

    public Integer getPattern() {
        return pattern;
    }

    public void setPattern(Integer pattern) {
        this.pattern = pattern;
    }

    public Integer getModes() {
        return modes;
    }

    public void setModes(Integer modes) {
        this.modes = modes;
    }

    public Integer getReadModes() {
        return readModes;
    }

    public void setReadModes(Integer readModes) {
        this.readModes = readModes;
    }
}
