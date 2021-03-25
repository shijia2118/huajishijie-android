package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostCaution;
import com.test.tworldapplication.entity.RequestCaution;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.Util;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class QdsCuteSelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qds_cute_phone);
        ButterKnife.bind(this);
        setBackGroundTitle("入口选择", true);
    }

    @OnClick({R.id.rl_dailishang, R.id.rl_qudaoshang,R.id.rl_baika})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_dailishang:
                startActivity("0");

                break;
            case R.id.rl_qudaoshang:


                PostCaution postCaution = new PostCaution();
                postCaution.setSession_token(Util.getLocalAdmin(QdsCuteSelectActivity.this)[0]);
                HttpPost<PostCaution> httpPost = new HttpPost<>();
                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCaution) + BaseCom.APP_PWD));
                httpPost.setParameter(postCaution);
                Log.d("aaa", gson.toJson(httpPost));
                new CardHttp().isBond(httpPost, new Subscriber<HttpRequest<RequestCaution>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpRequest<RequestCaution> requestCautionHttpRequest) {
                        if (requestCautionHttpRequest.getData().getIsBond().equals("Y")) {
                            /*已缴纳保证金*/
                            startActivity("1");


                        } else if (requestCautionHttpRequest.getData().getIsBond().equals("N")) {
                            if (requestCautionHttpRequest.getData().getIsQuaBond().equals("Y")) {
                                Intent intent = new Intent(QdsCuteSelectActivity.this, CautionActivity.class);
                                startActivity(intent);
                            } else if (requestCautionHttpRequest.getData().getIsQuaBond().equals("N")) {
                                Toast.makeText(QdsCuteSelectActivity.this, "您暂未开通此功能", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


                break;

            case R.id.rl_baika:
                gotoActy( BkApplyActivity.class );
                break;
        }
    }

    private void startActivity(String from) {
        if (!Util.isFastDoubleClick()) {
            Intent intent1 = new Intent(QdsCuteSelectActivity.this, DailiCuteActivity.class);
            intent1.putExtra("from", from);
            startActivity(intent1);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }
}
