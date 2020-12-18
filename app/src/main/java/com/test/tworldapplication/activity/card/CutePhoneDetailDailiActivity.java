package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Number;
import com.test.tworldapplication.entity.PostPreNumber;
import com.test.tworldapplication.entity.RequestPreNumber;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class CutePhoneDetailDailiActivity extends BaseActivity {
    Number number;
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

    RequestPreNumber mRequestPreNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cute_phone_detail_daili);
        ButterKnife.bind(this);
        setBackGroundTitle("详情", true);
        number = (Number) getIntent().getSerializableExtra("data");

        getData();
    }

    private void getData() {
        Util.createToast(CutePhoneDetailDailiActivity.this,"正在查询,请稍后");
        PostPreNumber postPreNumber = new PostPreNumber();
        postPreNumber.setNumber(number.getNumber());
        postPreNumber.setSession_token(Util.getLocalAdmin(CutePhoneDetailDailiActivity.this)[0]);
        HttpPost<PostPreNumber> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPreNumber);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPreNumber) + BaseCom.APP_PWD));

        new CardHttp().ePreNumber(httpPost, new Subscriber<HttpRequest<RequestPreNumber>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPreNumber> requestPreNumberHttpRequest) {
                Util.createToast(CutePhoneDetailDailiActivity.this,requestPreNumberHttpRequest.getMes());
                if (requestPreNumberHttpRequest.getCode() == BaseCom.NORMAL) {
                    mRequestPreNumber = requestPreNumberHttpRequest.getData();
                    initView();
                } else if (requestPreNumberHttpRequest.getCode() == BaseCom.LOSELOG || requestPreNumberHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(CutePhoneDetailDailiActivity.this, LoginActivity.class);
                }
            }
        });
    }

    private void initView() {
        tvNumber.setText(mRequestPreNumber.getNumber());
        tvAddress.setText(mRequestPreNumber.getCityName());
        tvOperate.setText(mRequestPreNumber.getOperator());
        tvPackage.setText(mRequestPreNumber.getPackageName());
        tvActivity.setText(mRequestPreNumber.getPromotionName());
        String status = "";
        switch (mRequestPreNumber.getPreState()) {
            case 0:
                status = "待开户";
                break;
            case 1:
                status = "已开户";
                break;
        }
        tvStatus.setText(status);
    }
}
