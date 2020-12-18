package com.test.tworldapplication.activity.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.test.tworldapplication.entity.PostRemarkOrderInfo;
import com.test.tworldapplication.entity.RequestRemarkOrderInfo;
import com.test.tworldapplication.entity.RequestTransferOrderInfo;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderBKDetailNewActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, SuccessValue<RequestRemarkOrderInfo> {

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
    RequestRemarkOrderInfo requestTransferOrderInfo;
    MyFragment myFragment, myFragment0, myFragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bk_detail_new);
        ButterKnife.bind(this);
        mainTab3.setText("邮寄信息");
        setBackGroundTitle("订单详情", true);
        orderGH = (OrderGH) getIntent().getSerializableExtra("data");
        dialog.getTvTitle().setText("正在获取数据");
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);
        mainTab3.setOnClickListener(this);

        mainTab3.setVisibility(View.VISIBLE);


        titles.add(mainTab1);
        titles.add(mainTab2);
        titles.add(mainTab3);

        myFragment = new MyFragment("tab1");
        myFragment0 = new MyFragment("tab2");
        myFragment1 = new MyFragment("tab3");
        pages.add(myFragment);
        pages.add(myFragment0);
        pages.add(myFragment1);


        // create new fragments

        adapter = new MyAdapter(getSupportFragmentManager());
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);
        titles.get(0).setSelected(true);

        initTabline();

        getData();


    }

    private void getData() {
        Toast.makeText(OrderBKDetailNewActivity.this, "正在查询数据,请稍后", Toast.LENGTH_SHORT).show();

        HttpPost<PostRemarkOrderInfo> httpPost = new HttpPost<>();
        PostRemarkOrderInfo postRemarkOrderInfo = new PostRemarkOrderInfo();
        postRemarkOrderInfo.setSession_token(Util.getLocalAdmin(OrderBKDetailNewActivity.this)[0]);
        postRemarkOrderInfo.setId(orderGH.getId());
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postRemarkOrderInfo);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRemarkOrderInfo) + BaseCom.APP_PWD));
        new OrderHttp().remarkOrderInfo(OrderRequest.remarkOrderInfo(OrderBKDetailNewActivity.this, dialog, this), httpPost);


//        HttpPost<PostTransferOrderInfo> httpPost = new HttpPost<>();
//        PostTransferOrderInfo postTransferOrderInfo = new PostTransferOrderInfo();
//        postTransferOrderInfo.setSession_token(Util.getLocalAdmin(OrderBKDetailNewActivity.this)[0]);
//        postTransferOrderInfo.setId(orderGH.getId());
//        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//        httpPost.setParameter(postTransferOrderInfo);
//        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTransferOrderInfo) + BaseCom.APP_PWD));
//        Log.d("aaa", gson.toJson(httpPost));
//        new OrderHttp().transferOrderInfo(OrderRequest.transferOrderInfo(OrderBKDetailNewActivity.this, dialog, this), httpPost);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void OnSuccess(RequestRemarkOrderInfo value) {
        this.requestTransferOrderInfo = value;
        myFragment.initData(OrderBKDetailNewActivity.this, orderGH, value);
        myFragment0.initData(OrderBKDetailNewActivity.this, orderGH, value);
        myFragment1.initData(OrderBKDetailNewActivity.this, orderGH, value);
    }


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
        @BindView(R.id.tvId)
        TextView tvId;
        @BindView(R.id.tvOrdertime)
        TextView tvOrdertime;
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
        @BindView(R.id.tvIdAddress)
        TextView tvIdAddress;
        @BindView(R.id.tvNameGet)
        TextView tvNameGet;
        @BindView(R.id.tvPhoneGet)
        TextView tvPhoneGet;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvItem)
        TextView tvItem;
        @BindView(R.id.imgIdAll)
        ImageView imgIdAll;
        @BindView(R.id.tvLast0)
        TextView tvLast0;
        @BindView(R.id.tvLast1)
        TextView tvLast1;
        @BindView(R.id.tvLast2)
        TextView tvLast2;
        @BindView(R.id.tvReason)
        TextView tvReason;
        @BindView(R.id.ll_ordermsg)
        LinearLayout llOrdermsg;
        @BindView(R.id.tvTel)
        TextView tvTel;
        @BindView(R.id.ll_cardmsg)
        LinearLayout llCardmsg;
        @BindView(R.id.ll_postrmsg)
        LinearLayout llPostrmsg;
        @BindView(R.id.ll_reason)
        LinearLayout llReason;
        private String text = "";
        Activity activity;
        RequestRemarkOrderInfo requestTransferOrderInfo;
        OrderGH orderGH;

        public MyFragment(String text) {
            this.text = text;
        }

        public void initData(Activity activity, OrderGH orderGH, RequestRemarkOrderInfo requestTransferOrderInfo) {
            this.activity = activity;
            this.orderGH = orderGH;
            this.requestTransferOrderInfo = requestTransferOrderInfo;
            String startName = "";
            if (requestTransferOrderInfo.getStartName() != null) {
                startName = requestTransferOrderInfo.getStartName();
                if (requestTransferOrderInfo.getStartName().equals("审核不通过")) {
                    if (requestTransferOrderInfo.getDescription() != null && !requestTransferOrderInfo.getDescription().equals("")) {
                        llReason.setVisibility(View.VISIBLE);
                        tvReason.setText(requestTransferOrderInfo.getDescription());

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
            tvId.setText(orderGH.getId());
            tvOrdertime.setText(orderGH.getStartTime());
            tvNumber.setText(requestTransferOrderInfo.getNumber());
            tvChecktime.setText(updateData);
            tvCheckResult.setText(startName);


            tvName.setText(requestTransferOrderInfo.getName());
            tvIdCard.setText(requestTransferOrderInfo.getCardId());
            tvIdAddress.setText(requestTransferOrderInfo.getAddress());
            tvTel.setText(requestTransferOrderInfo.getTel());

            tvLast0.setText(requestTransferOrderInfo.getNumOne());
            tvLast1.setText(requestTransferOrderInfo.getNumTwo());
            tvLast2.setText(requestTransferOrderInfo.getNumThree());

            BitmapUtil.LoadImageByUrl(activity, imgIdAll, requestTransferOrderInfo.getPhoto(), DisplayUtil.dp2px(activity, 132f), DisplayUtil.dp2px(activity, 132f));


            tvNameGet.setText(requestTransferOrderInfo.getReceiveName());
            tvPhoneGet.setText(requestTransferOrderInfo.getReceiveTel());
            tvAddress.setText(requestTransferOrderInfo.getMailingAddress());
            tvItem.setText(requestTransferOrderInfo.getMailMethod());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.activity_order_bk_detail, null);
            ButterKnife.bind(this, view);
            switch (text) {
                case "tab1":
                    llOrdermsg.setVisibility(View.VISIBLE);
                    break;
                case "tab2":
                    llCardmsg.setVisibility(View.VISIBLE);
                    break;
                case "tab3":
                    llPostrmsg.setVisibility(View.VISIBLE);
                    break;
            }
            if (requestTransferOrderInfo != null) {
                initData(activity, orderGH, requestTransferOrderInfo);
            }

//            switch (requestTransferOrderInfo.getMailMethod()) {
//                case "0":
//                    tvItem.setText("顺丰到付");
//                    break;
//                case "1":
//                    tvItem.setText("充100免费邮寄");
//                    break;
//            }
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

        }
    }
}
