package com.test.tworldapplication.activity.card;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.PostMode;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestMode;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Constants;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity {
    Package mPackage = null;
    Promotion mPromotion = null;
    RequestCheck requestCheck = null;
    String from = "";//0成卡1白卡2写卡激活
    int flag = 0;
    String code = "";
    String phone = "";
    RequestImsi requestImsi = null;
    String iccid = "";
    //    int flag = 0;
    String name, cardId, address, remark;
    String money;

    @BindView(R.id.normal)
    RelativeLayout rlNormal;
    @BindView(R.id.face_reveal)
    RelativeLayout rlFace;
    String number;
    String preStore;
    String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);

        HttpPost<PostOpenPower> httpPost0 = new HttpPost<>();
        PostOpenPower postOpenPower = new PostOpenPower();
        postOpenPower.setSession_token(Util.getLocalAdmin(SelectActivity.this)[0]);
        httpPost0.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost0.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpenPower) + BaseCom.APP_PWD));
        httpPost0.setParameter(postOpenPower);
        Log.d("aaa", gson.toJson(httpPost0));
        new AdminHttp().getOpenPower(AdminRequest.getOpenPower(SelectActivity.this, new SuccessValue<RequestOpenPower>() {
            @Override
            public void OnSuccess(RequestOpenPower value) {
                SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("pattern", value.getPattern());
                editor.putInt("modes", value.getModes());
                editor.putInt("readModes", value.getReadModes());
                editor.putInt(Constants.SHOOTMODES, value.getShootModes());
                editor.putInt(Constants.SHOOTSWITCH, value.getShootSwitch());
                editor.commit();

                if (value.getPattern() == 1) {
                    rlNormal.setVisibility(View.VISIBLE);
                    rlFace.setVisibility(View.GONE);
                } else if (value.getPattern() == 2) {
                    rlNormal.setVisibility(View.GONE);
                    rlFace.setVisibility(View.VISIBLE);
                } else if (value.getPattern() == 3) {
                    rlNormal.setVisibility(View.VISIBLE);
                    rlFace.setVisibility(View.VISIBLE);
                }

                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        }), httpPost0);


        setBackGroundTitle("读取信息", true);
        Util.createToast(SelectActivity.this, "请选择读取信息的方式!");


        from = getIntent().getStringExtra("from");
        switch (from) {
            case "0":
//                mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
//                mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
//                requestCheck = (RequestCheck) getIntent().getExtras().getSerializable("requestCheck");
                break;
            case "1":
                phone = getIntent().getStringExtra("phone");
                mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
                mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
                requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
                iccid = getIntent().getStringExtra("iccid");
                break;
            case "2":
                number = getIntent().getStringExtra("number");
                preStore = getIntent().getStringExtra("preStore");
                detail = getIntent().getStringExtra("detail");
                Log.d("nnn", "getNumber:" + number);
                break;
        }
    }


    @OnClick({R.id.normal, R.id.face_reveal})
    public void onClick(View view) {


//        final Intent intent0 = new Intent(this, MessageCollection0Activity.class);
//        Bundle bundle0 = new Bundle();

        switch (from) {
            case "0":
                /*chengka*/
//                bundle1.putSerializable("requestCheck", requestCheck);
//                bundle1.putSerializable("mPackage", mPackage);
//                bundle1.putSerializable("mPromotion", mPromotion);
                final Intent intent1 = new Intent(this, RenewCardActivity.class);
                Bundle bundle1 = new Bundle();
                intent1.putExtras(bundle1);
                intent1.putExtra("from", "0");
                switch (view.getId()) {
                    case R.id.normal:
                        if (!Util.isFastDoubleClick()) {

                            SharedPreferences sharedPreferences0 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            int modes = sharedPreferences0.getInt("modes", -1);
                            if (modes == 1) {
                                /*can*/
                                intent1.putExtra("type", "1");
                                intent1.putExtra("face", "0");
                                intent1.putExtra("from", "0");
                                intent1.putExtra("post", 0);
                                startActivity(intent1);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            } else if (modes == 2) {
                                intent1.putExtra("type", "0");
                                intent1.putExtra("face", "0");
                                intent1.putExtra("from", "0");
                                intent1.putExtra("post", 0);

                                startActivity(intent1);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            } else if (modes == 3) {
                                /*can*/
                                intent1.putExtra("type", "1");
                                intent1.putExtra("face", "0");
                                intent1.putExtra("from", "0");
                                intent1.putExtra("post", 0);

                                startActivity(intent1);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            }
                        }

                        break;

                    case R.id.face_reveal:
                        if (!Util.isFastDoubleClick()) {

                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            int modes0 = sharedPreferences.getInt("modes", 1);
                            if (modes0 == 1) {
                                /*can*/
                                intent1.putExtra("type", "1");
                                intent1.putExtra("face", "1");
                                intent1.putExtra("from", "0");
                                intent1.putExtra("post", 1);

                                startActivity(intent1);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            } else if (modes0 == 2) {
                                intent1.putExtra("type", "0");
                                intent1.putExtra("face", "1");
                                intent1.putExtra("from", "0");
                                intent1.putExtra("post", 1);

                                startActivity(intent1);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                /*can*/
                                break;
                            } else if (modes0 == 3) {
                                /*can*/
                                intent1.putExtra("type", "1");
                                intent1.putExtra("face", "1");
                                intent1.putExtra("from", "0");
                                intent1.putExtra("post", 1);

                                startActivity(intent1);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            }
                        }
                        break;
                }
                break;
            case "1":
                /*baika*/
                final Intent intent0 = new Intent(this, MessageCollectionNewActivity2.class);
                Bundle bundle0 = new Bundle();
                intent0.putExtra("phone", phone);
                intent0.putExtra("iccid", iccid);
                intent0.putExtra("from", "1");
                bundle0.putSerializable("mPackage", mPackage);
                bundle0.putSerializable("mPromotion", mPromotion);
                bundle0.putSerializable("requestImsi", requestImsi);
                intent0.putExtras(bundle0);

                break;
            case "2":
                /*jihuo*/
                Intent intent2 = new Intent(this, MessageCollectionNewActivity2.class);
                intent2.putExtra("number", number);
                intent2.putExtra("preStore", preStore);
                intent2.putExtra("detail", detail);
                switch (view.getId()) {
                    case R.id.normal:
                        if (!Util.isFastDoubleClick()) {

                            SharedPreferences sharedPreferences0 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            int modes = sharedPreferences0.getInt("modes", -1);
                            if (modes == 1) {
                                /*can*/
                                intent2.putExtra("type", "1");
                                intent2.putExtra("face", "0");
                                intent2.putExtra("from", "5");

                                startActivity(intent2);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            } else if (modes == 2) {
                                intent2.putExtra("type", "0");
                                intent2.putExtra("face", "0");
                                intent2.putExtra("from", "5");

                                startActivity(intent2);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            } else if (modes == 3) {
                                /*can*/
                                intent2.putExtra("type", "1");
                                intent2.putExtra("face", "0");
                                intent2.putExtra("from", "5");

                                startActivity(intent2);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            }
                        }

                        break;

                    case R.id.face_reveal:
                        if (!Util.isFastDoubleClick()) {

                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            int modes0 = sharedPreferences.getInt("modes", 1);
                            if (modes0 == 1) {
                                /*can*/
                                intent2.putExtra("type", "1");
                                intent2.putExtra("face", "1");
                                intent2.putExtra("from", "5");



                                startActivity(intent2);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            } else if (modes0 == 2) {
                                intent2.putExtra("type", "0");
                                intent2.putExtra("face", "1");
                                intent2.putExtra("from", "5");

                                startActivity(intent2);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                /*can*/
                                break;
                            } else if (modes0 == 3) {
                                /*can*/
                                intent2.putExtra("type", "1");
                                intent2.putExtra("face", "1");
                                intent2.putExtra("from", "5");

                                startActivity(intent2);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                break;
                            }
                        }
                        break;
                }

                break;
        }


    }
}
