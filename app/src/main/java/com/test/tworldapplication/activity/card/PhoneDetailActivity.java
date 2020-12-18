package com.test.tworldapplication.activity.card;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.WhitePreOpen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneDetailActivity extends BaseActivity {


    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    WhitePreOpen whitePreOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("号码详情", true);
        whitePreOpen = (WhitePreOpen) getIntent().getSerializableExtra("whitepreopen");
        tvNumber.setText(whitePreOpen.getPhone());
        tvAddress.setText("浙江省杭州市");
        tvStatus.setText(whitePreOpen.getStatus());
    }

    @OnClick({R.id.ll_package, R.id.ll_activity, R.id.tvNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_package:
                break;
            case R.id.ll_activity:
                break;
            case R.id.tvNext:

                break;
        }
    }
}
