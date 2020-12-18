package com.test.tworldapplication.activity.card;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.plk.bluetoothlesdk.PlkBleConnectCallback;
import com.plk.bluetoothlesdk.PlkBleService;
import com.plk.bluetoothlesdk.PlkException;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.PostImsi;
import com.test.tworldapplication.entity.PostPreOpen;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class CardReadingActivity extends BaseActivity implements PlkBleConnectCallback {

    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvInternet)
    TextView tvInternet;
    @BindView(R.id.tvIccid)
    TextView tvIccid;
    @BindView(R.id.tvRead)
    TextView tvRead;
    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.ll_package)
    LinearLayout llPackage;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    String phone = "";
    RequestImsi requestImsi = null;
    Package mPackage;
    Promotion mPromotion = null;
    //    int flag = 0;
//    String name, cardId, address, remark;
    String money;
    //    Bitmap photoOne, photoTwo;
    String strIccid = "";
    private BluetoothAdapter btAdapt;
    private static final int REQUEST_ENABLE_BT = 2;
    String[] mac;
    private PlkBleService bleService;
    private int receive = 0;
    private String from = "";

    /*白卡独有的页面 获取参数,手机号,两张bitmap,身份证信息。。,获取imsi,获取成功,显示选择套餐,活动包*/
    /*imsi中自带Package[],选择活动包成功之后,显示下一步按钮*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reading);
        ButterKnife.bind(this);
        setBackGroundTitle("读卡与套餐选择", true);
        phone = getIntent().getStringExtra("phone");
        from = getIntent().getStringExtra("from");

        tvPhone.setText("号码: " + phone);
        dialog.getTvTitle().setText("正在获取信息");
        tvNext.setVisibility(View.GONE);
        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
        bleService = new PlkBleService(CardReadingActivity.this);
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
    protected void onDestroy() {
        super.onDestroy();
        bleService.destroy();
    }

    @OnClick(R.id.tvRead)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {

            mac = getAddressmac();
            Log.d("ccc", mac[0]);
            Log.d("ccc", mac[1]);
            if (btAdapt.isEnabled()) {
                if (mac[1].equals("")) {
                /*蓝牙地址未存本地,跳转选择蓝牙设备界面*/
//                    Intent serverIntent2 = new Intent(CardReadingActivity.this, BlueToothReadActivity.class);
                    Intent serverIntent2 = new Intent(CardReadingActivity.this, BlueToothActivity.class);
                    startActivityForResult(serverIntent2, 12);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                } else {
                    try {
                        if (receive == 0) {
                            dialog.getTvTitle().setText("正在连接设备!");
                            dialog.show();
                            bleService.connectDevice(mac[1], CardReadingActivity.this);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CardReadingActivity.this);
                            builder.setMessage("是否写入设备" + mac[0]);
                            builder.setTitle("提示");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    Util.createToast(CardReadingActivity.this, "正在写入数据!");
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

                                        Log.d("aaa", strIccid);
                                    /*根据iccid获取imsi*/
                                        HttpPost<PostImsi> httpPost = new HttpPost<>();
                                        PostImsi postImsi = new PostImsi();
                                        postImsi.setSession_token(Util.getLocalAdmin(CardReadingActivity.this)[0]);
                                        postImsi.setNumber(phone);
                                        postImsi.setIccid(strIccid);
                                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                                        httpPost.setParameter(postImsi);
                                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postImsi) + BaseCom.APP_PWD));
                                        Log.d("aaa", gson.toJson(httpPost));
                                        new CardHttp().imsi(CardRequest.imsi(CardReadingActivity.this, dialog, new SuccessValue<RequestImsi>() {
                                            @Override
                                            public void OnSuccess(RequestImsi value) {
                                            /*写入imsi*/
                                                requestImsi = value;
                                                Log.d("aaa", requestImsi.getImsi());
                                                String v = "809" + value.getImsi();
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
                                                            Util.createToast(CardReadingActivity.this, "写卡成功,请选择套餐与活动包!");
                                                            etMoney.setText(String.valueOf(value.getPrestore()));
                                                            llContent.setVisibility(View.VISIBLE);
                                                            tvRead.setVisibility(View.GONE);
                                                        } else {
                                                            Toast.makeText(CardReadingActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }

                                                    } else {
                                                        Toast.makeText(CardReadingActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                } catch (PlkException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(CardReadingActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                            .show();
                                                }

                                            }
                                        }), httpPost);

                                    } catch (PlkException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                        Toast.makeText(CardReadingActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                .show();
                                    }

                                }
                            });
                            builder.setNegativeButton("其他设备", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
//                                    Intent serverIntent2 = new Intent(CardReadingActivity.this, BlueToothReadActivity.class);
                                    Intent serverIntent2 = new Intent(CardReadingActivity.this, BlueToothActivity.class);
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
                        Toast.makeText(CardReadingActivity.this, e.getMessage(), Toast.LENGTH_LONG)
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

    @OnClick({R.id.ll_package, R.id.ll_activity, R.id.tvNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_package:
                if (!Util.isFastDoubleClick()) {
                    Intent intent = new Intent(CardReadingActivity.this, PackageSelectDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("imsi", requestImsi);
                    intent.putExtra("flag", "1");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
                break;
            case R.id.ll_activity:
                if (!Util.isFastDoubleClick()) {
                    if (mPackage == null) {
                        Util.createToast(CardReadingActivity.this, "请选择活动包!");
                    } else {
                        Intent intent1 = new Intent(CardReadingActivity.this, ActivitySelectDetailActivity.class);
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
                if (!Util.isFastDoubleClick()) {
                    HttpPost<PostPreOpen> httpPost1 = new HttpPost<PostPreOpen>();
                    PostPreOpen postPreOpen = new PostPreOpen();
                    postPreOpen.setSession_token(Util.getLocalAdmin(CardReadingActivity.this)[0]);
                    postPreOpen.setIccid(strIccid);
                    postPreOpen.setNumber(phone);
                    httpPost1.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost1.setParameter(postPreOpen);
                    httpPost1.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPreOpen) + BaseCom.APP_PWD));
                    new CardHttp().preopen(CardRequest.preopen(CardReadingActivity.this, dialog, new SuccessNull() {
                        @Override
                        public void onSuccess() {

                            Intent intent2 = new Intent(CardReadingActivity.this, SelectActivity.class);
                            intent2.putExtra("phone", phone);
                            Bundle bundle2 = new Bundle();
                            bundle2.putSerializable("mPackage", mPackage);
                            bundle2.putSerializable("mPromotion", mPromotion);
                            bundle2.putSerializable("requestImsi", requestImsi);
                            intent2.putExtras(bundle2);
                            intent2.putExtra("from", "1");
                            intent2.putExtra("iccid", strIccid);
                            startActivity(intent2);
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

//                        gotoActy(AccountClosingActivity.class);
//                        finish();
                        }
                    }), httpPost1);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 0:
                    mPackage = requestImsi.getPackages()[data.getIntExtra("select", 0)];
                    tvPackage.setText(mPackage.getName());
                    break;
                case 1:
                    mPromotion = (Promotion) data.getExtras().getSerializable("select");
                    tvActivity.setText(mPromotion.getName());
                    tvNext.setVisibility(View.VISIBLE);
                    break;
                case 12:
                    String address = data.getStringExtra("device_address");
                    String name = data.getStringExtra("device_name");
                    saveAddressmac(name, address);
                    try {
                        bleService.connectDevice(address, CardReadingActivity.this);
                    } catch (PlkException e) {
                        e.printStackTrace();
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
//                        Intent serverIntent2 = new Intent(CardReadingActivity.this, BlueToothReadActivity.class);
                        Intent serverIntent2 = new Intent(CardReadingActivity.this, BlueToothActivity.class);
                        startActivityForResult(serverIntent2, 12);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    } else {
                        try {
                            bleService.connectDevice(mac[1], CardReadingActivity.this);
                        } catch (PlkException e) {
                            e.printStackTrace();
                        }
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
        Util.createToast(CardReadingActivity.this, "连接设备成功,请点击写卡按钮");
    }

    @Override
    public void onConnectFailed(String s) {
        Log.d("aaa", "fail");
        receive = 0;
        dialog.dismiss();
        Util.createToast(CardReadingActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }

    @Override
    public void onConnectLost() {
        Log.d("aaa", "lost");
        receive = 0;
        dialog.dismiss();
        Util.createToast(CardReadingActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }
}
