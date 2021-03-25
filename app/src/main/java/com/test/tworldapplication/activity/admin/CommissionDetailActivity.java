package com.test.tworldapplication.activity.admin;

import android.os.Bundle;
import android.widget.TextView;
import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostGetAccountPeriod;
import com.test.tworldapplication.entity.RequestGetAccountPeriod;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CommissionDetailActivity extends BaseActivity {

    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.openTime)
    TextView openTime;
    @BindView(R.id.telStart)
    TextView telStart;
    @BindView(R.id.subject)
    TextView subject;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.accountPeriod)
    TextView accountPeriod;
    @BindView(R.id.reason)
    TextView reason;

    @BindView(R.id.subject_t)
    TextView subject_t;
    @BindView(R.id.amount_t)
    TextView amount_t;
    @BindView(R.id.year_t)
    TextView year_t;
    @BindView(R.id.accountPeriod_t)
    TextView accountPeriod_t;
    @BindView(R.id.reason_t)
    TextView reason_t;

    String id;
    boolean kou = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("佣金详情", true);
        id = getIntent().getStringExtra("id");
        kou = getIntent().getBooleanExtra("kou",false);
        if(kou){
            subject_t.setText("扣罚科目");
            amount_t.setText("扣罚金额");
            year_t.setText("受理时间");
            accountPeriod_t.setText("账期");
            reason_t.setText("扣罚原因");
        }
        initData();
    }

    private void initData() {

        HttpPost<PostGetAccountPeriod> httpPost = new HttpPost<>();
        PostGetAccountPeriod postCheck = new PostGetAccountPeriod();
        postCheck.setSession_token(Util.getLocalAdmin(CommissionDetailActivity.this)[0]);
        postCheck.setId(id);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postCheck);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + new Gson().toJson(postCheck) + BaseCom.APP_PWD));
        new AdminHttp().getAccountPeriod(AdminRequest.getAccountPeriod(CommissionDetailActivity.this, new SuccessValue<RequestGetAccountPeriod>() {
            @Override
            public void OnSuccess(RequestGetAccountPeriod value) {
                tel.setText(value.getTel());
                openTime.setText(value.getOpenTime());
                telStart.setText(value.getTelStart());
                subject.setText(value.getSubject());
                String amountStr;
                if(kou){
                    amountStr = "-"+value.getAmount();
                }else {
                    amountStr = "+"+value.getAmount();
                }
                amount.setText(amountStr);
                year.setText(value.getYear());
                accountPeriod.setText(value.getAccountPeriod());
                reason.setText(value.getReason());
            }
        }), httpPost);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

}
