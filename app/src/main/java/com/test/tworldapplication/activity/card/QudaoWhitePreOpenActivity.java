package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostPreNumber;
import com.test.tworldapplication.entity.RequestPreNumber;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class QudaoWhitePreOpenActivity extends BaseActivity {

    @BindView(R.id.etNumber)
    EditText etNumber;

    RequestPreNumber mRequestPreNumber;
    @BindView(R.id.tvToast)
    TextView tvToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qudao_white_pre_open);
        ButterKnife.bind(this);
        setBackGroundTitle("白卡预开户", true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etNumber.setText("");
    }


    @OnClick({R.id.tvNext, R.id.imgDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                if (!Util.isFastDoubleClick()) {
                    if (etNumber.getText().toString().length() == 11) {


                        PostPreNumber postPreNumber = new PostPreNumber();
                        postPreNumber.setNumber(etNumber.getText().toString());
                        postPreNumber.setSession_token(Util.getLocalAdmin(QudaoWhitePreOpenActivity.this)[0]);
                        HttpPost<PostPreNumber> httpPost = new HttpPost<>();
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postPreNumber);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPreNumber) + BaseCom.APP_PWD));

                        new CardHttp().ePreNumber(httpPost, new Subscriber<HttpRequest<RequestPreNumber>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpRequest<RequestPreNumber> requestPreNumberHttpRequest) {
                                Toast.makeText(QudaoWhitePreOpenActivity.this, requestPreNumberHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
                                if (requestPreNumberHttpRequest.getCode() == BaseCom.NORMAL) {
                                    mRequestPreNumber = requestPreNumberHttpRequest.getData();
                                    Intent intent = new Intent(QudaoWhitePreOpenActivity.this, QudaoWhitePhoneDetailActivity.class);
                                    intent.putExtra("from", "3");
                                    intent.putExtra("data", mRequestPreNumber);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                } else if (requestPreNumberHttpRequest.getCode() == BaseCom.LOSELOG || requestPreNumberHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                                    Util.gotoActy(QudaoWhitePreOpenActivity.this, LoginActivity.class);
                                } else {
                                    tvToast.setVisibility(View.VISIBLE);

//                        Util.gotoActy(QudaoWhitePreOpenActivity.this, LoginActivity.class);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(QudaoWhitePreOpenActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.imgDelete:
                etNumber.setText("");
                break;
        }
    }
}
