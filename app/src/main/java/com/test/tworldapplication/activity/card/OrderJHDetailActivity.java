package com.test.tworldapplication.activity.card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.PhoneDetailActivity;
import com.test.tworldapplication.activity.order.OrderKhListActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.JHOrderInfo;
import com.test.tworldapplication.entity.Number;
import com.test.tworldapplication.entity.PostJHOrderInfo;
import com.test.tworldapplication.entity.RequestSelectionOrderInfo;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class OrderJHDetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ArrayList<TextView> titles = new ArrayList<>();
    ArrayList<Fragment> pages = new ArrayList<>();
    @BindView(R.id.main_tab1)
    TextView mainTab1;
    @BindView(R.id.main_tab2)
    TextView mainTab2;

    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.main_tab_line)
    View mainTabLine;
    private int mTabLineWidth;
    //    private OrderKH orderKH;
    private Number number;
    private
    MyAdapter adapter;
    RequestSelectionOrderInfo requestSelectionOrderInfo;
    String from;//0成卡1白卡
    private Dialog mDialog;
    JHOrderInfo jhOrderInfo;
    MyFragment fragment0, fragment1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_jhdetail);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);
        from = getIntent().getStringExtra("from");
        number = (Number) getIntent().getSerializableExtra("data");
        dialog.getTvTitle().setText("正在获取数据");
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);
        fragment0 = new MyFragment("tab1");
        fragment1 = new MyFragment("tab2");


        titles.add(mainTab1);
        titles.add(mainTab2);

        pages.add(fragment0);
        pages.add(fragment1);


        // create new fragments

        adapter = new MyAdapter(getSupportFragmentManager());
        initView();
        getData();
    }

    private void getData() {
        PostJHOrderInfo postPreNumberDetails = new PostJHOrderInfo();
        postPreNumberDetails.setSession_token(Util.getLocalAdmin(OrderJHDetailActivity.this)[0]);
        postPreNumberDetails.setePreNo(number.getePreNo());
        HttpPost<PostJHOrderInfo> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postPreNumberDetails) + BaseCom.APP_PWD));
        httpPost.setParameter(postPreNumberDetails);
        new CardHttp().preNumberOrderInfo(httpPost, new Subscriber<HttpRequest<JHOrderInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<JHOrderInfo> request) {
                if (request.getCode() == BaseCom.NORMAL) {
                    jhOrderInfo = request.getData();
                    fragment0.initData(OrderJHDetailActivity.this, jhOrderInfo, number);
                    fragment1.initData(OrderJHDetailActivity.this, jhOrderInfo, number);

                } else if (request.getCode() == BaseCom.LOSELOG || request.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(OrderJHDetailActivity.this, LoginActivity.class);
                else
                    Toast.makeText(OrderJHDetailActivity.this, request.getMes() + "", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initView() {
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);
        titles.get(0).setSelected(true);

        initTabline();

    }

    private void initTabline() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTabLineWidth = outMetrics.widthPixels / 2;
        ViewGroup.LayoutParams lp = mainTabLine.getLayoutParams();
        lp.width = mTabLineWidth;
        mainTabLine.setLayoutParams(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab1:
                mainViewpager.setCurrentItem(0, true);
                break;
            case R.id.main_tab2:
                mainViewpager.setCurrentItem(1, true);
                break;
            default:
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mainTabLine.getLayoutParams();
        lp.leftMargin = (int) ((position + positionOffset) * mTabLineWidth);
        mainTabLine.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < titles.size(); i++) {
            if (position == i) {
                titles.get(i).setSelected(true);
            } else {
                titles.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return pages.get(arg0);
        }
    }

    @SuppressLint("ValidFragment")
    public static class MyFragment extends Fragment {
        private String text;
        @BindView(R.id.tvId)
        TextView tvId;
        @BindView(R.id.tvPhonn)
        TextView tvPhonn;
        @BindView(R.id.tvOrdertime)
        TextView tvOrdertime;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.llOrderDetail)
        LinearLayout llOrderDetail;

        @BindView(R.id.llPhoneDetail)
        LinearLayout llPhoneDetail;
        @BindView(R.id.llPre)
        LinearLayout llPre;
        @BindView(R.id.tvChoose)
        TextView tvChoose;
        @BindView(R.id.tvCycle)
        TextView tvCycle;
        @BindView(R.id.tvPromotion)
        TextView tvPromotion;
        @BindView(R.id.tvSubmit)
        TextView tvSubmit;
        @BindView(R.id.tvDetail)
        TextView tvDetail;
        JHOrderInfo details0;
        Number number0;
        Activity activity0;

        public MyFragment(String text) {
            this.text = text;

        }


        public void initData(Activity activity, JHOrderInfo jhOrderInfo, Number number) {
            this.activity0 = activity;
            this.details0 = jhOrderInfo;
            this.number0 = number;

            tvId.setText(number.getePreNo());
            tvPhonn.setText(number.getNumber());
            tvOrdertime.setText(number.getCreatedate());
            tvType.setText("写卡激活");
            if (number.getState() != null)
                tvState.setText(number.getState());
            if (number.getState() != null && number.getState().equals("待激活")) {
                tvSubmit.setText("激活");
                tvSubmit.setVisibility(View.VISIBLE);
            } else if (number.getState() != null && (number.getState().equals("失败") || number.getState().equals("已锁定"))) {
                tvSubmit.setText("重写");
                tvSubmit.setVisibility(View.VISIBLE);
            } else
                tvSubmit.setVisibility(View.GONE);
            tvDetail.setText(number.getMemo4());
            tvChoose.setText(new BigDecimal(details0.getAmount()).setScale(2, RoundingMode.HALF_UP) + "");
            tvCycle.setText(details0.getCycle());
            tvPromotion.setText(number.getPromotionDesc());
            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.d("nnn", "postNumber:" + number.getNumber());
                    if (number.getState().equals("待激活")) {
                        Intent intent = new Intent(activity0, SelectActivity.class);
                        intent.putExtra("from", "2");
                        intent.putExtra("number", number0.getNumber());
                        intent.putExtra("preStore", "");
                        intent.putExtra("detail", number0.getMemo4());
                        startActivity(intent);
                    } else if (number.getState().equals("失败")) {
                        Intent intent = new Intent(activity0, PhoneDetailActivity.class);
                        intent.putExtra("number", number.getNumber());
                        intent.putExtra("orderNo", number.getePreNo());
                        intent.putExtra("from", "0");

                        startActivity(intent);
                    } else if (number.getState().equals("已锁定")) {
                        Intent intent = new Intent(activity0, PhoneDetailActivity.class);
                        intent.putExtra("number", number.getNumber());
                        intent.putExtra("orderNo", number.getePreNo());
                        intent.putExtra("from", "1");

                        startActivity(intent);

                    }

                }
            });


        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_jh_detail, null);
            ButterKnife.bind(this, view);
            llPre.setVisibility(View.GONE);

            switch (text) {
                case "tab1":
                    llOrderDetail.setVisibility(View.VISIBLE);
                    llPhoneDetail.setVisibility(View.GONE);
                    break;
                case "tab2":
                    llOrderDetail.setVisibility(View.GONE);
                    llPhoneDetail.setVisibility(View.VISIBLE);
                    break;
            }

            if (details0 != null) {
                initData(activity0, details0, number0);
            }
            return view;
        }
    }


}

