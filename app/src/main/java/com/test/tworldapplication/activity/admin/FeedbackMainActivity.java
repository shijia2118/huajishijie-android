package com.test.tworldapplication.activity.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.card.TransferCardActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostFeedback;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class FeedbackMainActivity extends BaseActivity {

    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.etInput)
    EditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_main);
        ButterKnife.bind(this);
        setBackGroundTitle("意见反馈", true);
    }

    @OnClick(R.id.tvSubmit)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            if (etInput.getText().toString().equals(""))
                Util.createToast(FeedbackMainActivity.this, "请输入意见反馈");
            else {
                HttpPost<PostFeedback> httpPost = new HttpPost<>();
                PostFeedback postFeedback = new PostFeedback();
                postFeedback.setSession_token(Util.getLocalAdmin(FeedbackMainActivity.this)[0]);
                postFeedback.setTitle("");
                postFeedback.setContent(etInput.getText().toString());
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postFeedback);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postFeedback) + BaseCom.APP_PWD));
                new AdminHttp().feedback(AdminRequest.feedback(FeedbackMainActivity.this, new SuccessNull() {
                    @Override
                    public void onSuccess() {
                        AppManager.getAppManager().finishActivity();
                    }
                }), httpPost);
            }
        }
    }
}
