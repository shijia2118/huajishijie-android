package com.test.tworldapplication;

import android.graphics.PointF;
import android.os.Bundle;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.test.tworldapplication.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgreementActivity extends BaseActivity {
    @BindView(R.id.imageView)
    SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        ButterKnife.bind(this);
        setBackGroundTitle("开户协议", true);
        imageView.setImage(ImageSource.resource(R.drawable.agreement));
        imageView.setScaleAndCenter(1.0f, new PointF(0f, 0f));
        imageView.setMinScale(1.0F);
        imageView.setMaxScale(10.0F);
//        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);

    }
}
