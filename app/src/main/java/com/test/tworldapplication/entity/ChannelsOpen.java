package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/21.
 */

public class ChannelsOpen {
    private String contact;
    private String orgCode;
    private String name;
    private Integer openCount;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Integer openCount) {
        this.openCount = openCount;
    }
}
