package com.test.tworldapplication.activity.admin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostPinModify;
import com.test.tworldapplication.entity.RequestPinModify;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class PayPassReviseActivity extends BaseActivity {

    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.etPrevious)
    EditText etPrevious;
    @BindView(R.id.etNew)
    EditText etNew;
    @BindView(R.id.etVerify)
    EditText etVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pass_revise);
        ButterKnife.bind(this);
        setBackGroundTitle("支付密码修改", true);
    }

    @OnClick(R.id.tvSure)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            String strNew = etNew.getText().toString();
            String strPrevious = etPrevious.getText().toString();
            String strVerify = etVerify.getText().toString();
            if (strNew.equals(strVerify)) {
                HttpPost<PostPinModify> httpPost = new HttpPost<>();
                PostPinModify postPinModify = new PostPinModify();
                postPinModify.setSession_token(Util.getLocalAdmin(PayPassReviseActivity.this)[0]);
                postPinModify.setOld_password(Util.encode(BaseCom.PASSWORD0 + strPrevious + BaseCom.PASSWORD1));
                postPinModify.setNew_password(Util.encode(BaseCom.PASSWORD0 + strVerify + BaseCom.PASSWORD1));
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postPinModify);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPinModify) + BaseCom.APP_PWD));
                new AdminHttp().pinModify(AdminRequest.pinModeify(new SuccessValue<HttpRequest<RequestPinModify>>() {
                    @Override
                    public void OnSuccess(HttpRequest<RequestPinModify> value) {
                        Util.createToast(PayPassReviseActivity.this, value.getMes());
                        if (value.getCode() == BaseCom.NORMAL) {
                            Util.createToast(PayPassReviseActivity.this, "密码修改成功!");
//                        context.finish();
                            AppManager.getAppManager().finishActivity(PayPassReviseActivity.class);
                            AppManager.getAppManager().finishActivity();

                        } else if ((value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT)) {
                            Util.gotoActy(PayPassReviseActivity.this, LoginActivity.class);
                            //AppManager.getAppManager().finishActivity();
                        }
                    }
                }), httpPost);
            } else {
                Util.createToast(PayPassReviseActivity.this, "两次密码输入不一致!");
            }
        }
    }
}
