package com.test.tworldapplication.activity.account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostQueryBalance;
import com.test.tworldapplication.entity.RequestQueryBalance;
import com.test.tworldapplication.http.AccountHttp;
import com.test.tworldapplication.http.AccountRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BalanceDetailsActivity extends BaseActivity implements DialogInterface.OnKeyListener {

    //    @BindView(R.id.tvTitle)
//    TextView tvTitle;
//    @BindView(R.id.imgBack)
//    ImageView imgBack;
//    @BindView(R.id.ll_back)
//    LinearLayout llBack;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    //    @BindView(R.id.tvPackage)
//    TextView tvPackage;
//    @BindView(R.id.tvActivity)
//    TextView tvActivity;
//    @BindView(R.id.tvDate)
//    TextView tvDate;
//    @BindView(R.id.tvName)
//    TextView tvName;
//    @BindView(R.id.tvPhoneNumber)
//    TextView tvPhoneNumber;
//    @BindView(R.id.tvId)
//    TextView tvId;
    String strNumber;
    Double number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_details);
        ButterKnife.bind(this);
        setBackGroundTitle("号码余额查询", true);
        strNumber = getIntent().getStringExtra("number");
        tvNumber.setText(strNumber);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.getTvTitle().setText("正在读取");
        dialog.show();
        HttpPost<PostQueryBalance> httpPost = new HttpPost<>();
        PostQueryBalance postQueryBalance = new PostQueryBalance();
        postQueryBalance.setSession_token(Util.getLocalAdmin(BalanceDetailsActivity.this)[0]);
        postQueryBalance.setNumber(strNumber);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postQueryBalance) + BaseCom.APP_PWD));
        httpPost.setParameter(postQueryBalance);


        new AccountHttp().queryBalance(AccountRequest.queryBalance(BalanceDetailsActivity.this, dialog, new SuccessValue<HttpRequest<RequestQueryBalance>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestQueryBalance> value) {
                Util.createToast(BalanceDetailsActivity.this, value.getMes());
                if (value.getCode() == BaseCom.NORMAL) {
                    tvBalance.setVisibility(View.VISIBLE);
                    tvNumber.setText(value.getData().getNumber());
                    tvBalance.setText("号码余额:  " + String.valueOf(value.getData().getMoney()) + "元");
                } else if
                        (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(BalanceDetailsActivity.this, LoginActivity.class);
                }
            }
        }), httpPost);


    }

}
