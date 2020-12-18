package com.test.tworldapplication.activity.admin;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.account.NumberRechargeActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostPinCreate;
import com.test.tworldapplication.entity.RequestPinCreate;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class PayPassCreateActivity extends BaseActivity {

    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.etVerify)
    EditText etVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pass_create);
        ButterKnife.bind(this);
        setBackGroundTitle("支付密码创建", true);
    }

    @OnClick(R.id.tvSure)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            String strPass = etPass.getText().toString();
            String strVerify = etVerify.getText().toString();
            if (strPass.length() != 6 || strVerify.length() != 6) {
                Util.createToast(PayPassCreateActivity.this, "请输入6位数字密码!");
            } else {
                if (strPass.equals(strVerify)) {
                    HttpPost<PostPinCreate> httpPost = new HttpPost<>();
                    PostPinCreate postPinCreate = new PostPinCreate();
                    postPinCreate.setSession_token(Util.getLocalAdmin(PayPassCreateActivity.this)[0]);
                    postPinCreate.setPassword(Util.encode(BaseCom.PASSWORD0 + strVerify + BaseCom.PASSWORD1));
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setParameter(postPinCreate);
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPinCreate) + BaseCom.APP_PWD));
                    Log.d("aaa", gson.toJson(httpPost));
                    new AdminHttp().pinCreate(AdminRequest.pinCreate(PayPassCreateActivity.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            SharedPreferences share = PayPassCreateActivity.this.getSharedPreferences(BaseCom.hasPassword, MODE_PRIVATE);
                            SharedPreferences.Editor edit = share.edit(); //编辑文件
                            edit.putInt("hasPass", 1);
                            edit.commit();
//                        PayPassCreateActivity.this.finish();
                            AppManager.getAppManager().finishActivity();

                        }
                    }), httpPost);
                } else {
                    Util.createToast(PayPassCreateActivity.this, "两次密码输入不一致!");
                }
            }
        }
    }
}
