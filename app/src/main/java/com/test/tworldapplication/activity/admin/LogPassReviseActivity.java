package com.test.tworldapplication.activity.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostAdmin;
import com.test.tworldapplication.entity.PostModify;
import com.test.tworldapplication.entity.RequestModify;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.AdminInterface;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class LogPassReviseActivity extends BaseActivity {

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
        setContentView(R.layout.activity_password_login);
        ButterKnife.bind(this);
        setBackGroundTitle("登录密码修改", true);
        initView();
    }

    private void initView() {
        dialog.getTvTitle().setText("正在修改");
    }


    @OnClick(R.id.tvSure)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            String strPrevious = etPrevious.getText().toString();
            String strNew = etNew.getText().toString();
            String strVerify = etVerify.getText().toString();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < strNew.length(); i++) {
                if (!Character.isDigit(strNew.charAt(i))) {
                    list.add(1);
                    Log.d("aaa", "1");
                } else {
                    list.add(2);
                    Log.d("aaa", "2");
                }
            }

            if (!list.contains(1) || !list.contains(2) || strNew.length() < 6 || strNew.length() > 12) {
                Util.createToast(LogPassReviseActivity.this, "请输入6-12位数字和字母新密码!");
            } else {
                if (strNew.equals(strPrevious)) {
                    Util.createToast(LogPassReviseActivity.this, "新密码不能与原密码一致!");
                } else if (!strNew.equals(strVerify)) {
                    Util.createToast(LogPassReviseActivity.this, "两次新密码输入不一致!");
                } else {
                    dialog.show();
                    HttpPost<PostModify> httpPost = new HttpPost<>();
                    PostModify postModify = new PostModify();
                    postModify.setSession_token(Util.getLocalAdmin(LogPassReviseActivity.this)[0]);
                    postModify.setOld_password(Util.GetMD5Code(BaseCom.PASSWORD0 + strPrevious + BaseCom.PASSWORD1));
                    postModify.setNew_password(Util.GetMD5Code(BaseCom.PASSWORD0 + strNew + BaseCom.PASSWORD1));
                    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                    httpPost.setParameter(postModify);
                    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postModify) + BaseCom.APP_PWD));
                    Log.d("aaa", gson.toJson(httpPost));
                    new AdminHttp().modifyPwd(AdminRequest.modifyPwd(new SuccessValue<HttpRequest<RequestModify>>() {
                        @Override
                        public void OnSuccess(HttpRequest<RequestModify> value) {
                            Util.createToast(LogPassReviseActivity.this, value.getMes());
                            if (value.getCode() == BaseCom.NORMAL) {
                                AppManager.getAppManager().finishActivity();
                            } else if ((value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT))
                                Util.gotoActy(LogPassReviseActivity.this, LoginActivity.class);
                        }
                    }, dialog), httpPost);
                }
            }

        }
    }
}
