package com.test.tworldapplication.activity.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.HbcjActivity;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.account.SubAccountActivity;
import com.test.tworldapplication.activity.admin.WriteInActivity;
import com.test.tworldapplication.activity.card.BillQueryActivity;
import com.test.tworldapplication.activity.card.ProductBusinessActivity;
import com.test.tworldapplication.activity.card.QdsCuteSelectActivity;
import com.test.tworldapplication.activity.card.ReplaceCardActivity;
import com.test.tworldapplication.activity.card.SelectActivity;
import com.test.tworldapplication.activity.card.TransferCardActivity;
import com.test.tworldapplication.activity.main.PhoneListActivity;
import com.test.tworldapplication.adapter.HomeGrideAdapter;
import com.test.tworldapplication.adapter.RollPagerAdapter;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Carousel;
import com.test.tworldapplication.entity.CheckInfo;
import com.test.tworldapplication.entity.Home;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostCarousel;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.PostStatistic;
import com.test.tworldapplication.entity.RequestCarousel;
import com.test.tworldapplication.entity.RequestStatistic;
import com.test.tworldapplication.entity.Statistics;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;
import com.test.tworldapplication.view.GridViewForScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeMainFragment extends Fragment implements DialogInterface.OnKeyListener, AdapterView.OnItemClickListener {
    //    @BindView(R.id.adview)
//    ADView adview;
    List<Integer> list = new ArrayList<>();
    List<Home> homeList = new ArrayList<>();
    @BindView(R.id.main_grideview)
    GridViewForScrollView mainGrideview;
    @BindView(R.id.ll_QD)
    LinearLayout llQD;
    @BindView(R.id.ll_DL)
    LinearLayout llDL;
    @BindView(R.id.llHalfNumber)
    RelativeLayout llHalfNumber;
    @BindView(R.id.llOneNumber)
    RelativeLayout llOneNumber;
    @BindView(R.id.vHalfNumber)
    View vHalfNumber;
    @BindView(R.id.vOneNumber)
    View vOneNumber;
    Gson gson = new Gson();
    Carousel[] carousels;
    Carousel[] mCarousels;
    @BindView(R.id.chartNumber)
    LineChart chartNumber;
    int numbers_state = 0;
    CheckResultDialog dialog;
    LineData numberData;

    View homeLayout;
    @BindView(R.id.fragment_shouye_rollpagerview)
    RollPagerView rollPagerView;

    RollPagerAdapter rollPagerAdapter;
    List<Carousel> carouselList = new ArrayList<>();
    HomeGrideAdapter adapter;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private BarData mBarData;

    private Dialog mDialog;

    public ScrollView getScrollView() {
        return scrollView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homeLayout = LayoutInflater.from(getActivity()).inflate(R.layout.activity_home_main, null);

        ButterKnife.bind(this, homeLayout);

        dialog = new CheckResultDialog(getActivity(), R.style.CustomDialog);
        dialog.getTvTitle().setText("正在请求数据");
        Log.d("ttttt", "ttttt");
        dialog.setOnKeyListener(this);

        initViewPager();
        searchCarousel();

        return homeLayout;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ttttt", "ttttt0");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ttttt", "ttttt1");
    }


    private void initViewPager() {

        mainGrideview.setNumColumns(3);
        adapter = new HomeGrideAdapter(getActivity(), homeList);
        mainGrideview.setAdapter(adapter);
        mainGrideview.setOnItemClickListener(this);
        mainGrideview.setFocusable(false);
        scrollView.smoothScrollTo(0, 0);


        rollPagerView.setPlayDelay(5000);
        //设置透明度
        rollPagerView.setAnimationDurtion(500);
        //设置小圆点
        rollPagerView.setHintView(new ColorPointHintView(getActivity(), Color.BLACK, Color.argb(100, 74, 74, 74)));
//        for (int i = 0; i < 3; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("image", R.drawable.logo);
//            rollList.add(map);
//        }
        rollPagerAdapter = new RollPagerAdapter(rollPagerView, getActivity(), carouselList);
        rollPagerView.setAdapter(rollPagerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void searchCarousel() {
        //判断是否是第一次进入页面 第一次获取轮播图接口 初始化轮播图
        if (BaseCom.index_home == 0) {
            Log.d("ccc", "0");

            HttpPost<PostCarousel> httpPost = new HttpPost();
            PostCarousel postCarousel = new PostCarousel();
            postCarousel.setParameter("");
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postCarousel);
            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCarousel) + BaseCom.APP_PWD));
            Log.d("aaa", gson.toJson(httpPost));


            new OtherHttp().carousel(OtherRequest.carousel(getActivity(), new SuccessValue<RequestCarousel>() {
                @Override
                public void OnSuccess(RequestCarousel value) {
                    BaseCom.index_home = 1;

                    carousels = value.getCarousel_Picture();
                    mCarousels = new Carousel[carousels.length + 2];
                    mCarousels[0] = carousels[carousels.length - 1];
                    mCarousels[carousels.length + 1] = carousels[0];
                    for (int i = 0; i < carousels.length; i++) {
                        mCarousels[i + 1] = carousels[i];
                    }

                    BaseCom.carousels = mCarousels;
                    carouselList.addAll(Arrays.asList(carousels));
                    rollPagerAdapter.notifyDataSetChanged();
//                    adview.setArr(mCarousels);
//                    adview.init();
                }
            }), httpPost);

        } else {
            Log.d("ccc", "1");
            carouselList.addAll(Arrays.asList(BaseCom.carousels));
            rollPagerAdapter.notifyDataSetChanged();
//            adview.setArr(BaseCom.carousels);
//            adview.init();
        }

    }

    //开户量数据,代理商使用Logi
    public void searchNumbers() {
        Log.d("sss", "search");

        String numbers = "";
        switch (numbers_state) {
            case 0:
                numbers = "month";
                break;
            case 1:
                numbers = "year";
                break;
        }
        dialog.show();
        HttpPost<PostStatistic> httpPost = new HttpPost<>();
        PostStatistic postStatistic = new PostStatistic();
        postStatistic.setSession_token(Util.getLocalAdmin(getActivity())[0]);
        postStatistic.setDate(numbers);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postStatistic);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postStatistic) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new AdminHttp().statistic(AdminRequest.statistic(getActivity(), dialog, new SuccessValue<RequestStatistic>() {
            @Override
            public void OnSuccess(RequestStatistic value) {
                Statistics[] statisticses = value.getStatisticsList();
                Log.d("ccc", statisticses.length + "");
                numberData = Util.getLineData(statisticses.length, 1, numbers_state, statisticses, null);
                Util.showChart(numbers_state, chartNumber, numberData, Color.BLACK);
//                mBarData = Util.getBarData(4, (float) 100);
//                Util.showBarChart(chartNumber, mBarData);
            }
        }), httpPost);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ttttt", "ttttt2");
        if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
            llQD.setVisibility(View.VISIBLE);
            llDL.setVisibility(View.GONE);
            initGride();
        } else {
            llQD.setVisibility(View.VISIBLE);
            llDL.setVisibility(View.VISIBLE);
            initGride();
            searchNumbers();
        }
    }

    public void initGride() {
//        homeList = new ArrayList<>();
        homeList.clear();
        if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
            homeList.add(new Home(R.mipmap.home_ywbl, "业务办理"));
            homeList.add(new Home(R.mipmap.home_yecz, "余额充值"));
            homeList.add(new Home(R.mipmap.home_gh, "过户"));
            homeList.add(new Home(R.mipmap.home_ckkh, "成卡开户"));
//            homeList.add(new Home(R.mipmap.home_bkkh, "白卡开户"));
            homeList.add(new Home(R.mipmap.home_bk, "补卡"));
            homeList.add(new Home(R.mipmap.home_lhpt, "靓号"));
//            homeList.add(new Home(R.mipmap.home_cpgm, "流量包"));
//            homeList.add(new Home(R.mipmap.home_bkyk, "白卡预开户"));
            homeList.add(new Home(R.mipmap.hbcj, "红包抽奖"));
            homeList.add(new Home(R.mipmap.home_bill, "账单查询"));
            homeList.add(new Home(R.mipmap.order_icon_establish_large, "写卡激活"));
        } else {
            //homeList.add(new Home(R.mipmap.home_lhpt, "靓号"));
            homeList.add(new Home(R.mipmap.home_ykjl, "预开户记录"));
            homeList.add(new Home(R.mipmap.home_bkyk, "白卡预开户"));
            homeList.add(new Home(R.mipmap.home_bill, "子账户开户记录"));


        }
        adapter.notifyDataSetChanged();


    }


    private void gotoActivity(Class clazz) {
        if (!Util.isFastDoubleClick()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mySP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("pattern", 3);
            editor.putInt("modes", 3);
            editor.putInt("readModes", 3);
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(getActivity(), clazz);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }

    @OnClick({R.id.llHalfNumber, R.id.llOneNumber})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llHalfNumber:
                numbers_state = 0;
                break;
            case R.id.llOneNumber:
                numbers_state = 1;
                break;
        }
        clearNumberClick();

    }

    private void clearNumberClick() {
        vHalfNumber.setBackgroundResource(R.drawable.shape_checkbox_noclick);
        vOneNumber.setBackgroundResource(R.drawable.shape_checkbox_noclick);
        switch (numbers_state) {
            case 0:
                vHalfNumber.setBackgroundResource(R.drawable.shape_checkbox_click);
                break;
            case 1:
                vOneNumber.setBackgroundResource(R.drawable.shape_checkbox_click);
                break;
        }
        searchNumbers();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
                    Util.saveClickTime(getActivity(), BaseCom.phoneRecharge);
//                    gotoActivity(NumberRechargeActivity.class);
                    gotoActivity(YewubanliActivity.class);
                } else {
                    Util.createToast(getActivity(), "该功能待开发,敬请期待");
                    //gotoActivity(CuteNumberActivity.class);
                }

                break;
            case 1:
                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
                    Util.saveClickTime(getActivity(), BaseCom.accountRecharge);
                    gotoActivity(AccountBalanceActivity.class);
                } else {
                    Util.createToast(getActivity(), "该功能待开发,敬请期待");
//                    gotoActivity(PreOpenRecordActivity.class);
                }
                break;
            case 2:
                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
                    Util.saveClickTime(getActivity(), BaseCom.transform);
                    gotoActivity(TransferCardActivity.class);
                } else {
//                    Util.createToast(getActivity(), "该功能待开发,敬请期待");
                    Util.saveClickTime(getActivity(), BaseCom.transform);
                    gotoActivity(SubAccountActivity.class);
                }
                break;
            case 3:
                Util.saveClickTime(getActivity(), BaseCom.renewOpen);
//                gotoActivity(RenewCardActivity.class);
                Intent intent = new Intent(getActivity(), SelectActivity.class);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;
//            case 4:
//                Util.createToast(getActivity(), "该功能暂未开放,敬请期待");
//                gotoActivity(NewCardActivity.class);
//                break;
            case 4:
                Util.saveClickTime(getActivity(), BaseCom.replace);
                gotoActivity(ReplaceCardActivity.class);
                break;
            case 5:
//                Util.createToast(getActivity(), "该功能待开发,敬请期待");
//                Util.saveClickTime(getActivity(), BaseCom.cardQuery);
                gotoActivity(QdsCuteSelectActivity.class);
                break;
//            case 6:
////                Util.createToast(getActivity(), "该功能待开发,敬请期待");
////                Util.saveClickTime(getActivity(), BaseCom.newOpen);
//                gotoActivity(ProductBusinessActivity.class);
//            break;
//            case 7:
//                Util.saveClickTime(getActivity(), BaseCom.replace);
//                gotoActivity(QudaoWhitePreOpenActivity.class);
//                Util.createToast(getActivity(), "该功能待开发,敬请期待");
//                break;
            case 6:
//                Util.saveClickTime(getActivity(), BaseCom.replace);
//                gotoActivity(HbcjActivity.class);
//                Util.createToast(getActivity(), "该功能待开发,敬请期待");
//                gotoActivity(HbcjActivity.class);

                HttpPost<PostOpenPower> httpPost = new HttpPost<>();
                PostOpenPower postOpenPower = new PostOpenPower();
                postOpenPower.setSession_token(Util.getLocalAdmin(getActivity())[0]);
                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpenPower) + BaseCom.APP_PWD));
                httpPost.setParameter(postOpenPower);
                Log.d("aaa", gson.toJson(httpPost));
                new AdminHttp().checkInfo(AdminRequest.checkInfo(getActivity(), new SuccessValue<CheckInfo>() {
                    @Override
                    public void OnSuccess(CheckInfo value) {
                        if (value.getRegisterFlag().equals("N")) {
                            gotoActivity(HbcjActivity.class);
                        } else {
                            onCheckInfoDialog().show();
                        }
                    }
                }), httpPost);


                break;
            case 7:
//                Util.saveClickTime(getActivity(), BaseCom.replace);
                gotoActivity(BillQueryActivity.class);
//                Util.createToast(getActivity(), "该功能待开发,敬请期待");
                break;
            case 8:
                gotoActivity(PhoneListActivity.class);
                break;
        }
    }

    public Dialog onCheckInfoDialog() {
//        mContext = this;
        mDialog = new Dialog(getContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_check_info);
        mDialog.setCanceledOnTouchOutside(true);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0); //没有边距
        //layoutParams.dimAmount = 0.0f;  //背景不变暗
        layoutParams.dimAmount = 0.7f;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //背景透明
        window.setAttributes(layoutParams);
        TextView refuse = (TextView) mDialog.findViewById(R.id.refuse);
        TextView agree = (TextView) mDialog.findViewById(R.id.agree);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                Util.saveClickTime(getActivity(), BaseCom.transform);
                Intent intent = new Intent();
                intent.putExtra("from", "1");
                intent.setClass(getActivity(), WriteInActivity.class);
                startActivity(intent);

//                gotoActivity(RefillInfoActivity.class);
            }
        });
        return mDialog;
    }

}
