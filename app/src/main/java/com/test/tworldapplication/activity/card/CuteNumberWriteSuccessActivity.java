package com.test.tworldapplication.activity.card;

import android.os.Bundle;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestPreNumberDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class CuteNumberWriteSuccessActivity extends BaseActivity {
    RequestPreNumberDetails requestPreNumberDetails;
    RequestImsi requestImsi;
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
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    private Package mPackage;
    private Promotion mPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cute_number_write_success);
        ButterKnife.bind(this);
        setBackGroundTitle("预开户结果", true);
        requestPreNumberDetails = (RequestPreNumberDetails) getIntent().getSerializableExtra("data");
        requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
        mPackage = (Package) getIntent().getSerializableExtra("mPackage");
        mPromotion = (Promotion) getIntent().getSerializableExtra("mPromotion");
        initView();
    }

    private void initView() {
        tvNumber.setText("靓号:  " + requestPreNumberDetails.getNumber());
        tvAddress.setText("归属地:  " + requestPreNumberDetails.getProvince() + "," + requestPreNumberDetails.getCityName());
        tvOperate.setText("运营商:  " + requestPreNumberDetails.getOperatorname());
        tvPackage.setText("套餐:  " + mPackage.getName());
        tvActivity.setText("活动包:  " + mPromotion.getName());
        tvStatus.setText("状态:  写卡成功");
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
        AppManager.getAppManager().finishActivity(CuteNumberDetailDailiActivity.class);
        AppManager.getAppManager().finishActivity(CuteNumberDailiActivity.class);


    }

    //CutePhoneDetailDailiActivity
//    @OnClick(R.id.tvFind)
//    public void onClick() {
//        Intent intent = new Intent(CuteNumberWriteSuccessActivity.this, CutePhoneDetailDailiActivity.class);
//        startActivity(intent);
//    }
}
