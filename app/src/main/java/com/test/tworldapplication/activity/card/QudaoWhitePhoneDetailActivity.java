package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.City;
import com.test.tworldapplication.entity.Province;
import com.test.tworldapplication.entity.RequestPreNumber;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QudaoWhitePhoneDetailActivity extends BaseActivity {

    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvOperate)
    TextView tvOperate;
    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.tvActivity)
    TextView tvActivity;


    RequestPreNumber requestPreNumber;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qudao_white_phone_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("号码详情", true);
        requestPreNumber = (RequestPreNumber) getIntent().getSerializableExtra("data");
        from = getIntent().getStringExtra("from");
        initView();
    }

    @OnClick(R.id.tvNext)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            Intent intent = new Intent(QudaoWhitePhoneDetailActivity.this, HuajiSelectActivity.class);
            intent.putExtra("from", "3");
            intent.putExtra("data", requestPreNumber);
            startActivity(intent);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }

    private void initView() {
        tvNumber.setText(requestPreNumber.getNumber());
        tvOperate.setText(requestPreNumber.getOperator());
        tvPackage.setText(requestPreNumber.getPackageName());
        tvActivity.setText(requestPreNumber.getPromotionName());
        tvAddress.setText(requestPreNumber.getCityName());
    }
}
