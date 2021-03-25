package com.test.tworldapplication.activity.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.OrderKH;
import com.test.tworldapplication.entity.PostOrderInfo;
import com.test.tworldapplication.entity.RequestOrderInfo;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderKHDetailNewActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, SuccessValue<RequestOrderInfo> {

    ArrayList<TextView> titles = new ArrayList<>();
    ArrayList<Fragment> pages = new ArrayList<>();
    @BindView(R.id.main_tab1)
    TextView mainTab1;
    @BindView(R.id.main_tab2)
    TextView mainTab2;
    @BindView(R.id.main_tab3)
    TextView mainTab3;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.main_tab_line)
    View mainTabLine;
    private int mTabLineWidth;
    private OrderKH orderKH;
    MyAdapter adapter;
    RequestOrderInfo requestOrderInfo;
    String from;//0成卡1白卡
    MyFragment myFragment0;
    MyFragment myFragment1;
    MyFragment myFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bk_detail_new);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);
        from = getIntent().getStringExtra("from");
        orderKH = (OrderKH) getIntent().getSerializableExtra("data");
        dialog.getTvTitle().setText("正在获取数据");
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);
        mainTab3.setOnClickListener(this);

        mainTab3.setVisibility(View.VISIBLE);


        titles.add(mainTab1);
        titles.add(mainTab2);
        titles.add(mainTab3);
        myFragment0 = new MyFragment("tab1");
        myFragment1 = new MyFragment("tab2");
        myFragment2 = new MyFragment("tab3");

        pages.add(myFragment0);
        pages.add(myFragment1);
        pages.add(myFragment2);


        // create new fragments

        adapter = new MyAdapter(getSupportFragmentManager());
        initTabline();
        getData();


    }

    private void getData() {
        Toast.makeText(OrderKHDetailNewActivity.this, "正在查询数据,请稍后", Toast.LENGTH_SHORT).show();

        HttpPost<PostOrderInfo> httpPost = new HttpPost<>();
        PostOrderInfo postOrderInfo = new PostOrderInfo();
        postOrderInfo.setSession_token(Util.getLocalAdmin(OrderKHDetailNewActivity.this)[0]);
        postOrderInfo.setOrderNo(orderKH.getOrderNo());
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postOrderInfo);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postOrderInfo) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().orderInfo(from, OrderRequest.orderInfo(OrderKHDetailNewActivity.this, dialog, this), httpPost);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void OnSuccess(RequestOrderInfo value) {
        this.requestOrderInfo = value;
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);
        titles.get(0).setSelected(true);


        myFragment0.initData(OrderKHDetailNewActivity.this, value);
        myFragment1.initData(OrderKHDetailNewActivity.this, value);
        myFragment2.initData(OrderKHDetailNewActivity.this, value);

    }


    //
//    @Override
//    protected void onResume() {
//        super.onResume();
//        HttpPost<PostOrderInfo> httpPost = new HttpPost<>();
//        PostOrderInfo postOrderInfo = new PostOrderInfo();
//        postOrderInfo.setSession_token(Util.getLocalAdmin(OrderKHDetailNewActivity.this)[0]);
//        postOrderInfo.setOrderNo(orderNo);
//        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//        httpPost.setParameter(postOrderInfo);
//        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postOrderInfo) + BaseCom.APP_PWD));
//        Log.d("aaa", gson.toJson(httpPost));
//        new OrderHttp().orderInfo(OrderRequest.orderInfo(OrderKHDetailNewActivity.this, dialog, new SuccessValue<RequestOrderInfo>() {
//            @Override
//            public void OnSuccess(RequestOrderInfo value) {
//                requestOrderInfo = value;
//                pages.add(new MyFragment("tab1"));
//                pages.add(new MyFragment("tab2"));
//                pages.add(new MyFragment("tab3"));
//                adapter.notifyDataSetChanged();
//            }
//        }), httpPost);

//    }

    private void initTabline() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTabLineWidth = outMetrics.widthPixels / 3;
        ViewGroup.LayoutParams lp = mainTabLine.getLayoutParams();
        lp.width = mTabLineWidth;
        mainTabLine.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tab1:
                mainViewpager.setCurrentItem(0, true);
                break;
            case R.id.main_tab2:
                mainViewpager.setCurrentItem(1, true);
                break;
            case R.id.main_tab3:
                mainViewpager.setCurrentItem(2, true);
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
        //
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
        @BindView(R.id.tvChecktime)
        TextView tvChecktime;
        @BindView(R.id.tvResult)
        TextView tvResult;
        @BindView(R.id.llOrderDetail)
        LinearLayout llOrderDetail;
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.tvActivity)
        TextView tvActivity;
        @BindView(R.id.tvRegulai)
        TextView tvRegulai;
        //        @BindView(R.id.tvPackage)
//        TextView tvPackage;
//        @BindView(R.id.tvDate)
//        TextView tvDate;
        @BindView(R.id.llExpenseDetail)
        LinearLayout llExpenseDetail;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvIdCard)
        TextView tvIdCard;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.imgIdAll)
        ImageView imgIdAll;
        @BindView(R.id.imgIdCard)
        ImageView imgIdCard;
        @BindView(R.id.llCustomerDetail)
        LinearLayout llCustomerDetail;
        @BindView(R.id.ll_reason)
        LinearLayout llReason;
        private String text;
        RequestOrderInfo requestOrderInfo;
        Activity activity;

        public MyFragment(String text) {
            this.text = text;
        }

        public void initData(Activity activity, RequestOrderInfo requestOrderInfo) {
            this.requestOrderInfo = requestOrderInfo;
            this.activity = activity;
            String state = "";
            switch (requestOrderInfo.getOrderStatus()) {
                case "PENDING":
                    state = "已提交";
                    break;
                case "WAITING":
                    state = "等待中";
                    break;
                case "SUCCESS":
                    state = "成功";
                    break;
                case "FAIL":
                    state = "失败";
                    break;
                case "CANCLE":
                    state = "已取消";
                case "CLOSED":
                    state = "已关闭";
                    if (requestOrderInfo.getCancelInfo() != null && !requestOrderInfo.getCancelInfo().equals("")) {
                        tvResult.setText(requestOrderInfo.getCancelInfo());
                        llReason.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            String cardType = "";
            switch (requestOrderInfo.getCardType()) {
                case "SIM":
                    cardType = "成卡开户";
                    break;
                case "ESIM":
                    cardType = "白卡开户";
                    break;
            }
            tvId.setText(requestOrderInfo.getOrderNo());
            tvOrdertime.setText(requestOrderInfo.getCreateDate());
            tvType.setText(cardType);
            tvState.setText(requestOrderInfo.getStartName());
            tvChecktime.setText(requestOrderInfo.getUpdateDate());

            tvPhonn.setText(requestOrderInfo.getNumber());
            tvMoney.setText(requestOrderInfo.getPrestore());
            tvActivity.setText(requestOrderInfo.getPromotion());
            tvRegulai.setText(requestOrderInfo.getIsLiang());
//            tvPackage.setText("套餐选择: " + requestOrderInfo.getPackagename());
//            tvDate.setText("起止日期: ");
            tvName.setText(requestOrderInfo.getCustomerName());
            tvIdCard.setText(Util.strPassword(requestOrderInfo.getCertificatesNo()));
            tvAddress.setText(requestOrderInfo.getAddress());
            BitmapUtil.LoadImageByUrl(activity, imgIdAll, requestOrderInfo.getPhotoFront(), DisplayUtil.dp2px(activity, 132f), DisplayUtil.dp2px(activity, 132f));
            BitmapUtil.LoadImageByUrl(activity, imgIdCard, requestOrderInfo.getPhotoBack(), DisplayUtil.dp2px(activity, 132f), DisplayUtil.dp2px(activity, 132f));

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_kh_detail, null);
            ButterKnife.bind(this, view);
            switch (text) {
                case "tab1":
                    llOrderDetail.setVisibility(View.VISIBLE);
                    llExpenseDetail.setVisibility(View.GONE);
                    llCustomerDetail.setVisibility(View.GONE);
                    break;
                case "tab2":
                    llOrderDetail.setVisibility(View.GONE);
                    llExpenseDetail.setVisibility(View.VISIBLE);
                    llCustomerDetail.setVisibility(View.GONE);
                    break;
                case "tab3":
                    llOrderDetail.setVisibility(View.GONE);
                    llExpenseDetail.setVisibility(View.GONE);
                    llCustomerDetail.setVisibility(View.VISIBLE);
                    break;
            }
            if (requestOrderInfo != null) {
                initData(activity, requestOrderInfo);

            }

            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

        }
    }
}
