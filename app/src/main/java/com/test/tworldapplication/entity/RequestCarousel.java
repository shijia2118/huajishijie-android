package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/7.
 */

public class RequestCarousel {
    private Integer count;
    private Carousel[] carousel_Picture;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Carousel[] getCarousel_Picture() {
        return carousel_Picture;
    }

    public void setCarousel_Picture(Carousel[] carousel_Picture) {
        this.carousel_Picture = carousel_Picture;
    }
}
