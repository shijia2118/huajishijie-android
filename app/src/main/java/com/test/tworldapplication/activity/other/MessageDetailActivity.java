package com.test.tworldapplication.activity.other;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostMessage;
import com.test.tworldapplication.entity.RequestMessage;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailActivity extends BaseActivity implements SuccessValue<RequestMessage> {
    Integer id = 0;
    @BindView(R.id.tvDetailTitle)
    TextView tvDetailTitle;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.imgContent)
    ImageView imgContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("id", 0);
        setBackGroundTitle("消息中心", true, false, false);
        int screenWidth = DisplayUtil.getWindowWidth(this); // 获取屏幕宽度
        ViewGroup.LayoutParams lp = imgContent.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imgContent.setLayoutParams(lp);

        imgContent.setMaxWidth(screenWidth);
        imgContent.setMaxHeight(screenWidth * 8);

    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpPost<PostMessage> httpPost = new HttpPost<>();
        PostMessage postMessage = new PostMessage();
        postMessage.setSession_token(Util.getLocalAdmin(MessageDetailActivity.this)[0]);
        postMessage.setId(id);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postMessage);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postMessage) + BaseCom.APP_PWD));

        new OtherHttp().getNotice(OtherRequest.getNotice(MessageDetailActivity.this, this), httpPost);
    }

    @Override
    public void OnSuccess(RequestMessage value) {
        tvDetailTitle.setText(value.getTitle());
        tvTime.setText(value.getTime());
//        tvContent.setText("\u3000\u3000" + value.getContent());
        BitmapUtil._LoadImageByUrl(ImageView.ScaleType.CENTER_CROP, imgContent, value.getContent(), DisplayUtil.dp2px(MessageDetailActivity.this, 100f), DisplayUtil.dp2px(MessageDetailActivity.this, 100f));
//        tvContent.setText(value.getContent());

    }
}
