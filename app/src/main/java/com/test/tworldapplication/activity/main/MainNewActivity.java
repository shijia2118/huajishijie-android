package com.test.tworldapplication.activity.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.luck.picture.lib.rxbus2.RxUtils;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountMainFragment;
import com.test.tworldapplication.activity.card.CardMainFragment;
import com.test.tworldapplication.activity.home.HomeMainFragment;
import com.test.tworldapplication.activity.order.OrderMainNewFragment;
import com.test.tworldapplication.activity.order.QdsMainNewFragment;
import com.test.tworldapplication.activity.other.MessageMainActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.base.MyApplication;
import com.test.tworldapplication.base.OSSConfig;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.City;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.JudgeLocationResponse;
import com.test.tworldapplication.entity.MessageEvent;
import com.test.tworldapplication.entity.PostFunction;
import com.test.tworldapplication.entity.PostLocationEntity;
import com.test.tworldapplication.entity.PostLogout;
import com.test.tworldapplication.entity.PostNoticeList;
import com.test.tworldapplication.entity.Province;
import com.test.tworldapplication.entity.RequestNoticeList;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.FileLogUtils;
import com.test.tworldapplication.utils.LocationHelper;
import com.test.tworldapplication.utils.LogUtils;
import com.test.tworldapplication.utils.SPUtil;
import com.test.tworldapplication.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainNewActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager2 viewPager;
    @BindView(R.id.imgHome)
    ImageView imgHome;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.imgOrder)
    ImageView imgOrder;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.imgAccount)
    ImageView imgAccount;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.imgCard)
    ImageView imgCard;
    @BindView(R.id.tvCard)
    TextView tvCard;
    @BindView(R.id.llCard)
    LinearLayout llCard;
    @BindView(R.id.ll_main_home)
    LinearLayout llMainHome;
    @BindView(R.id.imgQds)
    ImageView imgQds;
    @BindView(R.id.tvQds)
    TextView tvQds;
    @BindView(R.id.llQds)
    LinearLayout llQds;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgMessage)
    ImageView imgMessage;
    @BindView(R.id.imgNew)
    ImageView imgNew;
    @BindView(R.id.imgAdmin)
    ImageView imgAdmin;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;


    Integer current = 0;
    List<Fragment> fragmentList = new ArrayList<>();
    PagerAdapter pagerAdapter;
    AlertDialog.Builder _dialogBuilderPermission;
    AlertDialog _dialogPermission;
    AlertDialog.Builder _dialogBuilderLocation;
    AlertDialog _dialogLocation;
    int avaliable = 0;

    @Override
    protected void onPause() {
        super.onPause();
        avaliable = 0;
        Log.d("ttt", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        avaliable = 1;
        Log.d("ttt", "onResume");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getFlag()) {
            case MessageEvent.CHECK_PERMISSION:
                current = new Integer(messageEvent.getMessage());
                if (current % 600 == 0) {
                    if (!Util.isOPen(MainNewActivity.this) || ContextCompat.checkSelfPermission(MainNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PERMISSION_GRANTED) {
                        Util.stopSub();
                        if (_dialogPermission == null || !_dialogPermission.isShowing()) {
                            _dialogPermission = _dialogBuilderPermission.show();
                        }

                    } else if (current % 1200 == 0) {
                        LocationHelper.getInstance().startLocation();
                    }
                }

                break;
            case MessageEvent.START_LOCATE:
                Log.d("fff000", current + "");
                if (current != 0 && current % 1200 < 1190) {
                    if (!Util.isOPen(MainNewActivity.this) || ContextCompat.checkSelfPermission(MainNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PERMISSION_GRANTED) {
                        Util.stopSub();
                        if (_dialogPermission == null || !_dialogPermission.isShowing()) {
                            _dialogPermission = _dialogBuilderPermission.show();
                        }
                    } else {
                        LocationHelper.getInstance().startLocation();
                    }

                }
                break;
            case MessageEvent.START_COUNT:
                Util.checkCount();
                break;
            case MessageEvent.STOP_COUNT:
                Util.stopSub();
                break;
            case MessageEvent.LOCATION_MESSAGE:
                if (avaliable == 1) {
                    if (!Util.isFastDoubleShow()) {
                        String content = messageEvent.getMessage();
                        if (!content.equals("null,null")) {
                            String provinceCode = "";
                            String cityCode = "";
                            String province = content.split(",")[0];
                            String city = content.split(",")[1];
                            Area area = BaseUtils.getArea(MainNewActivity.this);
                            List<Province> provinces = area.getList();
                            Province province1 = new Province(province);
                            if (provinces.contains(province1)) {
                                int index = provinces.indexOf(province1);
                                provinceCode = provinces.get(index).getP_id();
                                List<City> cities = provinces.get(index).getP_list();
                                City city1 = new City(city);
                                if (cities.contains(city1)) {
                                    int index0 = cities.indexOf(city1);
                                    cityCode = cities.get(index0).getC_id();
                                }
                            }
                            if (!provinceCode.equals("") && !cityCode.equals("")) {

//                        provinceCode = "88";
//                        cityCode = "19";

                                HttpPost<PostLocationEntity> httpPost = new HttpPost<>();
                                PostLocationEntity postLocationEntity = new PostLocationEntity();
                                postLocationEntity.setSession_token(Util.getLocalAdmin(MainNewActivity.this)[0]);
                                postLocationEntity.setProvinceCode(provinceCode);
                                postLocationEntity.setCityCode(cityCode);
                                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                                httpPost.setParameter(postLocationEntity);
                                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postLocationEntity) + BaseCom.APP_PWD));

                                new OrderHttp().judgeLocation(new Subscriber<HttpRequest<JudgeLocationResponse>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(HttpRequest<JudgeLocationResponse> booleanHttpRequest) {
                                        if (booleanHttpRequest.getData().getJudgeLocation()) {

                                        } else {
                                            Util.stopSub();
                                            if (_dialogLocation == null || !_dialogLocation.isShowing()) {
                                                _dialogLocation = _dialogBuilderLocation.show();
                                            }
                                        }
                                    }
                                }, httpPost);
                            }

                        }
                    }

                }

                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setBackGroundTitle("首页", false, true, true);
        Log.d("yyy", Util.getVersion());
        _dialogBuilderLocation = Util.createAlertDialog(AppManager.getAppManager().currentActivity(),
                "用户未在常用地使用", null);
        _dialogBuilderLocation.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Util.gotoActy(AppManager.getAppManager().currentActivity(), LoginActivity.class);
            }
        });
        _dialogBuilderLocation.setCancelable(false);
        fragmentList.clear();
        if (Util.getLocalAdmin(MainNewActivity.this)[1].equals("lev3")) {
            /*渠道*/
            fragmentList.add(new HomeMainFragment());
            fragmentList.add(new OrderMainNewFragment());
            fragmentList.add(new AccountMainFragment());
            fragmentList.add(new CardMainFragment());
            llQds.setVisibility(View.GONE);
        } else {
            /*代理*/
            fragmentList.add(new HomeMainFragment());
            fragmentList.add(new QdsMainNewFragment());
            fragmentList.add(new CardMainFragment());
            llQds.setVisibility(View.VISIBLE);
        }
        pagerAdapter = new PagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (fragmentList.size()) {
                    case 3:
                        switch (position) {
                            case 0:
                                imgHome.setSelected(true);
                                imgQds.setSelected(false);
                                imgCard.setSelected(false);
                                tvHome.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorOrange));
                                tvQds.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvAccount.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));

                                break;
                            case 1:
                                imgHome.setSelected(false);
                                imgQds.setSelected(true);
                                imgCard.setSelected(false);
                                tvHome.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvQds.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorOrange));
                                tvAccount.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                break;
                            case 2:
                                imgHome.setSelected(false);
                                imgQds.setSelected(false);
                                imgCard.setSelected(true);
                                tvHome.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvQds.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvAccount.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorOrange));
                                break;
                        }
                        break;
                    case 4:
                        switch (position) {
                            case 0:
                                imgHome.setSelected(true);
                                imgOrder.setSelected(false);
                                imgAccount.setSelected(false);
                                imgCard.setSelected(false);
                                tvHome.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorOrange));
                                tvOrder.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvAccount.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvCard.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                break;
                            case 1:
                                imgHome.setSelected(false);
                                imgOrder.setSelected(true);
                                imgAccount.setSelected(false);
                                imgCard.setSelected(false);
                                tvHome.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvOrder.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorOrange));
                                tvAccount.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvCard.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                break;
                            case 2:
                                imgHome.setSelected(false);
                                imgOrder.setSelected(false);
                                imgAccount.setSelected(true);
                                imgCard.setSelected(false);
                                tvHome.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvOrder.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvAccount.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorOrange));
                                tvCard.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                break;
                            case 3:
                                imgHome.setSelected(false);
                                imgOrder.setSelected(false);
                                imgAccount.setSelected(false);
                                imgCard.setSelected(true);
                                tvHome.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvOrder.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvAccount.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorGray16));
                                tvCard.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.colorOrange));
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        viewPager.setCurrentItem(0);


        getMessage();
        _dialogBuilderPermission = Util.createAlertDialog(AppManager.getAppManager().currentActivity(),
                "未打开位置信息或未开启定位权限，禁止使用", null);
        _dialogBuilderPermission.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                Util.gotoActy(AppManager.getAppManager().currentActivity(), LoginActivity.class);
            }
        });
        _dialogBuilderPermission.setCancelable(false);

        Util.checkCount();
        uploadLog();

    }

    private void uploadLog() {
        String path = SPUtil.get(MyApplication.context, "log_path", "").toString();
        if (!path.equals("") && FileLogUtils.checkFileExit(path)) {
            Log.d("xxx", "upload");
            File file = new File(path);
            if (!file.exists()) {
                Log.w("AsyncPutImage", "FileNotExist");
                Log.w("LocalFile", path);
                return;
            }
            Log.d("zzzzzz", file.getName());

            // 构造上传请求
            PutObjectRequest put = new PutObjectRequest(OSSConfig.BUCKET, "newLog/00" + "/" + file.getName(), path);
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    Log.d("vvvvv", currentSize + "");
                }
            });


            OSSAsyncTask task = MyApplication.oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    Log.d("vvvvv", "success");
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    Log.d("vvvvv", "failed");
                }
            });

        }
    }


    private void getMessage() {
        if (BaseCom.index_main == 0) {
            Log.d("aaa", "message");
            dialog.getTvTitle().setText("正在获取数据");
            dialog.show();
//            if (Util.isLog(MainActivity.this)) {
            HttpPost<PostNoticeList> httpPost1 = new HttpPost<>();
            PostNoticeList postNoticeList = new PostNoticeList();
            postNoticeList.setSession_token(Util.getLocalAdmin(MainNewActivity.this)[0]);
            postNoticeList.setPage("1");
            postNoticeList.setLinage("10");
            postNoticeList.setType(2);
            httpPost1.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost1.setParameter(postNoticeList);
            httpPost1.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNoticeList) + BaseCom.APP_PWD));
            new OtherHttp().noticeList(OtherRequest.noticeList(0, MainNewActivity.this, dialog, new SuccessValue<RequestNoticeList>() {
                @Override
                public void OnSuccess(RequestNoticeList value) {
                    BaseCom.index_main = 1;
                    String update = value.getNotice()[0].getUpdateDate();
                    Log.d("ccc", "update:" + update);
                    SharedPreferences share = getSharedPreferences(BaseCom.MESSAGE, MODE_PRIVATE);
                    String time = share.getString("time", "0");
                    Log.d("ccc", "now:" + time);
                    if (time.equals("0")) {
                        Log.d("ccc", "11");
                        SharedPreferences.Editor edit = share.edit(); //编辑文件
                        edit.putString("time", update);
                        edit.commit();  //保存数据信息
                    } else {
                        if (Util.compareSecond(time, update)) {
                            Log.d("ccc", "22");
                            imgNew.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("ccc", "33");
                            imgNew.setVisibility(View.GONE);
                        }
                    }


                }
            }), httpPost1);
        }
    }

    public class PagerAdapter extends FragmentStateAdapter {

        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ccc", "destroy");
        LogUtils.setAppendFile("MainActivity.destroy");
        EventBus.getDefault().unregister(this);
        Util.stopSub();
        BaseCom.index_home = 0;
        BaseCom.index_main = 0;

        int phoneRecharge = Util.getClickTime(MainNewActivity.this, BaseCom.phoneRecharge);
        int accountRecharge = Util.getClickTime(MainNewActivity.this, BaseCom.accountRecharge);
        int transform = Util.getClickTime(MainNewActivity.this, BaseCom.transform);
        int renewOpen = Util.getClickTime(MainNewActivity.this, BaseCom.renewOpen);
        int newOpen = Util.getClickTime(MainNewActivity.this, BaseCom.newOpen);
        int replace = Util.getClickTime(MainNewActivity.this, BaseCom.replace);
        int phoneBanlance = Util.getClickTime(MainNewActivity.this, BaseCom.phoneBanlance);
        int accountRecord = Util.getClickTime(MainNewActivity.this, BaseCom.accountRecord);
        int cardQuery = Util.getClickTime(MainNewActivity.this, BaseCom.cardQuery);
        int orderQueryRenew = Util.getClickTime(MainNewActivity.this, BaseCom.orderQueryRenew);
        int orderQueryNew = Util.getClickTime(MainNewActivity.this, BaseCom.orderQueryNew);
        int orderQueryTransform = Util.getClickTime(MainNewActivity.this, BaseCom.orderQueryTransform);
        int orderQueryReplace = Util.getClickTime(MainNewActivity.this, BaseCom.orderQueryReplace);
        int orderQueryRecharge = Util.getClickTime(MainNewActivity.this, BaseCom.orderQueryRecharge);
        int qdsList = Util.getClickTime(MainNewActivity.this, BaseCom.qdsList);
        int qdsOrderList = Util.getClickTime(MainNewActivity.this, BaseCom.qdsOrderList);

        HttpPost<PostFunction> httpPost = new HttpPost<>();
        PostFunction postFunction = new PostFunction();
        if (phoneRecharge != 0) {
            postFunction.setPhoneRecharge(phoneRecharge);
        }
        if (accountRecharge != 0) {
            postFunction.setAccountRecharge(accountRecharge);
        }
        if (transform != 0) {
            postFunction.setTransform(transform);
        }
        if (renewOpen != 0) {
            postFunction.setRenewOpen(renewOpen);
        }
        if (newOpen != 0) {
            postFunction.setNewOpen(newOpen);
        }
        if (replace != 0) {
            postFunction.setReplace(replace);
        }
        if (phoneBanlance != 0) {
            postFunction.setPhoneBanlance(phoneBanlance);
        }
        if (accountRecord != 0) {
            postFunction.setAccountRecord(accountRecord);
        }
        if (cardQuery != 0) {
            postFunction.setCardQuery(cardQuery);
        }
        if (orderQueryRenew != 0) {
            postFunction.setOrderQueryRenew(orderQueryRenew);
        }
        if (orderQueryNew != 0) {
            postFunction.setOrderQueryNew(orderQueryNew);
        }
        if (orderQueryTransform != 0) {
            postFunction.setOrderQueryTransform(orderQueryTransform);
        }
        if (orderQueryReplace != 0) {
            postFunction.setOrderQueryReplace(orderQueryReplace);
        }
        if (orderQueryRecharge != 0) {
            postFunction.setOrderQueryRecharge(orderQueryRecharge);
        }
        if (qdsList != 0) {
            postFunction.setQdsList(qdsList);
        }
        if (qdsOrderList != 0) {
            postFunction.setQdsOrderList(qdsOrderList);
        }
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postFunction);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postFunction) + BaseCom.APP_PWD));
        new OtherHttp().functionStatistics(OtherRequest.functionStatistics(new SuccessNull() {
            @Override
            public void onSuccess() {
                Util.cleartClickTime(MainNewActivity.this, BaseCom.phoneRecharge);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.accountRecharge);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.transform);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.renewOpen);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.newOpen);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.replace);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.phoneBanlance);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.accountRecord);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.cardQuery);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.orderQueryRenew);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.orderQueryNew);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.orderQueryTransform);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.orderQueryReplace);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.orderQueryRecharge);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.qdsList);
                Util.cleartClickTime(MainNewActivity.this, BaseCom.qdsOrderList);
                Log.d("aaa", "success");
            }
        }), httpPost);
    }

    @OnClick({R.id.llHome, R.id.llOrder, R.id.llQds, R.id.llAccount, R.id.llCard})
    public void onClick(View view) {
        clearSelection();
        switch (view.getId()) {
            case R.id.llHome:
                setBackGroundTitle("首页", false, true, true);
                viewPager.setCurrentItem(0);
                break;
            case R.id.llOrder:
                setBackGroundTitle("订单查询", false, true, true);
                viewPager.setCurrentItem(1);
                break;
            case R.id.llQds:
                setBackGroundTitle("渠道商管理", false, true, true);
                viewPager.setCurrentItem(1);
                break;
            case R.id.llAccount:
                setBackGroundTitle("账户查询", false, true, true);
                viewPager.setCurrentItem(2);
                break;
            case R.id.llCard:
                setBackGroundTitle("业务管理", false, true, true);
                viewPager.setCurrentItem(fragmentList.size() - 1);
                break;
        }
    }

    private void clearSelection() {
        imgHome.setImageResource(R.mipmap.home);
        tvHome.setTextColor(getResources().getColor(R.color.colorGray16));
        imgOrder.setImageResource(R.mipmap.order);
        tvOrder.setTextColor(getResources().getColor(R.color.colorGray16));
        imgAccount.setImageResource(R.mipmap.account);
        tvAccount.setTextColor(getResources().getColor(R.color.colorGray16));
        imgCard.setImageResource(R.mipmap.card);
        tvCard.setTextColor(getResources().getColor(R.color.colorGray16));
        imgQds.setImageResource(R.mipmap.order);
        tvQds.setTextColor(getResources().getColor(R.color.colorGray16));
    }

//    private void addOrShowFragment(FragmentTransaction transaction,
//                                   Fragment fragment) {
//        if (currentFragment == fragment)
//            return;
//        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
//            transaction.hide(currentFragment)
//                    .add(R.id.framContent, fragment).commit();
//        } else {
//
//            transaction.hide(currentFragment).show(fragment).commit();
//        }
//        currentFragment = fragment;
//    }

    @OnClick(R.id.imgMessage)
    public void onClick() {
        imgNew.setVisibility(View.GONE);
        gotoActy(MessageMainActivity.class);
    }
}
