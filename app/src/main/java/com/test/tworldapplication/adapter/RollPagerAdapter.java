package com.test.tworldapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.other.WebViewActivity;
import com.test.tworldapplication.entity.Carousel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhulinfeng on 2017/4/10.
 */

public class RollPagerAdapter
        extends LoopPagerAdapter {
    private Context context;
    List<Carousel> list;
//    private List<Map<String, Object>> mapArrayList = new ArrayList<>();

    public RollPagerAdapter(RollPagerView viewPager, Context context, List<Carousel> list) {
        super(viewPager);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
//        imageView.setImageResource(R.drawable.account);


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.banner)
                .showImageOnFail(R.drawable.banner)
                .cacheInMemory(true)
                .build();
        ImageLoader.getInstance().displayImage(list.get(position).getUrl(), imageView, options);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", list.get(position).getJumpUrl());
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("carousel", list.get(position));
//                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return imageView;
    }

    @Override
    public int getRealCount() {
        return list.size();
    }
}
