package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.account.PayResultActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PayResult;
import com.test.tworldapplication.entity.PostCaution;
import com.test.tworldapplication.entity.PostCautionOrder;
import com.test.tworldapplication.entity.PostPay;
import com.test.tworldapplication.entity.RequestCautionOrder;
import com.test.tworldapplication.entity.RequestReAdd;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.Util;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

public class CautionActivity extends BaseActivity {

    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvDiscription)
    TextView tvDiscription;
    RequestCautionOrder requestCautionOrder;

    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.d("sss", resultInfo);
                    // 判断resultStatus 为9000则代表支付成功

                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(CautionActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        AppManager.getAppManager().finishActivity();
//                        Intent intent = new Intent(CautionActivity.this, HuajiCuteDetailActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(CautionActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caution);
        ButterKnife.bind(this);
        setBackGroundTitle("保证金缴纳", true);
        PostCautionOrder postCaution = new PostCautionOrder();
        postCaution.setSession_token(Util.getLocalAdmin(CautionActivity.this)[0]);
        HttpPost<PostCautionOrder> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCaution) + BaseCom.APP_PWD));
        httpPost.setParameter(postCaution);
        new CardHttp().bondAmount(httpPost, new Subscriber<HttpRequest<RequestCautionOrder>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestCautionOrder> requestCautionOrderHttpRequest) {
                if (requestCautionOrderHttpRequest.getCode() == BaseCom.NORMAL) {
                    requestCautionOrder = requestCautionOrderHttpRequest.getData();
                    tvMoney.setText("￥" + new BigDecimal(requestCautionOrderHttpRequest.getData().getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    tvUser.setText("缴纳用户:   " + requestCautionOrderHttpRequest.getData().getUserName());
                    tvOrderNo.setText("订单号:  " + requestCautionOrderHttpRequest.getData().getOrderNo());
                    tvDiscription.setText("保证金说明:  " + requestCautionOrderHttpRequest.getData().getDescription());

                }

            }
        });

    }

    @OnClick(R.id.tvSubmit)
    public void onClick() {
        PostPay postCaution = new PostPay();
        postCaution.setSession_token(Util.getLocalAdmin(CautionActivity.this)[0]);
        postCaution.setUserName(requestCautionOrder.getUserName());
        postCaution.setOrderNo(requestCautionOrder.getOrderNo());
        postCaution.setAmount(requestCautionOrder.getAmount());

        HttpPost<PostPay> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCaution) + BaseCom.APP_PWD));
        httpPost.setParameter(postCaution);
        Log.d("qqq", gson.toJson(httpPost));

        new CardHttp().bondOrder(httpPost, new Subscriber<HttpRequest<RequestReAdd>>() {
            @Override
            public void onCompleted() {


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final HttpRequest<RequestReAdd> requestReAddHttpRequest) {
                if (requestReAddHttpRequest.getCode() == BaseCom.NORMAL) {
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(CautionActivity.this);
                            Map<String, String> result = alipay.payV2(requestReAddHttpRequest.getData().getRequest(), true);
                            Log.i("msp", result.toString());

                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }

            }
        });
    }
}
