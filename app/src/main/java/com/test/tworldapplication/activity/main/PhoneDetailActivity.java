package com.test.tworldapplication.activity.main;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.luck.picture.lib.tools.ToastUtils;
import com.plk.bluetoothlesdk.PlkBleConnectCallback;
import com.plk.bluetoothlesdk.PlkBleService;
import com.plk.bluetoothlesdk.PlkException;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.PostResult;
import com.test.tworldapplication.activity.card.ActivitySelectDetailActivity;
import com.test.tworldapplication.activity.card.BlueToothActivity;
import com.test.tworldapplication.activity.card.PackageSelectDetailActivity;
import com.test.tworldapplication.activity.card.SelectActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.PostBalanceQuery;
import com.test.tworldapplication.entity.PostImsi;
import com.test.tworldapplication.entity.PostLockNumberNew;
import com.test.tworldapplication.entity.PostPreNumberDetails;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestLockNumberNew;
import com.test.tworldapplication.entity.RequestPreNumberDetails;
import com.test.tworldapplication.http.AccountHttp;
import com.test.tworldapplication.http.AccountRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import sunrise.bluetooth.SRBluetoothCardReader;
import wintone.passport.sdk.utils.AppManager;

import static com.sunrise.icardreader.helper.ConsantHelper.READ_CARD_SUCCESS;

public class PhoneDetailActivity extends BaseActivity implements PlkBleConnectCallback {
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvInternet)
    TextView tvInternet;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.etChooseMoney)
    EditText etChooseMoney;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.etOrderMoney)
    EditText etOrderMoney;
    @BindView(R.id.etOrderPresente)
    EditText etOrderPresente;
    @BindView(R.id.tvGetIMSI)
    TextView tvGetIMSI;
    @BindView(R.id.tvIccid)
    TextView tvIccid;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvNext0)
    TextView tvNext0;
    @BindView(R.id.tvNext1)
    TextView tvNext1;
    @BindView(R.id.activity_package_select)
    LinearLayout activityPackageSelect;
    @BindView(R.id.vICCID)
    View vICCID;
    @BindView(R.id.llICCID)
    LinearLayout llICCID;
    @BindView(R.id.ll_package)
    LinearLayout llPackage;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.vAccount)
    View vAccount;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    private String number;
    private Package mPackage;
    Promotion mPromotion;
    Dialog checkBuyDialog, buyResultDialog, writeSuccessDialog, writeFailedDialog;
    RequestPreNumberDetails requestPreNumberDetails;
    String[] mac;
    private BluetoothAdapter btAdapt;
    private PlkBleService bleService;
    private int receive = 0;
    String strIccid = "";
    RequestImsi requestImsi = null;
    private static final int REQUEST_ENABLE_BT = 2;
    String orderNo;
    SRBluetoothCardReader mSRBlueReaderHelper;
    int getImsiSuccess = 0;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail2);
        ButterKnife.bind(this);
        tvGetIMSI.setText("获取IMSI");
        tvTitle.setText("写卡");
        number = getIntent().getStringExtra("number");
        from = getIntent().getStringExtra("from");
        switch (from) {
            case "0":
                /*失败*/
                llICCID.setVisibility(View.GONE);
                vICCID.setVisibility(View.GONE);
                break;
            case "1":
                /*已锁定*/
                orderNo = getIntent().getStringExtra("orderNo");

                llICCID.setVisibility(View.VISIBLE);
                vICCID.setVisibility(View.VISIBLE);
                llAccount.setVisibility(View.GONE);
                vAccount.setVisibility(View.GONE);
                tvNext.setVisibility(View.GONE);
                tvGetIMSI.setVisibility(View.VISIBLE);
                tvIccid.setVisibility(View.GONE);
                llActivity.setVisibility(View.GONE);
                llPackage.setVisibility(View.GONE);
                llActivity.setClickable(false);
                llPackage.setClickable(false);

                break;
            case "2":
                /*号码写卡激活*/
                llICCID.setVisibility(View.GONE);
                vICCID.setVisibility(View.GONE);
                break;
        }
//        if (from.equals("0") || from.equals("2")) {
//            /*写卡失败订单，正常流程*/
//            llICCID.setVisibility(View.GONE);
//            vICCID.setVisibility(View.GONE);
//        } else {
//            /*已锁定订单*/
//            llICCID.setVisibility(View.VISIBLE);
//            vICCID.setVisibility(View.VISIBLE);
//            llAccount.setVisibility(View.GONE);
//            vAccount.setVisibility(View.GONE);
//            tvNext.setVisibility(View.GONE);
//            tvGetIMSI.setVisibility(View.VISIBLE);
//            tvIccid.setVisibility(View.GONE);
//            llActivity.setVisibility(View.GONE);
//            llPackage.setVisibility(View.GONE);
//            llActivity.setClickable(false);
//            llPackage.setClickable(false);
//        }

        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
        bleService = new PlkBleService(PhoneDetailActivity.this);
        getBalance();
        getDetail();
        initCheckBuyDialog();
        initWriteSuccessDialog();
        initWriteFailedDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleService.destroy();
    }

    private void getBalance() {
        HttpPost<PostBalanceQuery> httpPost = new HttpPost<>();
        PostBalanceQuery postBalanceQuery = new PostBalanceQuery();
        postBalanceQuery.setSession_token(Util.getLocalAdmin(PhoneDetailActivity.this)[0]);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postBalanceQuery);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postBalanceQuery) + BaseCom.APP_PWD));
        new AccountHttp().balanceQuery(AccountRequest.balanceQuery(new SuccessValue<HttpRequest<RequestBalanceQuery>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestBalanceQuery> value) {
                Toast.makeText(PhoneDetailActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                if (value.getCode() == BaseCom.NORMAL)
                    etOrderPresente.setText("当前余额：" + value.getData().getBalance().doubleValue() + "元");
                else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(PhoneDetailActivity.this, LoginActivity.class);

            }
        }), httpPost);
    }

    private void getDetail() {
        PostPreNumberDetails postPreNumberDetails = new PostPreNumberDetails();
        postPreNumberDetails.setSession_token(Util.getLocalAdmin(PhoneDetailActivity.this)[0]);
        postPreNumberDetails.setNumber(number);
        HttpPost<PostPreNumberDetails> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postPreNumberDetails) + BaseCom.APP_PWD));
        httpPost.setParameter(postPreNumberDetails);
        new CardHttp().preNumberDetailsNew(httpPost, new Subscriber<HttpRequest<RequestPreNumberDetails>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPreNumberDetails> requestPreNumberDetailsHttpRequest) {
                if (requestPreNumberDetailsHttpRequest.getCode() == BaseCom.NORMAL) {
                    requestPreNumberDetails = requestPreNumberDetailsHttpRequest.getData();
                    initView();

                } else if (requestPreNumberDetailsHttpRequest.getCode() == BaseCom.LOSELOG || requestPreNumberDetailsHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(PhoneDetailActivity.this, LoginActivity.class);
                else
                    Toast.makeText(PhoneDetailActivity.this, requestPreNumberDetailsHttpRequest.getMes() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        tvNumber.setText(number);
        tvAddress.setText(requestPreNumberDetails.getProvince() + "," + requestPreNumberDetails.getCityName());
        tvInternet.setText(requestPreNumberDetails.getOperatorname());
        etMoney.setText(requestPreNumberDetails.getPrestore());
        etChooseMoney.setText(requestPreNumberDetails.getRegFee());
        tvLimit.setText(requestPreNumberDetails.getCycle());
        etOrderMoney.setText(requestPreNumberDetails.getOrderPrice());

    }

    public void initCheckBuyDialog() {
//        mContext = this;
        checkBuyDialog = new Dialog(PhoneDetailActivity.this);
        checkBuyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkBuyDialog.setContentView(R.layout.dialog_check_buy);
        checkBuyDialog.setCanceledOnTouchOutside(true);
        Window window = checkBuyDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0); //没有边距
        //layoutParams.dimAmount = 0.0f;  //背景不变暗
        layoutParams.dimAmount = 0.7f;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //背景透明
        window.setAttributes(layoutParams);
        TextView refuse = (TextView) checkBuyDialog.findViewById(R.id.refuse);
        TextView agree = (TextView) checkBuyDialog.findViewById(R.id.agree);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBuyDialog.dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBuyDialog.dismiss();
                PostLockNumberNew postLockNumberNew = new PostLockNumberNew();
                postLockNumberNew.setSession_token(Util.getLocalAdmin(PhoneDetailActivity.this)[0]);
                postLockNumberNew.setNumber(number);
                postLockNumberNew.setPoolname(requestPreNumberDetails.getPoolname());
                postLockNumberNew.setCrmCode(requestPreNumberDetails.getCrmCode());
                postLockNumberNew.setCrmUserName(requestPreNumberDetails.getCrmUserName());
                postLockNumberNew.setLiangType(requestPreNumberDetails.getLiangType());
                postLockNumberNew.setIsLiang(requestPreNumberDetails.getIsLiang());
                postLockNumberNew.setPrestore(requestPreNumberDetails.getPrestore());
                postLockNumberNew.setRegFee(requestPreNumberDetails.getRegFee());
                postLockNumberNew.setCycle(requestPreNumberDetails.getCycle());
                postLockNumberNew.setCityName(requestPreNumberDetails.getCityName());
                postLockNumberNew.setProvince(requestPreNumberDetails.getProvince());
                postLockNumberNew.setOperatorname(requestPreNumberDetails.getOperatorname());
                postLockNumberNew.setPackageId(mPackage.getId());
                postLockNumberNew.setPackageName(mPackage.getName());
                postLockNumberNew.setPromotionId(mPromotion.getId());
                postLockNumberNew.setPromotionName(mPromotion.getName());
                postLockNumberNew.setPayAmount(requestPreNumberDetails.getPrestore());
                HttpPost<PostLockNumberNew> httpPost = new HttpPost<>();
                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postLockNumberNew) + BaseCom.APP_PWD));
                httpPost.setParameter(postLockNumberNew);
                dialog.getTvTitle().setText("正在购买");
                dialog.show();
                new CardHttp().lockNumberNew(new Subscriber<HttpRequest<RequestLockNumberNew>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(HttpRequest<RequestLockNumberNew> stringHttpRequest) {
                        dialog.dismiss();
                        if (stringHttpRequest.getCode() == BaseCom.NORMAL) {
                            orderNo = stringHttpRequest.getData().getOrderNo();
                            initBuySuccessDialog(0);
                            buyResultDialog.show();
                        } else if (stringHttpRequest.getCode() == BaseCom.LOSELOG)
                            Util.gotoActy(PhoneDetailActivity.this, LoginActivity.class);
                        else if (stringHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                            Toast.makeText(PhoneDetailActivity.this, stringHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
                        else {
                            initBuySuccessDialog(1);
                            buyResultDialog.show();

                        }
                    }
                }, httpPost);
            }
        });
    }

    private void initBuySuccessDialog(final int flag) {
        buyResultDialog = new Dialog(PhoneDetailActivity.this);
        buyResultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        buyResultDialog.setContentView(R.layout.dialog_buy_success);
        buyResultDialog.setCanceledOnTouchOutside(false);
        if (flag == 0) {
            buyResultDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return true;
                }
            });
            buyResultDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    llICCID.setVisibility(View.VISIBLE);
                    vICCID.setVisibility(View.VISIBLE);
                    llAccount.setVisibility(View.GONE);
                    vAccount.setVisibility(View.GONE);
                    tvNext.setVisibility(View.GONE);
                    tvGetIMSI.setVisibility(View.VISIBLE);
                    tvIccid.setVisibility(View.GONE);
                    llActivity.setClickable(false);
                    llPackage.setClickable(false);

                }
            });
        }
        Window window = buyResultDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0); //没有边距
        layoutParams.dimAmount = 0.7f;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //背景透明
        window.setAttributes(layoutParams);
        TextView agree = (TextView) buyResultDialog.findViewById(R.id.agree);
        TextView tvToast = (TextView) buyResultDialog.findViewById(R.id.tvToast);
        switch (flag) {
            case 0:
                tvToast.setText("购买成功");
                break;
            case 1:
                tvToast.setText("购买失败");
                break;
        }
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyResultDialog.dismiss();
                if (flag == 1)
                    AppManager.getAppManager().finishActivity();
            }
        });
    }

    private void initWriteSuccessDialog() {
        writeSuccessDialog = new Dialog(PhoneDetailActivity.this);
        writeSuccessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        writeSuccessDialog.setContentView(R.layout.dialog_write_success);
        writeSuccessDialog.setCanceledOnTouchOutside(false);
        writeSuccessDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        Window window = writeSuccessDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0); //没有边距
        layoutParams.dimAmount = 0.7f;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //背景透明
        window.setAttributes(layoutParams);
        TextView agree = (TextView) writeSuccessDialog.findViewById(R.id.agree);
        TextView refuse = (TextView) writeSuccessDialog.findViewById(R.id.refuse);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeSuccessDialog.dismiss();
                AppManager.getAppManager().finishActivity();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeSuccessDialog.dismiss();

                tvNext0.setVisibility(View.GONE);
                tvNext1.setVisibility(View.VISIBLE);
                Intent intent = new Intent(PhoneDetailActivity.this, SelectActivity.class);
                intent.putExtra("from", "2");
                intent.putExtra("number", number);
                startActivity(intent);

            }
        });
    }

    private void initWriteFailedDialog() {
        writeFailedDialog = new Dialog(PhoneDetailActivity.this);
        writeFailedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        writeFailedDialog.setContentView(R.layout.dialog_write_failed);
        writeFailedDialog.setCanceledOnTouchOutside(false);
        writeFailedDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        Window window = writeFailedDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0); //没有边距
        layoutParams.dimAmount = 0.7f;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //背景透明
        window.setAttributes(layoutParams);
        TextView agree = (TextView) writeFailedDialog.findViewById(R.id.agree);
        TextView refuse = (TextView) writeFailedDialog.findViewById(R.id.refuse);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeFailedDialog.dismiss();
                AppManager.getAppManager().finishActivity();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeFailedDialog.dismiss();
                writeCard();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    strIccid = msg.obj.toString();
                    if (from.equals("1"))//重写
                        getPreImsiAgain();
                    else
                        getPreImsi();
                    break;
                case 1:
                    Toast.makeText(PhoneDetailActivity.this, "读取失败", Toast.LENGTH_SHORT).show();

                    break;
            }
//            if (msg.what == 0) {
//                strIccid = msg.obj.toString();
//
//
//                getPreImsi();
//
//                Log.d("zzz", msg.obj.toString());
//            }
        }
    };

    private void getPreImsiAgain() {
        HttpPost<PostImsi> httpPost = new HttpPost<>();
        PostImsi postImsi = new PostImsi();
        postImsi.setSession_token(Util.getLocalAdmin(PhoneDetailActivity.this)[0]);
        postImsi.setNumber(number);
        postImsi.setIccid(strIccid);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postImsi);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postImsi) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new CardHttp().getImsiAgain(new Subscriber<HttpRequest<RequestImsi>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestImsi> requestImsiHttpRequest) {
                dialog.dismiss();
                if (requestImsiHttpRequest.getCode() == BaseCom.NORMAL) {
                    tvIccid.setText(strIccid);
                    tvGetIMSI.setVisibility(View.GONE);
                    tvIccid.setVisibility(View.VISIBLE);
                    getImsiSuccess = 1;
                    requestImsi = requestImsiHttpRequest.getData();
                    tvNext.setVisibility(View.GONE);
                    tvNext0.setVisibility(View.VISIBLE);
                } else if (requestImsiHttpRequest.getCode() == BaseCom.LOSELOG | requestImsiHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(PhoneDetailActivity.this, LoginActivity.class);
                else {
                    strIccid = "";
                    getImsiSuccess = 0;
                    tvNext.setVisibility(View.GONE);
                    tvNext0.setVisibility(View.VISIBLE);
//                    tvIccid.setVisibility(View.GONE);
//                    tvGetIMSI.setVisibility(View.VISIBLE);
//                    tvNext.setVisibility(View.VISIBLE);
//                    tvNext0.setVisibility(View.GONE);
                }

            }
        }, httpPost);
    }


    private void getPreImsi() {
        HttpPost<PostImsi> httpPost = new HttpPost<>();
        PostImsi postImsi = new PostImsi();
        postImsi.setSession_token(Util.getLocalAdmin(PhoneDetailActivity.this)[0]);
        postImsi.setNumber(number);
        postImsi.setIccid(strIccid);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postImsi);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postImsi) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new CardHttp().getImsi(new Subscriber<HttpRequest<RequestImsi>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestImsi> requestImsiHttpRequest) {
                dialog.dismiss();
                if (requestImsiHttpRequest.getCode() == BaseCom.NORMAL) {
                    tvIccid.setText(strIccid);
                    tvGetIMSI.setVisibility(View.GONE);
                    tvIccid.setVisibility(View.VISIBLE);
                    getImsiSuccess = 1;
                    requestImsi = requestImsiHttpRequest.getData();
                    tvNext.setVisibility(View.GONE);
                    tvNext0.setVisibility(View.VISIBLE);
                } else if (requestImsiHttpRequest.getCode() == BaseCom.LOSELOG | requestImsiHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(PhoneDetailActivity.this, LoginActivity.class);
                else {
                    strIccid = "";
                    getImsiSuccess = 0;
                    tvNext.setVisibility(View.GONE);
                    tvNext0.setVisibility(View.VISIBLE);
//                    tvIccid.setVisibility(View.GONE);
//                    tvGetIMSI.setVisibility(View.VISIBLE);
//                    tvNext.setVisibility(View.VISIBLE);
//                    tvNext0.setVisibility(View.GONE);
                }

            }
        }, httpPost);
//        new CardHttp().getImsi(CardRequest.imsi(PhoneDetailActivity.this, dialog, new SuccessValue<RequestImsi>() {
//            @Override
//            public void OnSuccess(RequestImsi value) {
//                                            /*写入imsi*/
//                requestImsi = value;
//                tvNext.setVisibility(View.GONE);
//                tvNext0.setVisibility(View.VISIBLE);
//            }
//        }), httpPost);

    }


    @OnClick({R.id.imgBack, R.id.tvGetIMSI, R.id.ll_package, R.id.ll_activity, R.id.tvNext, R.id.tvNext0, R.id.tvNext1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tvNext1:
                Intent intent = new Intent(PhoneDetailActivity.this, SelectActivity.class);
                intent.putExtra("from", "2");
                intent.putExtra("number", number);
                startActivity(intent);
                break;
            case R.id.tvGetIMSI:
                if (!Util.isFastDoubleClick()) {
                    Log.d("bbb", "0000000");

                    mac = getAddressmac();
                    if (btAdapt.isEnabled()) {
                        if (mac[1].equals("")) {
                            /*蓝牙地址未存本地,跳转选择蓝牙设备界面*/
                            Intent serverIntent2 = new Intent(PhoneDetailActivity.this, BlueToothActivity.class);
                            startActivityForResult(serverIntent2, 12);
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneDetailActivity.this);
                            builder.setMessage("是否读取设备" + mac[0]);
                            builder.setTitle("提示");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    if (mac[0].startsWith("SR")) {

                                        final byte[] cardNum = new byte[20];
                                        mSRBlueReaderHelper = new SRBluetoothCardReader(new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                super.handleMessage(msg);
                                                Log.d("vvv", msg.what + "");
                                                switch (msg.what) {
                                                    case READ_CARD_SUCCESS:
                                                        break;
                                                }
                                            }
                                        }, PhoneDetailActivity.this, "FE870B0163113409C134283661490AEF");

                                        if (mSRBlueReaderHelper.registerBlueCard(mac[1])) {
                                            dialog.getTvTitle().setText("正在读取设备");
                                            dialog.show();
                                            new Thread() {
                                                public void run() {
                                                    //蓝牙读身份证
                                                    int result = mSRBlueReaderHelper.readSimICCID(cardNum);
                                                    String string2 = "";
                                                    if (result >= 0) {
                                                        string2 = new String(cardNum);
                                                    } else {
                                                        string2 = String.valueOf(result);
                                                    }
                                                    if (string2.length() < 10) {
                                                        dialog.dismiss();
                                                        handler.sendEmptyMessage(1);
                                                    } else {
                                                        Message message = new Message();
                                                        message.obj = string2;
                                                        message.what = 0;
                                                        handler.sendMessage(message);
                                                    }
                                                    mSRBlueReaderHelper.unRegisterBlueCard();

                                                }
                                            }.start();
                                        } else
                                            Toast.makeText(PhoneDetailActivity.this, "连接失败", Toast.LENGTH_SHORT).show();


                                    } else {
                                        try {
                                            if (receive == 0) {
                                                dialog.getTvTitle().setText("正在连接设备!");
                                                dialog.show();
                                                bleService.connectDevice(mac[1], PhoneDetailActivity.this);
                                            } else {
                                                String atr = bleService.resetDevice();
                                                Log.d("aaa", String.valueOf(atr));
                                                String apdu1 = "A0A40000023F00";
                                                String apdu2 = "A0A40000022FE2";
                                                String apdu3 = "A0B000000A";
                                                String resp1 = bleService.transmitDataSync(apdu1);
                                                String resp2 = bleService.transmitDataSync(apdu2);
                                                String resp3 = bleService.transmitDataSync(apdu3).substring(0, 20);

                                                strIccid = Util.swap(resp3);

                                                tvIccid.setText(strIccid);
                                                tvGetIMSI.setVisibility(View.GONE);
                                                tvIccid.setVisibility(View.VISIBLE);
                                                if (from.equals("1"))//重写
                                                    getPreImsiAgain();
                                                else
                                                    getPreImsi();

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(PhoneDetailActivity.this, "Exception:" + e.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                            builder.setNegativeButton("其他设备", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent serverIntent2 = new Intent(PhoneDetailActivity.this, BlueToothActivity.class);
                                    startActivityForResult(serverIntent2, 12);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                }
                            });
                            builder.create().show();

                        }
                    } else {
                        /*开启蓝牙*/
                        Intent enableIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

                    }
                }

                break;
            case R.id.ll_package:
                if (!Util.isFastDoubleClick()) {
                    Intent intent0 = new Intent(PhoneDetailActivity.this, PackageSelectDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("preNumber", requestPreNumberDetails);
                    intent0.putExtra("flag", "3");
                    intent0.putExtras(bundle);
                    startActivityForResult(intent0, 0);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
                break;
            case R.id.ll_activity:
                if (!Util.isFastDoubleClick()) {
                    if (mPackage == null) {
                        Util.createToast(PhoneDetailActivity.this, "请选择套餐!");
                    } else {
                        Intent intent1 = new Intent(PhoneDetailActivity.this, ActivitySelectDetailActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("package", mPackage);
                        intent1.putExtra("flag", "1");
                        intent1.putExtras(bundle1);
                        startActivityForResult(intent1, 1);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                }
                break;
            case R.id.tvNext:
                if (mPackage == null || mPromotion == null)
                    Toast.makeText(this, "请选择套餐和活动包", Toast.LENGTH_SHORT).show();
                else {
                    checkBuyDialog.show();

                }
                break;
            case R.id.tvNext0:

                if (!Util.isFastDoubleClick()) {
                    switch (getImsiSuccess) {
                        case 0:
                            Toast.makeText(PhoneDetailActivity.this, "获取Imsi失败，无法进行写卡", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            dialog.getTvTitle().setText("正在写卡");
                            dialog.show();
                            writeCard();
                            break;
                    }

                }
                break;
        }
    }


    private void writeCard() {
        mac = getAddressmac();
        if (mac[0].startsWith("SR")) {
            if (mSRBlueReaderHelper.registerBlueCard(mac[1])) {
                if (mSRBlueReaderHelper.writeSimCard(requestImsi.getImsi(), requestImsi.getSmscent())) {
                    postResult(0);
                } else {
                    postResult(1);
                }
                mSRBlueReaderHelper.unRegisterBlueCard();
            }

        } else {
            if (btAdapt.isEnabled()) {
                if (mac[1].equals("")) {
                    /*蓝牙地址未存本地,跳转选择蓝牙设备界面*/
                    Intent serverIntent2 = new Intent(PhoneDetailActivity.this, BlueToothActivity.class);
                    startActivityForResult(serverIntent2, 12);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                } else {
                    try {
                        if (receive == 0) {
                            dialog.getTvTitle().setText("正在连接设备!");
                            dialog.show();
                            bleService.connectDevice(mac[1], PhoneDetailActivity.this);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneDetailActivity.this);
                            builder.setMessage("是否写入设备" + mac[0]);
                            builder.setTitle("提示");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    Util.createToast(PhoneDetailActivity.this, "正在写入数据!");
                                    /*根据iccid获取imsi*/
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
                                            String resp01 = bleService.transmitDataSync(apdu01);
                                            String resp02 = bleService.transmitDataSync(apdu02);
                                            String resp03 = bleService.transmitDataSync(apdu03);
                                            String resp04 = bleService.transmitDataSync(apdu04);
                                            String resp05 = bleService.transmitDataSync(apdu05);
                                            if (resp05.equals("9000")) {
                                                Util.createToast(PhoneDetailActivity.this, "写卡成功");
                                                postResult(0);

                                            } else {
                                                Toast.makeText(PhoneDetailActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                        .show();
                                                postResult(1);
                                            }

                                        } else {
                                            Toast.makeText(PhoneDetailActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                    .show();
                                            postResult(1);
                                        }
                                    } catch (PlkException e) {
                                        e.printStackTrace();
                                        Toast.makeText(PhoneDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                .show();
                                    }


                                }
                            });
                            builder.setNegativeButton("其他设备", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    Intent serverIntent2 = new Intent(PhoneDetailActivity.this, BlueToothActivity.class);
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
                        Toast.makeText(PhoneDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG)
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


    }

    private void postResult(final int flag) {
        HttpPost<PostResult> httpPost = new HttpPost<>();
        PostResult postImsi = new PostResult();
        postImsi.setSession_token(Util.getLocalAdmin(PhoneDetailActivity.this)[0]);
        postImsi.setOrderNo(orderNo);
        postImsi.setResult(flag + "");
        postImsi.setImsi(requestImsi.getImsi());
        postImsi.setIccid(strIccid);
        postImsi.setNumber(number);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postImsi);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postImsi) + BaseCom.APP_PWD));
        new CardHttp().postResult(new Subscriber<HttpRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest httpRequest) {
                dialog.dismiss();
                if (httpRequest.getCode() == BaseCom.NORMAL || httpRequest.getCode() == BaseCom.NORMAL0) {
                    switch (flag) {
                        case 0:
                            writeSuccessDialog.show();
                            break;
                        case 1:
                            writeFailedDialog.show();
                            break;
                    }
                } else if (httpRequest.getCode() == BaseCom.LOSELOG | httpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(PhoneDetailActivity.this, LoginActivity.class);
                else
                    Toast.makeText(PhoneDetailActivity.this, httpRequest.getMes(), Toast.LENGTH_SHORT).show();


            }
        }, httpPost);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 0:
                    mPackage = requestPreNumberDetails.getPackages()[data.getIntExtra("select", 0)];
                    tvPackage.setText(mPackage.getName());
                    mPromotion = null;
                    tvActivity.setText("请选择");
                    break;
                case 1:
                    mPromotion = (Promotion) data.getExtras().getSerializable("select");
                    tvActivity.setText(mPromotion.getName());
//                    tvNext.setVisibility(View.VISIBLE);
                    break;
                case 12:
                    String address = data.getStringExtra("device_address");
                    String name = data.getStringExtra("device_name");
                    saveAddressmac(name, address);
                    mac = getAddressmac();
                    if (mac[0].startsWith("SR")) {
//                        final byte[] cardNum = new byte[20];
//                        if (mSRBlueReaderHelper != null)
//                            mSRBlueReaderHelper = new SRBluetoothCardReader(new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    super.handleMessage(msg);
//                                    Log.d("vvv", msg.what + "");
//                                    switch (msg.what) {
//                                        case READ_CARD_SUCCESS:
//                                            break;
//                                    }
//                                }
//                            }, PhoneDetailActivity.this, "FE870B0163113409C134283661490AEF");
//                        if (mSRBlueReaderHelper.registerBlueCard(mac[1]))
//                            Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            bleService.connectDevice(address, PhoneDetailActivity.this);
                        } catch (PlkException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
            }
        } else {
            if (requestCode == REQUEST_ENABLE_BT) {
                if (resultCode == RESULT_OK) {
                    receive = 0;
                    mac = getAddressmac();
                    Log.d("ccc", mac[0]);
                    Log.d("ccc", mac[1]);
                    if (mac[1].equals("")) {
                        /*开启蓝牙成功,若本地未存蓝牙地址,跳转设备选择界面*/
                        Intent serverIntent2 = new Intent(PhoneDetailActivity.this, BlueToothActivity.class);
                        startActivityForResult(serverIntent2, 12);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    } else {
                        if (mac[0].startsWith("SR")) {
//                            final byte[] cardNum = new byte[20];
//                            mSRBlueReaderHelper = new SRBluetoothCardReader(new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    super.handleMessage(msg);
//                                    Log.d("vvv", msg.what + "");
//                                    switch (msg.what) {
//                                        case READ_CARD_SUCCESS:
//                                            break;
//                                    }
//                                }
//                            }, PhoneDetailActivity.this, "FE870B0163113409C134283661490AEF");
//                            if (mSRBlueReaderHelper.registerBlueCard(mac[1]))
//                                Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                bleService.connectDevice(mac[1], PhoneDetailActivity.this);
                            } catch (PlkException e) {
                                e.printStackTrace();
                            }
                        }


//                        try {
//                            bleService.connectDevice(mac[1], PhoneDetailActivity.this);
//                        } catch (PlkException e) {
//                            e.printStackTrace();
//                        }
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    receive = 0;
                    AppManager.getAppManager().finishActivity();
                }
            }
//            dialog.dismiss();
        }
    }

    @Override
    public void onConnectSuccess() {
        dialog.dismiss();
        Log.d("aaa", "success");
        receive = 1;
        Util.createToast(PhoneDetailActivity.this, "连接设备成功");
    }

    @Override
    public void onConnectFailed(String s) {
        receive = 0;
        dialog.dismiss();
        Util.createToast(PhoneDetailActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }

    @Override
    public void onConnectLost() {
        receive = 0;
        dialog.dismiss();
        Util.createToast(PhoneDetailActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }
}
