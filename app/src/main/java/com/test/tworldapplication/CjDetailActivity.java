package com.test.tworldapplication;

import android.os.Bundle;
import android.widget.TextView;

import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.LotteryRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class CjDetailActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    LotteryRecord lotteryRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cj_detail);
        ButterKnife.bind(this);
        tvTitle.setText("红包详情");
        lotteryRecord = (LotteryRecord) getIntent().getSerializableExtra("data");
        tvContent.setText(lotteryRecord.getName());
        tvTime.setText(lotteryRecord.getCreateDate());
        tvDetail.setText(lotteryRecord.getType());
    }

    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
