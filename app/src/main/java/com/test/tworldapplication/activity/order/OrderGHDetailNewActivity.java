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
import com.test.tworldapplication.entity.OrderGH;
import com.test.tworldapplication.entity.PostTransferOrderInfo;
import com.test.tworldapplication.entity.RequestTransferOrderInfo;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderGHDetailNewActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, SuccessValue<RequestTransferOrderInfo> {

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
    private OrderGH orderGH;
    MyAdapter adapter;
    RequestTransferOrderInfo requestTransferOrderInfo;
    MyFragment myFragment0;
    MyFragment myFragment1;
    MyFragment myFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bk_detail_new);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);
        orderGH = (OrderGH) getIntent().getSerializableExtra("data");
        dialog.getTvTitle().setText("正在获取数据");
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);
        mainTab3.setOnClickListener(this);

        mainTab3.setVisibility(View.GONE);


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
        Toast.makeText(OrderGHDetailNewActivity.this, "正在查询数据,请稍后", Toast.LENGTH_SHORT).show();

        HttpPost<PostTransferOrderInfo> httpPost = new HttpPost<>();
        PostTransferOrderInfo postTransferOrderInfo = new PostTransferOrderInfo();
        postTransferOrderInfo.setSession_token(Util.getLocalAdmin(OrderGHDetailNewActivity.this)[0]);
        postTransferOrderInfo.setId(orderGH.getId());
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postTransferOrderInfo);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTransferOrderInfo) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().transferOrderInfo(OrderRequest.transferOrderInfo(OrderGHDetailNewActivity.this, dialog, this), httpPost);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void OnSuccess(RequestTransferOrderInfo value) {
        this.requestTransferOrderInfo = value;
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);
        titles.get(0).setSelected(true);
        myFragment0.initData(OrderGHDetailNewActivity.this,orderGH,value);
        myFragment1.initData(OrderGHDetailNewActivity.this,orderGH,value);
        myFragment1.initData(OrderGHDetailNewActivity.this,orderGH,value);


    }


//    }

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
        @BindView(R.id.tvId)
        TextView tvId;
        @BindView(R.id.tvOrdertime)
        TextView tvOrdertime;
        @BindView(R.id.tvTel)
        TextView tvTel;
        //        @BindView(R.id.tvState)
//        TextView tvState;
        @BindView(R.id.tvChecktime)
        TextView tvChecktime;
        @BindView(R.id.tvCheckResult)
        TextView tvCheckResult;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvNumber)
        TextView tvNumber;
        @BindView(R.id.tvIdCard)
        TextView tvIdCard;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvLast0)
        TextView tvLast0;
        @BindView(R.id.tvLast1)
        TextView tvLast1;
        @BindView(R.id.tvLast2)
        TextView tvLast2;
        @BindView(R.id.tvReason)
        TextView tvReason;
        @BindView(R.id.imgAllBefore)
        ImageView imgAllBefore;
        @BindView(R.id.imgCardBefore)
        ImageView imgCardBefore;
        @BindView(R.id.imgAllNow)
        ImageView imgAllNow;
        @BindView(R.id.imgCardNow)
        ImageView imgCardNow;
        //        //    String time = "";
        @BindView(R.id.ll_ordermsg)
        LinearLayout llOrdermsg;
        @BindView(R.id.ll_moneymsg)
        LinearLayout llMoneymsg;
        @BindView(R.id.ll_customermsg)
        LinearLayout llCustomermsg;
        @BindView(R.id.ll_reason)
        LinearLayout llReason;
        private String text = "";
        Activity activity;
        RequestTransferOrderInfo requestTransferOrderInfo;
        OrderGH orderGH;

        public MyFragment(String text) {
            this.text = text;
        }

        public void initData(Activity activity, OrderGH orderGH, RequestTransferOrderInfo requestTransferOrderInfo) {
            this.activity = activity;
            this.orderGH = orderGH;
            this.requestTransferOrderInfo = requestTransferOrderInfo;
            String startName = "";
            if (requestTransferOrderInfo.getStartName() != null) {
                startName = requestTransferOrderInfo.getStartName();
                if (requestTransferOrderInfo.getStartName().equals("审核不通过")) {
                    if (requestTransferOrderInfo.getDescription() != null && !requestTransferOrderInfo.getDescription().equals("")) {
                        tvReason.setText(requestTransferOrderInfo.getDescription());
                        llReason.setVisibility(View.VISIBLE);
//
                    }
                }
            } else {
                startName = "无";
            }
            String updateData = "";
            if (requestTransferOrderInfo.getUpdateDate() != null) {
                updateData = requestTransferOrderInfo.getUpdateDate();
            } else {
                updateData = "无";
            }
//
            tvId.setText(orderGH.getId());
            tvOrdertime.setText(orderGH.getStartTime());
            tvTel.setText(requestTransferOrderInfo.getTel());
            tvChecktime.setText(updateData);
            tvCheckResult.setText(startName);
            tvName.setText(requestTransferOrderInfo.getName());
            tvNumber.setText(requestTransferOrderInfo.getNumber());
            tvIdCard.setText(requestTransferOrderInfo.getCardId());
            tvAddress.setText(requestTransferOrderInfo.getAddress());
            tvLast0.setText(requestTransferOrderInfo.getNumOne());
            tvLast1.setText(requestTransferOrderInfo.getNumTwo());
            tvLast2.setText(requestTransferOrderInfo.getNumThree());

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.activity_order_gh_detail, null);
            ButterKnife.bind(this, view);
            switch (text) {
                case "tab1":
                    llOrdermsg.setVisibility(View.VISIBLE);
                    break;
                case "tab2":
                    llMoneymsg.setVisibility(View.VISIBLE);
                    break;
                case "tab3":
                    llCustomermsg.setVisibility(View.VISIBLE);
                    break;
            }
            if (requestTransferOrderInfo != null) {
                initData(activity, orderGH, requestTransferOrderInfo);
            }

//
//            BitmapUtil.LoadImageByUrl(OrderGHDetailNewActivity.this, imgAllBefore, requestTransferOrderInfo.getPhotoFour(), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f));
//            BitmapUtil.LoadImageByUrl(OrderGHDetailNewActivity.this, imgCardBefore, requestTransferOrderInfo.getPhotoTwo(), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f));
//            BitmapUtil.LoadImageByUrl(OrderGHDetailNewActivity.this, imgAllNow, requestTransferOrderInfo.getPhotoThree(), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f));
//            BitmapUtil.LoadImageByUrl(OrderGHDetailNewActivity.this, imgCardNow, requestTransferOrderInfo.getPhotoOne(), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f), DisplayUtil.dp2px(OrderGHDetailNewActivity.this, 132f));
//            TextView view = new TextView(OrderGHDetailNewActivity.this);
//            view.setText(text);

            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }
}
