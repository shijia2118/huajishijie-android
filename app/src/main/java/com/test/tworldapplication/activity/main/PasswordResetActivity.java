package com.test.tworldapplication.activity.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.LogPassReviseActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostForgetPwd;
import com.test.tworldapplication.entity.RequestForgrtPwd;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class PasswordResetActivity extends BaseActivity {

    @BindView(R.id.tvDone)
    TextView tvDone;
    @BindView(R.id.etUser)
    EditText etUser;
    @BindView(R.id.etCode)
    EditText etCode;
    String code = "";
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        ButterKnife.bind(this);
        setBackGroundTitle("重置密码", true);
        code = getIntent().getStringExtra("code");
        phone = getIntent().getStringExtra("phone");
    }

    @OnClick(R.id.tvDone)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            String strPass = etUser.getText().toString();
            String strPassNew = etCode.getText().toString();


            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < strPass.length(); i++) {
                if (!Character.isDigit(strPass.charAt(i))) {
                    list.add(1);
                    Log.d("aaa", "1");
                } else {
                    list.add(2);
                    Log.d("aaa", "2");
                }
            }

            if (!list.contains(1) || !list.contains(2) || strPass.length() < 6 || strPass.length() > 12) {
                Util.createToast(PasswordResetActivity.this, "请输入6-12位数字和字母新密码!");
            } else if (!strPass.equals(strPassNew)) {
                Util.createToast(PasswordResetActivity.this, "两次密码输入不一致!");
            } else {
                dialog.getTvTitle().setText("正在修改");
                dialog.show();
                HttpPost<PostForgetPwd> httpPost = new HttpPost<>();
                PostForgetPwd postForgetPwd = new PostForgetPwd();
                postForgetPwd.setUserName(phone);
                postForgetPwd.setCaptcha(code);
                postForgetPwd.setPassword(Util.encode(BaseCom.PASSWORD0 + strPassNew + BaseCom.PASSWORD1));
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postForgetPwd);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postForgetPwd) + BaseCom.APP_PWD));
                new AdminHttp().forgetPwd(AdminRequest.forgetPwd(dialog, new SuccessValue<HttpRequest<RequestForgrtPwd>>() {
                    @Override
                    public void OnSuccess(HttpRequest<RequestForgrtPwd> value) {
                        Util.createToast(PasswordResetActivity.this, value.getMes());
                        if (value.getCode() == BaseCom.NORMAL) {
                            AppManager.getAppManager().finishActivity(PasswordForgetActivity.class);
                            AppManager.getAppManager().finishActivity();
                        } else if (value.getCode() == BaseCom.VERSIONINCORRENT || value.getCode() == BaseCom.LOSELOG)
                            Util.gotoActy(PasswordResetActivity.this, LoginActivity.class);
                    }
                }), httpPost);
            }
        }
    }
}
