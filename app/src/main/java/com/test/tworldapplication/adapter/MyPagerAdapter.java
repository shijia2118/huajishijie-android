package com.test.tworldapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.test.tworldapplication.activity.other.WebViewActivity;
import com.test.tworldapplication.entity.Carousel;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;

/**
 * Created by 27733 on 2016/9/19.
 */
public class MyPagerAdapter extends PagerAdapter {

    Context context;
    Carousel[] carousels;
    WebView webView;
    int pos = 0;

    public MyPagerAdapter(Context context, Carousel[] carousels) {
        this.context = context;
        this.carousels = carousels;
    }

    @Override
    public int getCount() {
        return carousels.length;
    }//重点

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {


        ImageView imageView = new ImageView(context);
        container.addView(imageView);
        BitmapUtil.LoadImageByUrl(ImageView.ScaleType.FIT_XY, imageView, carousels[position].getUrl(), DisplayUtil.dp2px(context, 100f), DisplayUtil.dp2px(context, 100f));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!carousels[pos].equals("")) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("carousel", carousels[pos]);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        return imageView;


    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
