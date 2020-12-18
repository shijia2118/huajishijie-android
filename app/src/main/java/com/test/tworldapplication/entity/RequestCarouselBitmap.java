package com.test.tworldapplication.entity;

import android.graphics.Bitmap;

/**
 * Created by dasiy on 16/11/7.
 */

public class RequestCarouselBitmap {
    private Integer count;
    private CarouselBitmap[] carousel_Picture;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CarouselBitmap[] getCarousel_Picture() {
        return carousel_Picture;
    }

    public void setCarousel_Picture(CarouselBitmap[] carousel_Picture) {
        this.carousel_Picture = carousel_Picture;
    }
}
