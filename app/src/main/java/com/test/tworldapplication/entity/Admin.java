package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/20.
 */

public class Admin {
    private Integer imageId;
    private String strItem;
    public Admin(Integer imageId,String strItem){
        this.imageId = imageId;
        this.strItem = strItem;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getStrItem() {
        return strItem;
    }

    public void setStrItem(String strItem) {
        this.strItem = strItem;
    }
}
