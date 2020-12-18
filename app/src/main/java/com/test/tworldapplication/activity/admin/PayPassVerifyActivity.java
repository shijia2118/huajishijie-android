package com.test.tworldapplication.activity.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.main.RegisterActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostCaptcha;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.RequestCaptcha;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.CountDownTimerUtils;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayPassVerifyActivity extends BaseActivity {

    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.etVerify)
    EditText etVerify;
    @BindView(R.id.tvYzm)
    TextView tvYzm;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verify);
        ButterKnife.bind(this);
        setBackGroundTitle("手机号码验证", true);
        etPass.setFocusable(false);
    }

    @OnClick({R.id.tvNext, R.id.tvYzm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvYzm:
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvYzm, 60000, 1000);

                mCountDownTimerUtils.start();
                HttpPost<PostCode> httpPost = new HttpPost<PostCode>();
                PostCode postCode = new PostCode();
                postCode.setCaptcha_type("4");
                postCode.setSession_token(Util.getLocalAdmin(PayPassVerifyActivity.this)[0]);
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
                httpPost.setParameter(postCode);
                Log.d("aaa", gson.toJson(httpPost));
                new AdminHttp().getCode(AdminRequest.getCode(dialog), httpPost);
                break;
            case R.id.tvNext:
                if (!Util.isFastDoubleClick()) {
                    String strCode = etVerify.getText().toString();
                    if (strCode.length() == 0) {
                        Util.createToast(this, "请输入验证码");
                    } else {
                        dialog.show();
                        HttpPost<PostCaptcha> httpPost1 = new HttpPost<>();
                        PostCaptcha postCaptcha = new PostCaptcha();
                        postCaptcha.setSession_token(Util.getLocalAdmin(PayPassVerifyActivity.this)[0]);
                        postCaptcha.setCaptcha(strCode);
                        httpPost1.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost1.setParameter(postCaptcha);
                        httpPost1.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCaptcha) + BaseCom.APP_PWD));
                        Log.d("aaa", gson.toJson(httpPost1));
                        new AdminHttp().verifyCaptcha(AdminRequest.verifyCaptcha(PayPassVerifyActivity.this, dialog, new SuccessNull() {
                            @Override
                            public void onSuccess() {
                                gotoActy(PayPassReviseActivity.class);
//                            finish();
                            }
                        }), httpPost1);

                    }
                }

                break;
        }
//

    }

}
