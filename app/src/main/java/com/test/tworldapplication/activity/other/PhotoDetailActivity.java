package com.test.tworldapplication.activity.other;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;

import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.view.MatrixImageView;

import wintone.passport.sdk.utils.AppManager;


public class PhotoDetailActivity extends BaseActivity {
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private String path;
    private MatrixImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);
        path = getIntent().getStringExtra("path");
    }

    public void initView() {
        image = new MatrixImageView(this);
        image.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        image.transformIn();
        image.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        image.setScaleType(ScaleType.FIT_CENTER);
        image.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile
                (getResources(), path, DisplayUtil.getWindowWidth(this), DisplayUtil.getWindowHeight(this)));
        image.setOnTransformListener(new MatrixImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    AppManager.getAppManager().finishActivity();
                }
            }
        });
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                image.transformOut();
            }
        });
        setContentView(image);
    }

    @Override
    public void onBackPressed() {
        image.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }
}