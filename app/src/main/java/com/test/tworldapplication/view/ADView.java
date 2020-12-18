package com.test.tworldapplication.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.MyPagerAdapter;
import com.test.tworldapplication.entity.Carousel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 27733 on 2016/10/11.
 */
public class ADView extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.rell_point)
    RelativeLayout rellPoint;
    private Context context;
    private RelativeLayout mRelativeLayout;
    public static final int MARGIN_BETWEEN_POINTS = 18;
    MyPagerAdapter mAdapter;
    Carousel[] carousels;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    vp.setCurrentItem(vp.getCurrentItem() + 1);
                    handler.sendEmptyMessageDelayed(1, 3000);
                    break;
            }

        }
    };

    public void setArr(Carousel[] carousels) {
        this.carousels = carousels;
    }

    public ADView(Context context) {
        super(context);
    }


    public ADView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        mRelativeLayout = (RelativeLayout) mInflater.inflate(R.layout.layout_adview, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(mRelativeLayout, params);
        ButterKnife.bind(this);
    }

    public void init() {
        mAdapter = new MyPagerAdapter(context, carousels);

        for (int i = 0; i < carousels.length - 2; i++) {
            View point = new View(context);
            Log.d("bbb", "init" + i);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_point_red);
            } else {
                point.setBackgroundResource(R.drawable.shape_point_white);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
            params.leftMargin = MARGIN_BETWEEN_POINTS;
            params.rightMargin = MARGIN_BETWEEN_POINTS;
            point.setLayoutParams(params);
            llPointGroup.addView(point);
        }
        vp.setAdapter(mAdapter);
        vp.addOnPageChangeListener(this);
        vp.setCurrentItem(1);
//        vp.setOnTouchListener(this);
        handler.sendEmptyMessageDelayed(1, 3000);//3秒后发送空消息
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < carousels.length - 2; i++) {
            if (i == position - 1) {
                llPointGroup.getChildAt(i).setBackgroundResource(R.drawable.shape_point_red);
            } else {
                llPointGroup.getChildAt(i).setBackgroundResource(R.drawable.shape_point_white);
            }
        }
        if (position == carousels.length - 1) {
            vp.setCurrentItem(1, false);
        } else if (position == 0) {
            vp.setCurrentItem(carousels.length - 2, false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                handler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息
                break;
            case MotionEvent.ACTION_UP://抬起
                handler.sendEmptyMessageDelayed(1, 3000);
                break;
            case MotionEvent.ACTION_CANCEL://事件取消，即按下后手指移到了其他地方才松开
                handler.sendEmptyMessageDelayed(1, 3000);
                break;

            default:
                break;
        }
        return false;
    }
}
