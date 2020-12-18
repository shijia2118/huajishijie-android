package com.test.tworldapplication.activity.card;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alipay.sdk.app.PayTask;
import com.plk.bluetoothlesdk.PlkBleConnectCallback;
import com.plk.bluetoothlesdk.PlkBleService;
import com.plk.bluetoothlesdk.PlkException;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangAgent;
import com.test.tworldapplication.entity.LiangRecords;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.PayResult;
import com.test.tworldapplication.entity.PostAgentsLiangNumber;
import com.test.tworldapplication.entity.PostBalanceQuery;
import com.test.tworldapplication.entity.PostImsi;
import com.test.tworldapplication.entity.PostLiangPay;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.http.AccountHttp;
import com.test.tworldapplication.http.AccountRequest;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.RequestLiangPay;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

public class HuajiCuteDetailActivity extends BaseActivity implements PlkBleConnectCallback {
    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvPreStore)
    TextView tvPreStore;
    @BindView(R.id.tvLess)
    TextView tvLess;
    @BindView(R.id.ll_package)
    LinearLayout llPackage;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.tvIccid)
    TextView tvIccid;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvIccidText)
    TextView tvIccidText;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvNetwork)
    TextView tvNetwork;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.tvYue)
    TextView tvYue;
    @BindView(R.id.tvOrderValue)
    TextView tvOrderValue;
    @BindView(R.id.imgZhifubao)
    ImageView imgZhifubao;
    @BindView(R.id.imgWeixin)
    ImageView imgWeixin;
    @BindView(R.id.ll_iccid)
    LinearLayout llIccid;
    private LiangAgent liangAgent;
    AgentsLiangNumber agentsLiangNumber;
    Package mPackage = null;
    Promotion mPromotion = null;
    String[] mac;
    String strIccid = "";
    private BluetoothAdapter btAdapt;
    private PlkBleService bleService;
    private int receive = 0;
    RequestImsi requestImsi = null;
    private static final int REQUEST_ENABLE_BT = 2;
    Double yue = null;
    int buyOrNext = 0;
    String payType = "0";
    private static final int SDK_PAY_FLAG = 1;
    RequestLiangPay requestLiangPay;
    int hasWrite = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huaji_cute);
        ButterKnife.bind(this);
        setBackGroundTitle("号码详情", true);
        liangAgent = (LiangAgent) getIntent().getSerializableExtra("data");
        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
        bleService = new PlkBleService(HuajiCuteDetailActivity.this);
        dialog.getTvTitle().setText("正在获取信息");
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyOrNext == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HuajiCuteDetailActivity.this);
                    builder.setMessage("若返回上一页，将不对已支付金额进行返还，请确认是否返回？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AppManager.getAppManager().finishActivity();
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    AppManager.getAppManager().finishActivity();
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
            }
        });
        getData();
    }


//        if (mPackage == null || mPromotion == null) {
//            Toast.makeText(HuajiCuteDetailActivity.this, "请将套餐和活动包选择完整", Toast.LENGTH_SHORT).show();
//        } else {

//        }


    private void getData() {
        PostAgentsLiangNumber postAgentsLiangNumber = new PostAgentsLiangNumber();//Util.getLocalAdmin(getActivity())[0]
        postAgentsLiangNumber.setSession_token(Util.getLocalAdmin(HuajiCuteDetailActivity.this)[0]);
        postAgentsLiangNumber.setNumber(liangAgent.getNumber());
        HttpPost httpPost = new HttpPost();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postAgentsLiangNumber);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postAgentsLiangNumber) + BaseCom.APP_PWD));

        new CardHttp().getHjsjLiangNumber(httpPost, new Subscriber<HttpRequest<AgentsLiangNumber>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<AgentsLiangNumber> agentsLiangNumberHttpRequest) {
                Util.createToast(HuajiCuteDetailActivity.this, agentsLiangNumberHttpRequest.getMes());
                if (agentsLiangNumberHttpRequest.getCode() == BaseCom.NORMAL) {
                    agentsLiangNumber = agentsLiangNumberHttpRequest.getData();
                    initView();
                } else if (agentsLiangNumberHttpRequest.getCode() == BaseCom.LOSELOG || agentsLiangNumberHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(HuajiCuteDetailActivity.this, LoginActivity.class);
                }
            }
        });

        HttpPost<PostBalanceQuery> httpPost1 = new HttpPost<>();
        PostBalanceQuery postBalanceQuery = new PostBalanceQuery();
        postBalanceQuery.setSession_token(Util.getLocalAdmin(HuajiCuteDetailActivity.this)[0]);
        httpPost1.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost1.setParameter(postBalanceQuery);
        httpPost1.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postBalanceQuery) + BaseCom.APP_PWD));
        new AccountHttp().balanceQuery(AccountRequest.balanceQuery(new SuccessValue<HttpRequest<RequestBalanceQuery>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestBalanceQuery> value) {
//                Toast.makeText(HuajiCuteDetailActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                if (value.getCode() == BaseCom.NORMAL) {
                    yue = value.getData().getBalance();
                    tvYue.setText("当前余额:" + value.getData().getBalance().doubleValue() + "元");
                } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(HuajiCuteDetailActivity.this, LoginActivity.class);
            }
        }), httpPost1);


    }

    private void initView() {
//        tvPackage.setText(agentsLiangNumber.getPackages().getName());
//        tvActivity.setText(agentsLiangNumber.getPromotions().getName());
        String state = "";
        switch (agentsLiangNumber.getLiangStatus()) {
            case 0:
                state = "未分配";
                break;
            case 1:
                state = "已激活";
                break;
            case 2:
                state = "已使用";
                break;
            case 3:
                state = "锁定";
                break;
            case 4:
                state = "开户中";
                break;
        }
        tvState.setText(state);
        tvNumber.setText(agentsLiangNumber.getNumber());
        tvNetwork.setText(agentsLiangNumber.getNetwork());
        tvAddress.setText(agentsLiangNumber.getProvinceName() + agentsLiangNumber.getCityName());
        tvPreStore.setText(agentsLiangNumber.getPrestore() + "元");
        tvLess.setText(agentsLiangNumber.getMinConsumption() + "元");
        tvOrderValue.setText(agentsLiangNumber.getPrestore() + "元");
    }

    @OnClick({R.id.ll_package, R.id.ll_activity, R.id.tvBuy, R.id.ll_zhifubao, R.id.ll_zhanghu, R.id.tvIccid})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_zhifubao:
                payType = "1";
                imgZhifubao.setImageResource(R.drawable.shape_checkbox_click);
                imgWeixin.setImageResource(R.drawable.shape_checkbox_noclick);
                break;
            case R.id.ll_zhanghu:
                payType = "0";
                imgWeixin.setImageResource(R.drawable.shape_checkbox_click);
                imgZhifubao.setImageResource(R.drawable.shape_checkbox_noclick);
                break;
            case R.id.ll_package:
                if (!Util.isFastDoubleClick()) {
                    if (agentsLiangNumber != null) {
                        Intent intent = new Intent(HuajiCuteDetailActivity.this, PackageSelectDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("agentsLiangNumber", agentsLiangNumber);
                        intent.putExtra("flag", "2");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 0);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                }
                break;
            case R.id.ll_activity:
                if (!Util.isFastDoubleClick()) {
                    if (mPackage == null) {
                        Util.createToast(HuajiCuteDetailActivity.this, "请选择套餐!");
                    } else {
                        Intent intent1 = new Intent(HuajiCuteDetailActivity.this, ActivitySelectDetailActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("package", mPackage);
                        intent1.putExtra("flag", "1");
                        intent1.putExtras(bundle1);
                        startActivityForResult(intent1, 1);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                }
                break;
            case R.id.tvBuy:
                if (!Util.isFastDoubleClick()) {
                    if (buyOrNext == 0) {
                        if (mPackage == null || mPromotion == null) {
                            Toast.makeText(HuajiCuteDetailActivity.this, "请先选择套餐和活动包", Toast.LENGTH_SHORT).show();
                        } else if (yue == null) {
                            yue = 0d;
                        } else if (payType.equals("0") && yue < Double.valueOf(agentsLiangNumber.getPrestore())) {
                            Toast.makeText(HuajiCuteDetailActivity.this, "当前余额不足,请选择其他支付方式", Toast.LENGTH_SHORT).show();
                        } else  {
                            PostLiangPay postLiangPay = new PostLiangPay();
                            postLiangPay.setSession_token(Util.getLocalAdmin(HuajiCuteDetailActivity.this)[0]);
                            postLiangPay.setNumber(agentsLiangNumber.getNumber());
                            postLiangPay.setOrderAmount(agentsLiangNumber.getPrestore());
                            postLiangPay.setPayAmount(agentsLiangNumber.getPrestore());
                            postLiangPay.setPayMethod(payType);
                            HttpPost<PostLiangPay> httpPost1 = new HttpPost<>();
                            httpPost1.setApp_key(Util.encode(BaseCom.APP_KEY));
                            httpPost1.setParameter(postLiangPay);
                            httpPost1.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postLiangPay) + BaseCom.APP_PWD));
                            new CardHttp().liangPay(httpPost1, new Subscriber<HttpRequest<RequestLiangPay>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(final HttpRequest<RequestLiangPay> requestLiangPayHttpRequest) {
                                    Util.createToast(HuajiCuteDetailActivity.this, requestLiangPayHttpRequest.getMes());
                                    if (requestLiangPayHttpRequest.getCode() == BaseCom.NORMAL) {
                                        requestLiangPay = requestLiangPayHttpRequest.getData();
                                        switch (payType) {
                                            case "0":
                                                BaseCom.payMethod = "0";
                                                tvBuy.setText("下一步");
                                                llActivity.setClickable(false);
                                                llPackage.setClickable(false);
                                                llShow.setVisibility(View.INVISIBLE);
                                                llIccid.setVisibility(View.VISIBLE);
                                                buyOrNext = 1;
                                                break;
                                            case "1":
                                                BaseCom.payMethod = "0";
                                                Runnable payRunnable = new Runnable() {
                                                    //
                                                    @Override
                                                    public void run() {
                                                        PayTask alipay = new PayTask(HuajiCuteDetailActivity.this);
                                                        Map<String, String> result = alipay.payV2(requestLiangPayHttpRequest.getData().getRequest(), true);
                                                        Log.i("msp", result.toString());

                                                        Message msg = new Message();
                                                        msg.what = SDK_PAY_FLAG;
                                                        msg.obj = result;
                                                        handler.sendMessage(msg);
                                                    }
                                                };

                                                Thread payThread = new Thread(payRunnable);
                                                payThread.start();
                                                break;
                                        }
                                    } else if (requestLiangPayHttpRequest.getCode() == BaseCom.LOSELOG || requestLiangPayHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                                        Util.gotoActy(HuajiCuteDetailActivity.this, LoginActivity.class);
                                }
                            });



                /*调用接口支付  支付成功之后  button变成下一步  选择套餐失去焦点 支付宝账户隐藏 设置buyOrNext=1*/
                        }
                    } else if (buyOrNext == 1) {
//                        strIccid="89860116841318291027";
//                        hasWrite=1;

                        if (mPackage == null || mPromotion == null) {
                            Util.createToast(HuajiCuteDetailActivity.this, "请选择套餐和活动包!");
                        } else if (strIccid.equals("")) {
                            Util.createToast(HuajiCuteDetailActivity.this, "请写卡!");
                        } else if (hasWrite == 1){
                            LiangRecords liangRecords = new LiangRecords();
                            liangRecords.setSession_token(Util.getLocalAdmin(HuajiCuteDetailActivity.this)[0]);
                            liangRecords.setNumber(agentsLiangNumber.getNumber());
//                            liangRecords.setNumber("17078861000");

                            liangRecords.setIccid(strIccid);
                            HttpPost<LiangRecords> httpPost1 = new HttpPost<>();
                            httpPost1.setApp_key(Util.encode(BaseCom.APP_KEY));
                            httpPost1.setParameter(liangRecords);
                            httpPost1.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(liangRecords) + BaseCom.APP_PWD));

                            new CardHttp().liangRecords(httpPost1, new Subscriber<HttpRequest<LiangRecords>>() {
                                @Override
                                public void onCompleted() {

                                    HttpPost<PostOpenPower> httpPost0=new HttpPost<>();
                                    PostOpenPower postOpenPower=new PostOpenPower();
                                    postOpenPower.setSession_token(Util.getLocalAdmin(HuajiCuteDetailActivity.this)[0]);
                                    httpPost0.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                                    httpPost0.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpenPower) + BaseCom.APP_PWD));
                                    httpPost0.setParameter(postOpenPower);
                                    Log.d("aaa", gson.toJson(httpPost0));
                                    new AdminHttp().getOpenPower( AdminRequest.getOpenPower( HuajiCuteDetailActivity.this, new SuccessValue<RequestOpenPower>() {
                                        @Override
                                        public void OnSuccess(RequestOpenPower value) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt( "pattern",value.getPattern() );
                                        editor.putInt( "modes",value.getModes());
                                        editor.putInt( "readModes",value.getReadModes());
                                        editor.commit();

                                    agentsLiangNumber.setSelectPackage(mPackage);
                                    agentsLiangNumber.setSelectPromotion(mPromotion);
                                    Intent intent = new Intent(HuajiCuteDetailActivity.this, HuajiSelectActivity.class);
                                    intent.putExtra("from", "2");
                                    intent.putExtra("agentsLiangNumber", agentsLiangNumber);

//                                    requestImsi=new RequestImsi();
//                                    requestImsi.setIccid( "89860116841318291027" );
//                                    requestImsi.setImsi( "123456789000000" );
//                                    requestImsi.setSmscent( "+8613010360500" );
//                                    requestImsi.setSimId( "71" );
//                                    requestImsi.setPrestore( "0" );
                                    intent.putExtra("requestImsi", requestImsi);
                                    intent.putExtra("requestLiangPay", requestLiangPay);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                                        }
                                    } ),httpPost0 );
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HttpRequest<LiangRecords> liangRecordsHttpRequest) {

                                }
                            });
                        }
                    }
                }
                break;
            case R.id.tvIccid:
                if (!Util.isFastDoubleClick()) {
                    tvIccid.startAnimation(mAnimation);
                    mAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            mac = getAddressmac();
                            Log.d("ccc", mac[0]);
                            Log.d("ccc", mac[1]);
                            if (btAdapt.isEnabled()) {
                                if (mac[1].equals("")) {
                /*蓝牙地址未存本地,跳转选择蓝牙设备界面*/
                                    Intent serverIntent2 = new Intent(HuajiCuteDetailActivity.this, BlueToothActivity.class);
                                    startActivityForResult(serverIntent2, 12);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                } else {
                                    try {
                                        if (receive == 0) {
                                            dialog.getTvTitle().setText("正在连接设备!");
                                            dialog.show();
                                            bleService.connectDevice(mac[1], HuajiCuteDetailActivity.this);
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HuajiCuteDetailActivity.this);
                                            builder.setMessage("是否写入设备" + mac[0]);
                                            builder.setTitle("提示");
                                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface alertDialog, int which) {
                                                    alertDialog.dismiss();
                                                    Util.createToast(HuajiCuteDetailActivity.this, "正在写入数据!");
                                                    try {
/*读iccid*/
                                                        String atr = bleService.resetDevice();
                                                        Log.d("aaa", String.valueOf(atr));
                                                        String apdu1 = "A0A40000023F00";
                                                        String apdu2 = "A0A40000022FE2";
                                                        String apdu3 = "A0B000000A";
                                                        String resp1 = bleService.transmitDataSync(apdu1);
                                                        String resp2 = bleService.transmitDataSync(apdu2);
                                                        String resp3 = bleService.transmitDataSync(apdu3).substring(0, 20);

                                                        strIccid = Util.swap(resp3);
                                                        tvIccidText.setText(strIccid);
                                                        Log.d("aaa", strIccid);
                                    /*根据iccid获取imsi*/


                                                        HttpPost<PostImsi> httpPost = new HttpPost<>();
                                                        PostImsi postImsi = new PostImsi();
                                                        postImsi.setSession_token(Util.getLocalAdmin(HuajiCuteDetailActivity.this)[0]);
                                                        postImsi.setNumber(agentsLiangNumber.getNumber());
                                                        postImsi.setIccid(strIccid);
                                                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                                                        httpPost.setParameter(postImsi);
                                                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postImsi) + BaseCom.APP_PWD));
                                                        Log.d("aaa", gson.toJson(httpPost));

                                                        new CardHttp().liangImsi(httpPost, new Subscriber<HttpRequest<RequestImsi>>() {
                                                            @Override
                                                            public void onCompleted() {

                                                            }

                                                            @Override
                                                            public void onError(Throwable e) {

                                                            }

                                                            @Override
                                                            public void onNext(HttpRequest<RequestImsi> requestImsiHttpRequest) {
                                                                Toast.makeText(HuajiCuteDetailActivity.this, requestImsiHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
                                                                if (requestImsiHttpRequest.getCode() == BaseCom.NORMAL) {
                                                                    requestImsi = requestImsiHttpRequest.getData();
                                                                    requestImsi.setIccid(strIccid);
                                                                    Log.d("aaa", requestImsi.getImsi());
                                                                    String v = "809" + requestImsi.getImsi();
                                                                    String apdu = "A0F4000012" + Util.swap(v) + Util.swap(v);
                                                                    try {
                                                                        Log.d("aaa", apdu);
                                                                        String resp = bleService.transmitDataSync(apdu);
                                                                        Log.d("aaa", resp);
                                                                        if (resp.equals("9000")) {
                                                    /*写入短信中心号*/
                                                                            String c = requestImsi.getSmscent().replace("+86", "") + "F";
                                                                            Log.d("yyy", c);
                                                                            String a = "089168" + Util.swap(c);
                                                                            Log.d("aaa", a);
//                                                    String atr = bleService.resetDevice();
                                                                            String apdu01 = "002000010831323334FFFFFFFF";
                                                                            String apdu02 = "A0A40000023F00";
                                                                            String apdu03 = "A0A40000027FF0";
                                                                            String apdu04 = "A0A40000026F42";
                                                                            String apdu05 = "A0DC010428FFFFFFFFFFFFFFFFFFFFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFF" + a + "FFFFFFFFFFFFFF";
                                                                            Log.d("yyy", apdu01);
                                                                            String resp01 = bleService.transmitDataSync(apdu01);
                                                                            Log.d("yyy", resp01);
                                                                            Log.d("yyy", apdu02);
                                                                            String resp02 = bleService.transmitDataSync(apdu02);
                                                                            Log.d("yyy", resp02);
                                                                            Log.d("yyy", apdu03);
                                                                            String resp03 = bleService.transmitDataSync(apdu03);
                                                                            Log.d("yyy", resp03);
                                                                            Log.d("yyy", apdu04);
                                                                            String resp04 = bleService.transmitDataSync(apdu04);
                                                                            Log.d("yyy", resp04);
                                                                            Log.d("yyy", apdu05);
                                                                            String resp05 = bleService.transmitDataSync(apdu05);
                                                                            Log.d("yyy", resp05);
                                                                            if (resp05.equals("9000")) {
                                                                                tvIccid.setVisibility(View.GONE);
                                                                                tvBuy.setVisibility(View.VISIBLE);
                                                                                tvIccidText.setVisibility(View.VISIBLE);
                                                                                hasWrite = 1;
                                                                                Util.createToast(HuajiCuteDetailActivity.this, "写卡成功,请点击下一步!");
                                                                            } else {
                                                                                Toast.makeText(HuajiCuteDetailActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                                                        .show();
                                                                                tvBuy.setVisibility(View.GONE);


                                                                            }

                                                                        } else {
                                                                            Toast.makeText(HuajiCuteDetailActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                                                    .show();
                                                                            tvBuy.setVisibility(View.GONE);
                                                                        }
                                                                    } catch (PlkException e) {
                                                                        e.printStackTrace();
                                                                        Toast.makeText(HuajiCuteDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                                                .show();
                                                                    }
                                                                } else if (requestImsiHttpRequest.getCode() == BaseCom.LOSELOG || requestImsiHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                                                                    Util.gotoActy(HuajiCuteDetailActivity.this, LoginActivity.class);
                                                                }
                                                            }
                                                        });
//
                                                    } catch (PlkException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                        Toast.makeText(HuajiCuteDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                                .show();
                                                    }

                                                }
                                            });
                                            builder.setNegativeButton("其他设备", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface alertDialog, int which) {
                                                    alertDialog.dismiss();
                                                    Intent serverIntent2 = new Intent(HuajiCuteDetailActivity.this, BlueToothActivity.class);
                                                    startActivityForResult(serverIntent2, 12);
                                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                                }
                                            });
                                            builder.create().show();
                                        }

//                                addLog(String.valueOf(chars), 2);
                                    } catch (PlkException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                        dialog.dismiss();
                                        receive = 0;
                                        clearAddressmac();
                                        Toast.makeText(HuajiCuteDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                .show();
                                    }


                                }
                            } else {
            /*开启蓝牙*/
                                Intent enableIntent = new Intent(
                                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                break;
        }
    }

    //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            switch (requestCode) {
//                case 0:
//                    mPackage = agentsLiangNumber.getPackages()[data.getIntExtra("select", 0)];
//                    tvPackage.setText(mPackage.getName());
//                    break;
//                case 1:
//                    mPromotion = (Promotion) data.getExtras().getSerializable("select");
//                    tvActivity.setText(mPromotion.getName());
////                tvNext.setVisibility(View.VISIBLE);
//                    break;
//
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleService.destroy();
    }

    private final String READADDRESS = "readAddress";

    private void saveAddressmac(String name, String addressmac) {
        SharedPreferences.Editor editor = this.getSharedPreferences(READADDRESS, MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("addressmac", addressmac);
        editor.commit();
    }

    private void clearAddressmac() {
        SharedPreferences.Editor editor = this.getSharedPreferences(READADDRESS, MODE_PRIVATE).edit();
        editor.putString("name", "");
        editor.putString("addressmac", "");
        editor.commit();
    }

    private String[] getAddressmac() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(READADDRESS, MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String addressmac = sharedPreferences.getString("addressmac", "");
        String[] strings = new String[2];
        strings[0] = name;
        strings[1] = addressmac;
        return strings;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 12:
                    String address = data.getStringExtra("device_address");
                    String name = data.getStringExtra("device_name");
                    saveAddressmac(name, address);
                    try {
                        bleService.connectDevice(address, HuajiCuteDetailActivity.this);
                    } catch (PlkException e) {
                        e.printStackTrace();
                    }
                    break;

                case 0:
                    mPromotion = null;
                    tvActivity.setText("请选择");
                    mPackage = agentsLiangNumber.getPackages()[data.getIntExtra("select", 0)];
                    tvPackage.setText(mPackage.getName());
                    break;
                case 1:
                    mPromotion = (Promotion) data.getExtras().getSerializable("select");
                    tvActivity.setText(mPromotion.getName());
//                tvNext.setVisibility(View.VISIBLE);
                    break;


            }
        } else {
            switch (requestCode) {
//                case 0:
//                    mPromotion = null;
//                    tvActivity.setText("请选择");
//                    break;
                case REQUEST_ENABLE_BT:
                    if (resultCode == RESULT_OK) {
                        receive = 0;
                        mac = getAddressmac();
                        Log.d("ccc", mac[0]);
                        Log.d("ccc", mac[1]);
                        if (mac[1].equals("")) {
                        /*开启蓝牙成功,若本地未存蓝牙地址,跳转设备选择界面*/
                            Intent serverIntent2 = new Intent(HuajiCuteDetailActivity.this, BlueToothActivity.class);
                            startActivityForResult(serverIntent2, 12);
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        } else {
                            try {
                                bleService.connectDevice(mac[1], HuajiCuteDetailActivity.this);
                            } catch (PlkException e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (resultCode == RESULT_CANCELED) {
                        receive = 0;
                        Util.createToast(HuajiCuteDetailActivity.this, "请开启蓝牙!");
//                    finish();
                    }
                    break;
//                case 1:
//                    break;
            }

//            if (requestCode == REQUEST_ENABLE_BT) {
//                if (resultCode == RESULT_OK) {
//                    receive = 0;
//                    mac = getAddressmac();
//                    Log.d("ccc", mac[0]);
//                    Log.d("ccc", mac[1]);
//                    if (mac[1].equals("")) {
//                        /*开启蓝牙成功,若本地未存蓝牙地址,跳转设备选择界面*/
//                        Intent serverIntent2 = new Intent(HuajiCuteDetailActivity.this, BlueToothReadActivity.class);
//                        startActivityForResult(serverIntent2, 12);
//                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                    } else {
//                        try {
//                            bleService.connectDevice(mac[1], HuajiCuteDetailActivity.this);
//                        } catch (PlkException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                } else if (resultCode == RESULT_CANCELED) {
//                    receive = 0;
//                    Util.createToast(HuajiCuteDetailActivity.this, "请手动开启蓝牙!");
////                    finish();
//                }
//            }
//            dialog.dismiss();
        }
    }

    @Override
    public void onConnectSuccess() {
        dialog.dismiss();
        Log.d("aaa", "success");
        receive = 1;
        Util.createToast(HuajiCuteDetailActivity.this, "连接设备成功,请再次点击写卡按钮");
    }

    @Override
    public void onConnectFailed(String s) {
        Log.d("aaa", "fail");
        receive = 0;
        dialog.dismiss();
        Util.createToast(HuajiCuteDetailActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }

    @Override
    public void onConnectLost() {
        Log.d("aaa", "lost");
        receive = 0;
        dialog.dismiss();
        Util.createToast(HuajiCuteDetailActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        tvBuy.setText("下一步");
                        llActivity.setClickable(false);
                        llPackage.setClickable(false);
                        llShow.setVisibility(View.INVISIBLE);
                        llIccid.setVisibility(View.VISIBLE);
                        buyOrNext = 1;
                    } else {
                        Util.createToast(HuajiCuteDetailActivity.this, "支付失败!");
                    }
                    break;
            }
        }
    };
}
