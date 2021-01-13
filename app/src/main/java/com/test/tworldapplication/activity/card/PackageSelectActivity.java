package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.order.QrCodeActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PackageSelectActivity extends BaseActivity {

    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvInternet)
    TextView tvInternet;
    @BindView(R.id.ll_package)
    LinearLayout llPackage;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvNext0)
    TextView tvNext0;
    @BindView(R.id.activity_package_select)
    LinearLayout activityPackageSelect;
    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    Package[] packages;
    Package mPackage = null;
    Promotion mPromotion = null;
    RequestCheck requestCheck = null;
    /*RequestCheck包含package[],将RequestCheck传递到套餐选择页面,选择package,把package传递到活动选择页面,查询promotion【】,选择promotion,与requestCheck传向信息采集页面*/

    String face = "";
    String from = "";
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_select);
        ButterKnife.bind(this);
        setBackGroundTitle("套餐选择", true);

        Bundle bundle = getIntent().getExtras();
        requestCheck = (RequestCheck) bundle.getSerializable("check");
        packages = requestCheck.getPackages();

        face = getIntent().getStringExtra("face");
        from = getIntent().getStringExtra("from");
        type = getIntent().getStringExtra("type");

        if (face.equals("3")) {
            tvNext.setVisibility(View.GONE);
            tvNext0.setVisibility(View.VISIBLE);
        } else {
            tvNext.setVisibility(View.VISIBLE);
            tvNext0.setVisibility(View.GONE);
        }

        init();
    }

    public void init() {
        tvNumber.setText(requestCheck.getNumber());
        tvAddress.setText(requestCheck.getCityName());
        tvState.setText(requestCheck.getNumberStatus());
        tvInternet.setText(requestCheck.getOperatorName());
        etMoney.setText(requestCheck.getPrestore());
    }

    @OnClick({R.id.ll_package, R.id.ll_activity, R.id.tvNext, R.id.tvNext0})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_package:
                if (!Util.isFastDoubleClick()) {
                    Intent intent = new Intent(PackageSelectActivity.this, PackageSelectDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("check", requestCheck);
                    intent.putExtra("flag", "0");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
                break;
            case R.id.ll_activity:
                if (!Util.isFastDoubleClick()) {
                    if (mPackage == null) {
                        Util.createToast(this, "请选择套餐!");
                    } else {
                        Intent intent0 = new Intent(PackageSelectActivity.this, ActivitySelectDetailActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("package", mPackage);
                        intent0.putExtra("flag", "0");
                        intent0.putExtras(bundle1);
                        startActivityForResult(intent0, 1);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                }
                break;
            case R.id.tvNext:
                if (!Util.isFastDoubleClick()) {
                    if (mPackage == null || mPromotion == null) {
                        Util.createToast(this, "请将信息填写完整!");
                    } else {

//                        HttpPost<PostOpenPower> httpPost0=new HttpPost<>();
//                        PostOpenPower postOpenPower=new PostOpenPower();
//                        postOpenPower.setSession_token(Util.getLocalAdmin(PackageSelectActivity.this)[0]);
//                        httpPost0.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
//                        httpPost0.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpenPower) + BaseCom.APP_PWD));
//                        httpPost0.setParameter(postOpenPower);
//                        Log.d("aaa", gson.toJson(httpPost0));
//                        new AdminHttp().getOpenPower( AdminRequest.getOpenPower( PackageSelectActivity.this, new SuccessValue<RequestOpenPower>() {
//                            @Override
//                            public void OnSuccess(RequestOpenPower value) {
//                                SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putInt( "pattern",value.getPattern() );
//                                editor.putInt( "modes",value.getModes());
//                                editor.putInt( "readModes",value.getReadModes());
//                                editor.commit();

//                                final Intent intent1 = new Intent(PackageSelectActivity.this, SelectActivity.class);

                        final Intent intent1 = new Intent(PackageSelectActivity.this, MessageCollectionNewActivity2.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("requestCheck", requestCheck);
                        bundle1.putSerializable("mPackage", mPackage);
                        bundle1.putSerializable("mPromotion", mPromotion);
                        intent1.putExtras(bundle1);
                        intent1.putExtra("from", from);
                        intent1.putExtra("face", face);
                        intent1.putExtra("type", type);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                            }
//                        } ),httpPost0 );

//                        final Intent intent1 = new Intent(this, SelectActivity.class);
//                        Bundle bundle1 = new Bundle();
//                        bundle1.putSerializable("requestCheck", requestCheck);
//                        bundle1.putSerializable("mPackage", mPackage);
//                        bundle1.putSerializable("mPromotion", mPromotion);
//                        intent1.putExtras(bundle1);
//                        intent1.putExtra("from", "0");
//                        startActivity(intent1);
//                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                    }
                }
                break;
            case R.id.tvNext0:
                if (!Util.isFastDoubleClick()) {
                    if (mPackage == null || mPromotion == null) {
                        Util.createToast(this, "请将信息填写完整!");
                    } else {
                        final Intent intent1 = new Intent(PackageSelectActivity.this, QrCodeActivity.class);
                        Bundle bundle1 = new Bundle();
//                        bundle1.putSerializable("requestCheck", requestCheck);
//                        bundle1.putSerializable("mPackage", mPackage);
//                        bundle1.putSerializable("mPromotion", mPromotion);
//                        intent1.putExtras(bundle1);
                        intent1.putExtra("tel", requestCheck.getNumber() + "");
                        intent1.putExtra("packagesId", mPackage.getId() + "");
                        intent1.putExtra("promotionId", mPromotion.getId() + "");
                        startActivity(intent1);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
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
                    Log.d("aaa", data.getIntExtra("select", 50) + "");
                    mPackage = packages[data.getIntExtra("select", 0)];
                    tvPackage.setText(mPackage.getName());
                    mPromotion = null;
                    tvActivity.setText("请选择");
                    break;
                case 1:
                    mPromotion = (Promotion) data.getExtras().getSerializable("select");
                    tvActivity.setText(mPromotion.getName());
                    break;
            }
        }
    }
}
