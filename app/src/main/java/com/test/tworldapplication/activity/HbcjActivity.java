package com.test.tworldapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.CjRecordActivity;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.PersonInformationActivity;
import com.test.tworldapplication.activity.card.ReplaceCardActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostLogin;
import com.test.tworldapplication.entity.PostLuckNumber;
import com.test.tworldapplication.entity.PostTips;
import com.test.tworldapplication.entity.RequestLuckNumber;
import com.test.tworldapplication.entity.RequestLuckeyDraw;
import com.test.tworldapplication.entity.RequestTips;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

public class HbcjActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTimes)
    TextView tvTimes;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvRegular)
    TextView tvRegular;
    @BindView(R.id.tvCollection)
    TextView tvCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbcj);
        ButterKnife.bind(this);
        tvTitle.setText("抽奖活动");
        tvCollection.setText("抽奖记录");
        tvCollection.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        HttpPost<PostTips> httpPost = new HttpPost<>();
        PostTips postTips = new PostTips();
        postTips.setSession_token(Util.getLocalAdmin(HbcjActivity.this)[0]);
        postTips.setType(3);

        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postTips);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTips) + BaseCom.APP_PWD));

        new CardHttp().getTips(CardRequest.getTips(new SuccessValue<HttpRequest<RequestTips>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestTips> value) {
                if (value.getCode() == 10000 & value.getData().getCount() > 0) {
                    String toast = "";
                    for (int i = 0; i < value.getData().getTips().length; i++) {
                        if (i == 0) {
                            tvTime.setText(value.getData().getTips()[i].getTips());
                        } else {
                            toast += i + ". " + value.getData().getTips()[i].getTips();
                            toast += "\n";
                        }
                    }
                    tvRegular.setText(toast);
                }
            }
        }), httpPost);

    }


    @Override
    protected void onResume() {
        super.onResume();
        HttpPost<PostLuckNumber> httpPost = new HttpPost<PostLuckNumber>();
        PostLuckNumber postLogin = new PostLuckNumber();
        postLogin.setSession_token(Util.getLocalAdmin(HbcjActivity.this)[0]);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postLogin) + BaseCom.APP_PWD));
        httpPost.setParameter(postLogin);
        Log.d("aaa", gson.toJson(httpPost));
        new AdminHttp().getLuckNumbers(new Subscriber<HttpRequest<RequestLuckNumber>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLuckNumber> requestLuckNumberHttpRequest) {
                if (requestLuckNumberHttpRequest.getCode() == BaseCom.NORMAL)
                    tvTimes.setText("您还有" + requestLuckNumberHttpRequest.getData().getNumbers() + "次抽奖机会");
                else
                    tvTimes.setText("您还有0次抽奖机会");
            }
        }, httpPost);
    }

    @OnClick({R.id.imgBack, R.id.tvCollection, R.id.imgCj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                AppManager.getAppManager().finishActivity();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.imgCj:
//                startActivity(new Intent(HbcjActivity.this, CjjgActivity.class).putExtra("name", "胡"));
                HttpPost<PostLuckNumber> httpPost = new HttpPost<PostLuckNumber>();
                PostLuckNumber postLogin = new PostLuckNumber();
                postLogin.setSession_token(Util.getLocalAdmin(HbcjActivity.this)[0]);
                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postLogin) + BaseCom.APP_PWD));
                httpPost.setParameter(postLogin);
                Log.d("aaa", gson.toJson(httpPost));
                new AdminHttp().luckyDraw(new Subscriber<HttpRequest<RequestLuckeyDraw>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpRequest<RequestLuckeyDraw> requestLuckeyDrawHttpRequest) {
                        if (requestLuckeyDrawHttpRequest.getCode() == BaseCom.NORMAL)
                            startActivity(new Intent(HbcjActivity.this, CjjgActivity.class).putExtra("name", requestLuckeyDrawHttpRequest.getMes()));
                        else
                            Toast.makeText(HbcjActivity.this, requestLuckeyDrawHttpRequest.getMes() + "", Toast.LENGTH_SHORT).show();
                    }
                }, httpPost);


                break;
            case R.id.tvCollection:
                startActivity(new Intent(HbcjActivity.this, CjRecordActivity.class));

                break;
        }
    }
}
