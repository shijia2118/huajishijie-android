package com.test.tworldapplication.activity.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.OrderAudit;
import com.test.tworldapplication.entity.PostSelectOrderInfo;
import com.test.tworldapplication.entity.PostSendOrCancel;
import com.test.tworldapplication.entity.RequestSelectionOrderInfo;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import wintone.passport.sdk.utils.AppManager;

public class OrderYKHDetailNewActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, SuccessValue<RequestSelectionOrderInfo> {

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
    private OrderAudit orderAudit;
    private
    MyAdapter adapter;
    RequestSelectionOrderInfo requestSelectionOrderInfo;
    String from;//0成卡1白卡
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ykh_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);
        from = getIntent().getStringExtra("from");
        orderAudit = (OrderAudit) getIntent().getSerializableExtra("data");
        dialog.getTvTitle().setText("正在获取数据");
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);


        titles.add(mainTab1);
        titles.add(mainTab2);

        pages.add(new MyFragment("tab1", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConcelDialog().show();
            }
        }));
        pages.add(new MyFragment("tab2", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConcelDialog().show();
            }
        }));


        // create new fragments

        adapter = new MyAdapter(getSupportFragmentManager());
        getData();


    }

    private void getData() {
        Toast.makeText(OrderYKHDetailNewActivity.this, "正在查询数据,请稍后", Toast.LENGTH_SHORT).show();

        HttpPost<PostSelectOrderInfo> httpPost = new HttpPost<>();
        PostSelectOrderInfo postOrderInfo = new PostSelectOrderInfo();
        postOrderInfo.setSession_token(Util.getLocalAdmin(OrderYKHDetailNewActivity.this)[0]);
        postOrderInfo.setId(orderAudit.getId() + "");
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postOrderInfo);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postOrderInfo) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().selectOrderInfo(OrderRequest.selectOrderInfo(OrderYKHDetailNewActivity.this, dialog, this), httpPost);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void OnSuccess(RequestSelectionOrderInfo value) {
        this.requestSelectionOrderInfo = value;
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);
        titles.get(0).setSelected(true);

        initTabline();
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
        @BindView(R.id.ll_action)
        LinearLayout llAction;
        @BindView(R.id.send_tv)
        TextView mSend;
        @BindView(R.id.cancel_tv)
        TextView mCancel;
        View.OnClickListener onClickListener;


        Activity activity;
        RequestSelectionOrderInfo requestSelectionOrderInfo;
        private String text;

        public MyFragment(String text, View.OnClickListener onClickListener) {
            this.text = text;
            this.onClickListener=onClickListener;
        }

//        public void initData(final Activity activity0, RequestSelectionOrderInfo requestSelectionOrderInfo0) {
//            this.activity = activity0;
//            this.requestSelectionOrderInfo = requestSelectionOrderInfo0;
//            String state = "";
////            String reason = "";
//            switch (requestSelectionOrderInfo.getOrderStatusName()) {
//                case "待预审":
//                    state = "待预审";
//                    llAction.setVisibility(View.GONE);
//                    break;
//                case "预审通过":
//                    state = "预审通过";
//                    llAction.setVisibility(View.VISIBLE);
//                    break;
//                case "预审不通过":
//                    state = "预审不通过";
//                    llAction.setVisibility(View.GONE);
//                    if (requestSelectionOrderInfo.getCancelInfo() != null && !requestSelectionOrderInfo.getCancelInfo().equals("")) {
//                        tvResult.setText(requestSelectionOrderInfo.getCancelInfo());
//                        llReason.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case "已发货":
//                    state = "已发货";
//                    llAction.setVisibility(View.GONE);
//                    break;
//                case "已取消":
//                    state = "已取消";
//                    llAction.setVisibility(View.GONE);
//                    if (requestSelectionOrderInfo.getCancelInfo() != null && !requestSelectionOrderInfo.getCancelInfo().equals("")) {
//                        tvResult.setText(requestSelectionOrderInfo.getCancelInfo());
//                        llReason.setVisibility(View.VISIBLE);
//                    }
//                    break;
////                case "CLOSED":
////                    state = "已关闭";
////                    if (requestOrderInfo.getCancelInfo() != null && !requestOrderInfo.getCancelInfo().equals("")) {
////                        tvResult.setText(requestOrderInfo.getCancelInfo());
////                        llReason.setVisibility(View.VISIBLE);
////                    }
////                    break;
//            }
//            String cardType = requestSelectionOrderInfo.getCardType();
////            switch (requestSelectionOrderInfo.getCardType()) {
////                case "SIM":
////                    cardType = "成卡开户";
////                    break;
////            }
//            tvId.setText(requestSelectionOrderInfo.getOrderNo());
//            tvOrdertime.setText(requestSelectionOrderInfo.getStartTime());
//            tvType.setText(cardType);
//            tvState.setText(requestSelectionOrderInfo.getOrderStatusName());
//            tvChecktime.setText(requestSelectionOrderInfo.getUpdateDate());
//
//            tvPhonn.setText(requestSelectionOrderInfo.getNumber());
//            tvMoney.setText(requestSelectionOrderInfo.getPrestore());
//            tvActivity.setText(requestSelectionOrderInfo.getPromotionName());
//            tvRegulai.setText(requestSelectionOrderInfo.getIsLiang());
////            tvPackage.setText("套餐选择: " + requestOrderInfo.getPackagename());
////            tvDate.setText("起止日期: ");
//            tvName.setText(requestSelectionOrderInfo.getCustomerName());
//            tvIdCard.setText(Util.strPassword(requestSelectionOrderInfo.getCertificatesNo()));
//            tvAddress.setText(requestSelectionOrderInfo.getAddress());
////            BitmapUtil.LoadImageByUrl(OrderYKHDetailNewActivity.this, imgIdAll, requestSelectionOrderInfo.getPhotoFront(), DisplayUtil.dp2px(OrderYKHDetailNewActivity.this, 132f), DisplayUtil.dp2px(OrderYKHDetailNewActivity.this, 132f));
////            BitmapUtil.LoadImageByUrl(OrderYKHDetailNewActivity.this, imgIdCard, requestSelectionOrderInfo.getPhotoBack(), DisplayUtil.dp2px(OrderYKHDetailNewActivity.this, 132f), DisplayUtil.dp2px(OrderYKHDetailNewActivity.this, 132f));
//            mSend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HttpPost<PostSendOrCancel> httpPost = new HttpPost<>();
//                    PostSendOrCancel postSendOrCancel = new PostSendOrCancel();
//                    postSendOrCancel.setOrderId(requestSelectionOrderInfo.getId());
//                    postSendOrCancel.setOrderStatus("DELIVERED");
//                    postSendOrCancel.setSession_token(Util.getLocalAdmin(getActivity())[0]);
//                    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
//                    httpPost.setParameter(postSendOrCancel);
//                    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + new Gson().toJson(postSendOrCancel) + BaseCom.APP_PWD));
//
//                    new OrderHttp().sendOrCancel(OrderRequest.sendOrCancel(getActivity(), new SuccessNull() {
//                        @Override
//                        public void onSuccess() {
//                            activity0.finish();
//                        }
//                    }), httpPost);
//                }
//            });
//            mCancel.setOnClickListener(onClickListener);
//        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_kh_detail, null);
            ButterKnife.bind(this, view);
            switch (text) {
                case "tab1":
                    llOrderDetail.setVisibility(View.VISIBLE);
                    llExpenseDetail.setVisibility(View.GONE);
                    break;
                case "tab2":
                    llOrderDetail.setVisibility(View.GONE);
                    llExpenseDetail.setVisibility(View.VISIBLE);
                    break;
            }

            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

        }
    }


    public Dialog onConcelDialog() {
//        mContext = this;
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_cancel_dialog);
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
        final EditText mReason = (EditText) mDialog.findViewById(R.id.reason_et);
        mReason.setSelection(0);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mReason.getText().toString().equals("")) {
                    Toast.makeText(OrderYKHDetailNewActivity.this, "请输入取消原因", Toast.LENGTH_SHORT).show();
                } else {
                    mDialog.dismiss();
                    HttpPost<PostSendOrCancel> httpPost = new HttpPost<>();
                    PostSendOrCancel postSendOrCancel = new PostSendOrCancel();
                    postSendOrCancel.setOrderId(requestSelectionOrderInfo.getId());
                    postSendOrCancel.setOrderDetails(mReason.getText().toString());
                    postSendOrCancel.setOrderStatus("CANCEL");
                    postSendOrCancel.setSession_token(Util.getLocalAdmin(OrderYKHDetailNewActivity.this)[0]);
                    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                    httpPost.setParameter(postSendOrCancel);
                    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postSendOrCancel) + BaseCom.APP_PWD));
                    Log.d("aaa", gson.toJson(httpPost));
                    new OrderHttp().sendOrCancel(OrderRequest.sendOrCancel(OrderYKHDetailNewActivity.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            AppManager.getAppManager().finishActivity();
                        }
                    }), httpPost);
                }
            }
        });
        return mDialog;
    }
}
