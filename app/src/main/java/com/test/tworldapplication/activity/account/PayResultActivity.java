package com.test.tworldapplication.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.home.YewubanliActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.Message;
import com.test.tworldapplication.utils.EventBusCarrier;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class PayResultActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.ll_failed)
    LinearLayout llFailed;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tvRecharge)
    TextView tvRecharge;
    @BindView(R.id.tvBackHome)
    TextView tvBackHome;
    @BindView(R.id.tvReason)
    TextView tvReason;
    private int flag;
    String from, out_trade_no, timestamp, total_amount;
    String phone, money, date, reason, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);
        setBackGroundTitle("支付结果", false);
        flag = getIntent().getIntExtra("flag", 0);//0成功1失败
        from = getIntent().getStringExtra("from");//1账户0话费
        if (from.equals("0")) {
            phone = getIntent().getStringExtra("phone");
            money = getIntent().getStringExtra("money");
            date = getIntent().getStringExtra("date");
            reason = getIntent().getStringExtra("reason");
            id = getIntent().getStringExtra("id");

        } else {
            out_trade_no = getIntent().getStringExtra("out_trade_no");//1账户0话费
            timestamp = getIntent().getStringExtra("timestamp");//1账户0话费
            total_amount = getIntent().getStringExtra("total_amount");//1账户0话费
        }

        setView();
    }

    private void setView() {

        if (from.equals("0")) {
            tvType.setText("类型: 话费充值");
            tvReason.setText(reason);
            tvData.setText("日期: " + date);
            tvId.setText("编号: " + id);
            tvMoney.setText("金额: " + money);
            tvPhoneNumber.setText("号码: " + phone);
        } else {
            tvData.setText("日期: " + timestamp);
            tvId.setText("编号: " + out_trade_no);
            tvMoney.setText("金额: " + total_amount);
            tvType.setText("类型: 账户充值");
            tvPhoneNumber.setText("号码: 当前账户");

        }
        if (flag == 0) {
            llSuccess.setVisibility(View.VISIBLE);
            llFailed.setVisibility(View.GONE);

        } else {
            llFailed.setVisibility(View.VISIBLE);
            llSuccess.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tvRecharge, R.id.tvBackHome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRecharge:
                switch (from) {
                    case "0":
                        AppManager.getAppManager().finishActivity(NumberRechargeActivity.class);
                        AppManager.getAppManager().finishActivity();
                        gotoActy(NumberRechargeActivity.class);
                        break;
                    case "1":
                        AppManager.getAppManager().finishActivity(AccountBalanceActivity.class);
                        AppManager.getAppManager().finishActivity();
                        gotoActy(AccountBalanceActivity.class);
                        break;
                }

//                this.finish();
                break;
            case R.id.tvBackHome:

                switch (from) {
                    case "0":
//                        AppManager.getAppManager().finishActivity(YewubanliActivity.class);
                        AppManager.getAppManager().finishActivity();
                        EventBusCarrier carrier = new EventBusCarrier();
                        carrier.setEventType("444");
                        EventBus.getDefault().post(carrier);
                        break;
                    case "1":
                        AppManager.getAppManager().finishActivity(AccountBalanceActivity.class);
                        AppManager.getAppManager().finishActivity();
                        break;
                }

                break;
        }
    }
}
