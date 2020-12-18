package com.test.tworldapplication.entity;

/**
 * Created by 27733 on 2016/10/11.
 */
public class Home {

    private Integer imageId;
    private String itemName;

    public Home(Integer imageId, String itemName) {
        this.imageId = imageId;
        this.itemName = itemName;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
