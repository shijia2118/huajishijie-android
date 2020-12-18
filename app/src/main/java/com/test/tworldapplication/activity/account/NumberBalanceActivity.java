package com.test.tworldapplication.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import butterknife.OnClick;

public class NumberBalanceActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.ll_input)
    RelativeLayout llInput;
    @BindView(R.id.tvInquiry)
    TextView tvInquiry;
    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_query);
        ButterKnife.bind(this);
        setBackGroundTitle("话费余额查询", true);
    }

    @OnClick(R.id.tvInquiry)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            String strNumber = etNumber.getText().toString();
            if (strNumber.equals("")) {
                Util.createToast(NumberBalanceActivity.this, "请输入手机号码");
            } else if (strNumber.length() != 11) {
                Util.createToast(NumberBalanceActivity.this, "请输入正确的手机号码");
            } else {
                dialog.getTvTitle().setText("正在读取");
                dialog.show();
                HttpPost<PostQueryBalance> httpPost = new HttpPost<>();
                PostQueryBalance postQueryBalance = new PostQueryBalance();
                postQueryBalance.setSession_token(Util.getLocalAdmin(NumberBalanceActivity.this)[0]);
                postQueryBalance.setNumber(strNumber);
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postQueryBalance) + BaseCom.APP_PWD));
                httpPost.setParameter(postQueryBalance);


                new AccountHttp().queryBalance(AccountRequest.queryBalance(NumberBalanceActivity.this, dialog, new SuccessValue<HttpRequest<RequestQueryBalance>>() {
                    @Override
                    public void OnSuccess(HttpRequest<RequestQueryBalance> value) {
                        Util.createToast(NumberBalanceActivity.this, value.getMes());
                        if (value.getCode() == BaseCom.NORMAL) {
                            tvBalance.setVisibility(View.VISIBLE);
                            tvBalance.setText("号码余额:  " + String.valueOf(value.getData().getMoney()) + "元");
                        } else if
                                (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
                            Util.gotoActy(NumberBalanceActivity.this, LoginActivity.class);
                        }
                    }
                }), httpPost);


//            Intent intent = new Intent(this, BalanceDetailsActivity.class);
//            intent.putExtra("number", strNumber);
//            startActivity(intent);
//            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        }
//        gotoActy(BalanceDetailsActivity.class);
    }
}
