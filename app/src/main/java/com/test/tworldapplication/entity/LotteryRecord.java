package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 2018/4/25.
 */

public class LotteryRecord implements Serializable {
    private String name;
    private String type;
    private String createDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
