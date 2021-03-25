package com.test.tworldapplication.activity.admin;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.other.MessageDetailActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostIntroduction;
import com.test.tworldapplication.entity.RequestIntroduction;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.imgAbout)
    ImageView imgAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        setBackGroundTitle("关于我们", true);
        int screenWidth = DisplayUtil.getWindowWidth(this); // 获取屏幕宽度
        ViewGroup.LayoutParams lp = imgAbout.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imgAbout.setLayoutParams(lp);

        imgAbout.setMaxWidth(screenWidth);
        imgAbout.setMaxHeight(screenWidth * 8);
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getData() {
        HttpPost<PostIntroduction> httpPost = new HttpPost<>();
        PostIntroduction postIntroduction = new PostIntroduction();
        postIntroduction.setSession_token(Util.getLocalAdmin(AboutUsActivity.this)[0]);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postIntroduction);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postIntroduction) + BaseCom.APP_PWD));
        new AdminHttp().introduction(AdminRequest.introduction(AboutUsActivity.this, new SuccessValue<RequestIntroduction>() {
            @Override
            public void OnSuccess(RequestIntroduction value) {
                BitmapUtil._LoadImageByUrl(ImageView.ScaleType.CENTER_CROP, imgAbout, value.getInformation(), DisplayUtil.getWindowWidth(AboutUsActivity.this), DisplayUtil.getWindowHeight(AboutUsActivity.this));
            }
        }), httpPost);
    }
}
