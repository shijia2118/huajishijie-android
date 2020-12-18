package com.test.tworldapplication.activity.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.order.OrderKhListActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PayResult;
import com.test.tworldapplication.entity.PostBalanceQuery;
import com.test.tworldapplication.entity.PostReAdd;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestReAdd;
import com.test.tworldapplication.http.AccountHttp;
import com.test.tworldapplication.http.AccountRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountBalanceActivity extends BaseActivity {

    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.ll_input)
    RelativeLayout llInput;
    @BindView(R.id.ll_weixin)
    LinearLayout llWeixin;
    @BindView(R.id.ll_zhifubao)
    LinearLayout llZhifubao;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.imgWeixin)
    ImageView imgWeixin;
    @BindView(R.id.imgZhifubao)
    ImageView imgZhifubao;
    RequestReAdd requestReAdd;
    private static final int SDK_PAY_FLAG = 1;
    String from, out_trade_no, timestamp, total_amount;
    @BindView(R.id.tvCollection)
    TextView tvCollection;

    /*不管支付成功与否,都要跳转支付结果页面,传递三个参数*/

    @SuppressLint("HandlerLeak")
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
                    Intent intent = new Intent(AccountBalanceActivity.this, PayResultActivity.class);
                    intent.putExtra("from", "1");
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                        intent.putExtra("flag", Integer.valueOf(0));

                        Toast.makeText(AccountBalanceActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("flag", Integer.valueOf(1));
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(AccountBalanceActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    intent.putExtra("out_trade_no", out_trade_no);
                    intent.putExtra("timestamp", timestamp);
                    intent.putExtra("total_amount", total_amount);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                    AccountBalanceActivity.this.finish();
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
        setContentView(R.layout.activity_account_balance);
        ButterKnife.bind(this);
        setBackGroundTitle("账户查询与充值", true);
        tvCollection.setText("充值记录");
        tvCollection.setVisibility(View.VISIBLE);
        initView();
    }

    public void initView() {
        llWeixin.setVisibility(View.GONE);
        imgWeixin.setImageResource(R.drawable.shape_checkbox_noclick);
        imgZhifubao.setImageResource(R.drawable.shape_checkbox_click);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Util.isLog(AccountBalanceActivity.this)) {
        HttpPost<PostBalanceQuery> httpPost = new HttpPost<>();
        PostBalanceQuery postBalanceQuery = new PostBalanceQuery();
        postBalanceQuery.setSession_token(Util.getLocalAdmin(AccountBalanceActivity.this)[0]);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postBalanceQuery);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postBalanceQuery) + BaseCom.APP_PWD));
        new AccountHttp().balanceQuery(AccountRequest.balanceQuery(new SuccessValue<HttpRequest<RequestBalanceQuery>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestBalanceQuery> value) {
                Toast.makeText(AccountBalanceActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                if (value.getCode() == BaseCom.NORMAL)
                    tvBalance.setText(value.getData().getBalance().doubleValue() + "元");
                else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(AccountBalanceActivity.this, LoginActivity.class);
            }
        }), httpPost);
//        }
    }

    @OnClick({R.id.ll_weixin, R.id.ll_zhifubao, R.id.tvNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_weixin:
                imgWeixin.setImageResource(R.drawable.shape_checkbox_click);
                imgZhifubao.setImageResource(R.drawable.shape_checkbox_noclick);
                break;
            case R.id.ll_zhifubao:

//                postReAdd.setNumber();
                break;
            case R.id.tvNext:
                if (!Util.isFastDoubleClick()) {
                    total_amount = etMoney.getText().toString();
                    if (total_amount.equals("") || total_amount.equals("0")) {
                        Util.createToast(AccountBalanceActivity.this, "请输入正确的充值金额!");
                    } else if (Double.valueOf(total_amount) > 50000) {
                        Util.createToast(AccountBalanceActivity.this, "充值金额不得大于50000元!");
                    } else {
                        HttpPost<PostReAdd> httpPost = new HttpPost<>();
                        PostReAdd postReAdd = new PostReAdd();
                        postReAdd.setSession_token(Util.getLocalAdmin(AccountBalanceActivity.this)[0]);
                        postReAdd.setMoney(total_amount);
                        postReAdd.setNumber(total_amount);
                        postReAdd.setRechargeMethod("1");
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postReAdd);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postReAdd) + BaseCom.APP_PWD));
                        Log.d("aaa", gson.toJson(httpPost));
                        new AccountHttp().reAddRecharge(AccountRequest.reAddRecharge(AccountBalanceActivity.this, dialog, new SuccessValue<RequestReAdd>() {
                            @Override
                            public void OnSuccess(final RequestReAdd value) {
                                requestReAdd = value;
                                Log.d("aaa", value.getRequest());
                                out_trade_no = value.getOrderNo();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                                timestamp = df.format(new Date());
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        PayTask alipay = new PayTask(AccountBalanceActivity.this);
                                        Map<String, String> result = alipay.payV2(value.getRequest(), true);
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
                        }), httpPost);
                    }
                }
//}
                break;
        }
    }

    @OnClick(R.id.tvCollection)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            Intent intent = new Intent(AccountBalanceActivity.this, OrderKhListActivity.class);
            intent.putExtra("from", "5");
            startActivity(intent);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }
}
