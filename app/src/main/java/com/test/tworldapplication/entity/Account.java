package com.test.tworldapplication.entity;

import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by dasiy on 16/10/17.
 */
public class Account {
    private Integer imgLogo;
    private String tvLogo;
    public Account(Integer imgLogo,String tvLogo){
        this.imgLogo = imgLogo;
        this.tvLogo = tvLogo;
    }

    public Integer getImgLogo() {
        return imgLogo;
    }

    public void setImgLogo(Integer imgLogo) {
        this.imgLogo = imgLogo;
    }

    public String getTvLogo() {
        return tvLogo;
    }

    public void setTvLogo(String tvLogo) {
        this.tvLogo = tvLogo;
    }
}
