package com.test.tworldapplication.entity;

import android.graphics.Bitmap;

/**
 * Created by dasiy on 16/11/7.
 */

public class CarouselBitmap {
    private Bitmap bitmap;
    private String jumpUrl;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
