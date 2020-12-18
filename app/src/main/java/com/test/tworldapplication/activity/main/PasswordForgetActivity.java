package com.test.tworldapplication.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.PayPassVerifyActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostCaptcha;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostNumberCheck;
import com.test.tworldapplication.entity.RequestCaptcha;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.CountDownTimerUtils;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordForgetActivity extends BaseActivity {

    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.etUser)
    EditText etUser;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    String strEtPhone = "";

    /*点击验证码按钮校验手机号,发送验证码;点击下一步 校验验证码,跳转页面*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget);
        ButterKnife.bind(this);
        setBackGroundTitle("号码验证", true);
    }


    @OnClick({R.id.tvNext, R.id.tvGetCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                if (!Util.isFastDoubleClick()) {

                    if (etCode.getText().toString().equals("")) {
                        Util.createToast(PasswordForgetActivity.this, "请输入验证码");
                    } else {
                        dialog.getTvTitle().setText("验证码验证");
                        dialog.show();
                        HttpPost<PostCaptcha> httpPost = new HttpPost<>();
                        PostCaptcha postCaptcha = new PostCaptcha();
                        postCaptcha.setTel(strEtPhone);
                        postCaptcha.setCaptcha(etCode.getText().toString());
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postCaptcha);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCaptcha) + BaseCom.APP_PWD));
                        Log.d("aaa", gson.toJson(httpPost));
                        new AdminHttp().verifyCaptcha(AdminRequest.verifyCaptcha(PasswordForgetActivity.this, dialog, new SuccessNull() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(PasswordForgetActivity.this, PasswordResetActivity.class);
                                intent.putExtra("code", etCode.getText().toString());
                                intent.putExtra("phone", strEtPhone);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                            }
                        }), httpPost);

                    }

                }
                break;
            case R.id.tvGetCode:
                strEtPhone = etUser.getText().toString();
                if (strEtPhone.length() != 11) {
                    Util.createToast(PasswordForgetActivity.this, "请输入正确的手机号码!");
                } else {
                    dialog.getTvTitle().setText("正在校验号码");
                    dialog.show();
                    HttpPost<PostNumberCheck> httpPost = new HttpPost<>();
                    PostNumberCheck postNumberCheck = new PostNumberCheck();
                    postNumberCheck.setNumber(strEtPhone);
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setParameter(postNumberCheck);
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNumberCheck) + BaseCom.APP_PWD));
                    new OtherHttp().numberCheck(OtherRequest.numberCheck(PasswordForgetActivity.this, dialog, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            dialog.getTvTitle().setText("正在获取验证码");
                            dialog.show();
                            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetCode, 60000, 1000);
                            mCountDownTimerUtils.start();
                            HttpPost<PostCode> httpPost = new HttpPost<PostCode>();
                            PostCode postCode = new PostCode();
                            postCode.setTel(etUser.getText().toString());
                            postCode.setCaptcha_type("2");
                            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
                            httpPost.setParameter(postCode);
                            Log.d("aaa", gson.toJson(httpPost));
                            new AdminHttp().getCode(AdminRequest.getCode(dialog), httpPost);
                        }
                    }), httpPost);
                }

        }

    }

}
