package com.test.tworldapplication.activity.card;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
import com.test.tworldapplication.entity.LiangRecords;
import com.test.tworldapplication.entity.PostImsi;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

public class HuajiWriteActivity extends BaseActivity implements PlkBleConnectCallback {
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvOperate)
    TextView tvOperate;
    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvIccid)
    TextView tvIccid;
    @BindView(R.id.tvIccidText)
    TextView tvIccidText;
    AgentsLiangNumber agentsLiangNumber;
    private BluetoothAdapter btAdapt;
    private PlkBleService bleService;
    //    Area area;
    String[] mac;
    String strIccid = "";
    private int receive = 0;
    RequestImsi requestImsi = null;
    private static final int REQUEST_ENABLE_BT = 2;
    int hasWrite = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huaji_write);
        ButterKnife.bind(this);
        setBackGroundTitle("写卡", true);
        agentsLiangNumber = (AgentsLiangNumber) getIntent().getSerializableExtra("data");
//        area = BaseUtils.getArea(HuajiWriteActivity.this);
        initView();
        dialog.getTvTitle().setText("正在获取信息");
        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
        bleService = new PlkBleService(HuajiWriteActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleService.destroy();
    }

    @OnClick({R.id.tvSubmit, R.id.tvIccid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                if (!Util.isFastDoubleClick() && hasWrite == 1) {
                    LiangRecords liangRecords = new LiangRecords();
                    liangRecords.setSession_token(Util.getLocalAdmin(HuajiWriteActivity.this)[0]);
                    liangRecords.setNumber(agentsLiangNumber.getNumber());
                    liangRecords.setIccid(strIccid);
                    HttpPost<LiangRecords> httpPost1 = new HttpPost<>();
                    httpPost1.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost1.setParameter(liangRecords);
                    httpPost1.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(liangRecords) + BaseCom.APP_PWD));

                    new CardHttp().liangRecords(httpPost1, new Subscriber<HttpRequest<LiangRecords>>() {
                        @Override
                        public void onCompleted() {
                            Intent intent = new Intent(HuajiWriteActivity.this, HuajiSelectActivity.class);
                            intent.putExtra("from", "2");
                            intent.putExtra("agentsLiangNumber", agentsLiangNumber);
                            intent.putExtra("requestImsi", requestImsi);
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpRequest<LiangRecords> liangRecordsHttpRequest) {

                        }
                    });

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
                                    Intent serverIntent2 = new Intent(HuajiWriteActivity.this, BlueToothActivity.class);
                                    startActivityForResult(serverIntent2, 12);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                } else {
                                    try {
                                        if (receive == 0) {
                                            dialog.getTvTitle().setText("正在连接设备!");
                                            dialog.show();
                                            bleService.connectDevice(mac[1], HuajiWriteActivity.this);
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HuajiWriteActivity.this);
                                            builder.setMessage("是否写入设备" + mac[0]);
                                            builder.setTitle("提示");
                                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface alertDialog, int which) {
                                                    alertDialog.dismiss();
                                                    Util.createToast(HuajiWriteActivity.this, "正在写入数据!");
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
                                                        postImsi.setSession_token(Util.getLocalAdmin(HuajiWriteActivity.this)[0]);
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
                                                                Toast.makeText(HuajiWriteActivity.this, requestImsiHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
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
                                                                                tvSubmit.setVisibility(View.VISIBLE);
                                                                                tvIccidText.setVisibility(View.VISIBLE);
                                                                                hasWrite = 1;
                                                                                Util.createToast(HuajiWriteActivity.this, "写卡成功,请点击下一步!");
                                                                            } else {
                                                                                Toast.makeText(HuajiWriteActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                                                        .show();
                                                                                tvSubmit.setVisibility(View.VISIBLE);


                                                                            }

                                                                        } else {
                                                                            Toast.makeText(HuajiWriteActivity.this, "写卡失败!", Toast.LENGTH_SHORT)
                                                                                    .show();
                                                                        }
                                                                    } catch (PlkException e) {
                                                                        e.printStackTrace();
                                                                        Toast.makeText(HuajiWriteActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                                                .show();
                                                                    }
                                                                } else if (requestImsiHttpRequest.getCode() == BaseCom.LOSELOG || requestImsiHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                                                                    Util.gotoActy(HuajiWriteActivity.this, LoginActivity.class);
                                                                }
                                                            }
                                                        });
//
                                                    } catch (PlkException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                        Toast.makeText(HuajiWriteActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                                                                .show();
                                                    }

                                                }
                                            });
                                            builder.setNegativeButton("其他设备", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface alertDialog, int which) {
                                                    alertDialog.dismiss();
                                                    Intent serverIntent2 = new Intent(HuajiWriteActivity.this, BlueToothActivity.class);
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
                                        Toast.makeText(HuajiWriteActivity.this, e.getMessage(), Toast.LENGTH_LONG)
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
                        bleService.connectDevice(address, HuajiWriteActivity.this);
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
                        Intent serverIntent2 = new Intent(HuajiWriteActivity.this, BlueToothActivity.class);
                        startActivityForResult(serverIntent2, 12);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    } else {
                        try {
                            bleService.connectDevice(mac[1], HuajiWriteActivity.this);
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

    private void initView() {
        tvNumber.setText(agentsLiangNumber.getNumber());
//        String province = "";
//        String city = "";
//        List<Province> provinces = area.getList();
//        for (int i = 0; i < provinces.size(); i++) {
//            if (provinces.get(i).getP_id().equals(agentsLiangNumber.getProvinceCode())) {
//                province = provinces.get(i).getP_name();
//                List<City> cities = provinces.get(i).getP_list();
//                for (int j = 0; j < cities.size(); j++) {
//                    if (cities.get(j).getC_id().equals(agentsLiangNumber.getCityCode())) {
//                        city = cities.get(j).getC_name();
//                        break;
//                    }
//                }
//                break;
//            }
//        }
        tvAddress.setText(agentsLiangNumber.getProvinceName() + agentsLiangNumber.getCityName());
        tvOperate.setText(agentsLiangNumber.getUserName());
        tvPackage.setText(agentsLiangNumber.getSelectPackage().getName());
        tvActivity.setText(agentsLiangNumber.getSelectPromotion().getName());
    }

    @Override
    public void onConnectSuccess() {
        dialog.dismiss();
        Log.d("aaa", "success");
        receive = 1;
        Util.createToast(HuajiWriteActivity.this, "连接设备成功,请再次点击写卡按钮");
    }

    @Override
    public void onConnectFailed(String s) {
        Log.d("aaa", "fail");
        receive = 0;
        dialog.dismiss();
        Util.createToast(HuajiWriteActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }

    @Override
    public void onConnectLost() {
        Log.d("aaa", "lost");
        receive = 0;
        dialog.dismiss();
        Util.createToast(HuajiWriteActivity.this, "设备连接失败,请重新连接!");
        clearAddressmac();
    }
}
