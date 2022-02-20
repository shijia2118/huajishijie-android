package com.test.tworldapplication.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostCaptcha;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostNumberCheck;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.CountDownTimerUtils;
import com.test.tworldapplication.utils.Util;

import butterknife.ButterKnife;
import wintone.passport.sdk.utils.AppManager;

public class NumberVerificationActivity extends BaseActivity {

    EditText phoneNum;
    EditText etCode;
    TextView tvGetCode;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);
        ButterKnife.bind(this);
        setBackGroundTitle("号码验证", true);

        initView();

        Intent intent = getIntent(); // 取得从上一个Activity当中传递过来的Intent对象
        if (intent != null) {
            String data = intent.getStringExtra("phone"); // 从Intent当中根据key取得value
            phoneNum.setText(data);
            phoneNum.setSelection(data.length());
        }

    }

    /**
     * 初始化页面
     */
    private void initView(){
        phoneNum = findViewById(R.id.phone_num);
        etCode = findViewById(R.id.etCode);
        tvGetCode = findViewById(R.id.tvGetCode);
        tvLogin = findViewById(R.id.tvLogin);

        tvGetCode.setOnClickListener(onClickCodeButton);
        tvLogin.setOnClickListener(onClickLoginButton);
    }

    /**
     * 获取验证码按钮点击事件
     */
    private final View.OnClickListener onClickCodeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String _phone = phoneNum.getText().toString();
            if(_phone.isEmpty()){
                Util.createToast(NumberVerificationActivity.this, "请输入手机号码!");
            }else if(_phone.length()!=11){
                Util.createToast(NumberVerificationActivity.this, "请输入正确的手机号码!");
            }else {
                dialog.getTvTitle().setText("正在校验号码");
                dialog.show();
                HttpPost<PostNumberCheck> httpPost = new HttpPost<>();
                PostNumberCheck postNumberCheck = new PostNumberCheck();
                postNumberCheck.setNumber(_phone);
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postNumberCheck);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNumberCheck) + BaseCom.APP_PWD));
                new OtherHttp().numberCheck(OtherRequest.numberCheck(NumberVerificationActivity.this, dialog, new SuccessNull() {
                    @Override
                    public void onSuccess() {
                        dialog.getTvTitle().setText("正在获取验证码");
                        dialog.show();
                        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetCode, 60000, 1000);
                        mCountDownTimerUtils.start();
                        HttpPost<PostCode> httpPost = new HttpPost<PostCode>();
                        PostCode postCode = new PostCode();
                        postCode.setTel(_phone);
                        postCode.setCaptcha_type("6");
                        postCode.setSession_token(Util.getLocalAdmin(NumberVerificationActivity.this)[0]);
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
                        httpPost.setParameter(postCode);
                        Log.d("shijia", gson.toJson(httpPost));
                        new AdminHttp().getCode(AdminRequest.getCode(dialog), httpPost);
                    }
                }), httpPost);
            }
        }
    };

    /**
     * 登录按钮点击事件
     */
    private final View.OnClickListener onClickLoginButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!Util.isFastDoubleClick()) {
                String _phone = phoneNum.getText().toString();
                String _code = etCode.getText().toString();
                if(_phone.isEmpty()){
                    Util.createToast(NumberVerificationActivity.this, "请输入手机号码");
                } else if(_phone.length()!=11){
                    Util.createToast(NumberVerificationActivity.this, "请输入正确的手机号码");
                } else if (_code.isEmpty()) {
                    Util.createToast(NumberVerificationActivity.this, "请输入验证码");
                } else {
                    dialog.getTvTitle().setText("验证码验证");
                    dialog.show();
                    HttpPost<PostCaptcha> httpPost = new HttpPost<>();
                    PostCaptcha postCaptcha = new PostCaptcha();
                    postCaptcha.setTel(_phone);
                    postCaptcha.setSession_token(Util.getLocalAdmin(NumberVerificationActivity.this)[0]);
                    postCaptcha.setCaptcha(etCode.getText().toString());
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setParameter(postCaptcha);
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCaptcha) + BaseCom.APP_PWD));
                    Log.d("aaa", gson.toJson(httpPost));
                    new AdminHttp().verifyCaptcha(AdminRequest.verifyCaptcha(NumberVerificationActivity.this, dialog, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            gotoActy(MainNewActivity.class);
                            AppManager.getAppManager().finishAllActivity();
                        }
                    }), httpPost);
                }

            }
        }
    };
}