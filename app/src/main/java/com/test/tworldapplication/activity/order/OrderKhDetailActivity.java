package com.test.tworldapplication.activity.order;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostOrderInfo;
import com.test.tworldapplication.entity.RequestOrderInfo;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.CacheUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.POST;

public class OrderKhDetailActivity extends BaseActivity implements SuccessValue<RequestOrderInfo> {

    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.vOrder)
    View vOrder;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.tvExpense)
    TextView tvExpense;
    @BindView(R.id.vExpense)
    View vExpense;
    @BindView(R.id.llExpense)
    LinearLayout llExpense;
    @BindView(R.id.tvCustomer)
    TextView tvCustomer;
    @BindView(R.id.vCustomer)
    View vCustomer;
    @BindView(R.id.llCustomer)
    LinearLayout llCustomer;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvOrdertime)
    TextView tvOrdertime;
    @BindView(R.id.tvChecktime)
    TextView tvChecktime;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvResult)
    TextView tvResult;
    @BindView(R.id.tvPhonn)
    TextView tvPhonn;
    @BindView(R.id.llOrderDetail)
    LinearLayout llOrderDetail;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.tvRegulai)
    TextView tvRegulai;
    @BindView(R.id.tvPackage)
    TextView tvPackage;
    @BindView(R.id.tvDate)
    TextView tvDate;
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
    @BindView(R.id.activity_order_kh_detail)
    LinearLayout activityOrderKhDetail;
    int flag = 0;
    String orderNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_kh_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);
        orderNo = getIntent().getStringExtra("orderNo");
        dialog.getTvTitle().setText("正在获取数据");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
        HttpPost<PostOrderInfo> httpPost = new HttpPost<>();
        PostOrderInfo postOrderInfo = new PostOrderInfo();
        postOrderInfo.setSession_token(Util.getLocalAdmin(OrderKhDetailActivity.this)[0]);
        postOrderInfo.setOrderNo(orderNo);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postOrderInfo);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postOrderInfo) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().orderInfo("", OrderRequest.orderInfo(OrderKhDetailActivity.this, dialog, this), httpPost);
    }

    @OnClick({R.id.llOrder, R.id.llExpense, R.id.llCustomer})
    public void onClick(View view) {
        clearClickState();
        switch (view.getId()) {
            case R.id.llOrder:
                flag = 0;
                tvOrder.setTextColor(getResources().getColor(R.color.colorOrange));
                vOrder.setVisibility(View.VISIBLE);
                break;
            case R.id.llExpense:
                flag = 1;
                tvExpense.setTextColor(getResources().getColor(R.color.colorOrange));
                vExpense.setVisibility(View.VISIBLE);
                break;
            case R.id.llCustomer:
                flag = 2;
                tvCustomer.setTextColor(getResources().getColor(R.color.colorOrange));
                vCustomer.setVisibility(View.VISIBLE);
                break;
        }
        setView();
    }

    public void clearClickState() {
        tvOrder.setTextColor(getResources().getColor(R.color.colorGray1));
        tvExpense.setTextColor(getResources().getColor(R.color.colorGray1));
        tvCustomer.setTextColor(getResources().getColor(R.color.colorGray1));
        vOrder.setVisibility(View.INVISIBLE);
        vExpense.setVisibility(View.INVISIBLE);
        vCustomer.setVisibility(View.INVISIBLE);
    }

    public void setView() {
        switch (flag) {
            case 0:
                llOrderDetail.setVisibility(View.VISIBLE);
                llExpenseDetail.setVisibility(View.GONE);
                llCustomerDetail.setVisibility(View.GONE);
                break;
            case 1:
                llOrderDetail.setVisibility(View.GONE);
                llExpenseDetail.setVisibility(View.VISIBLE);
                llCustomerDetail.setVisibility(View.GONE);
                break;
            case 2:
                llOrderDetail.setVisibility(View.GONE);
                llExpenseDetail.setVisibility(View.GONE);
                llCustomerDetail.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void OnSuccess(RequestOrderInfo value) {
        String state = "";
        String reason = "";
        switch (value.getOrderStatus()) {
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
                reason = "取消原因: " + value.getCancelInfo();
                tvResult.setText(reason);
                tvResult.setVisibility(View.VISIBLE);
                break;
        }
        tvId.setText("订单编号: " + value.getOrderNo());
        tvOrdertime.setText("订单时间: " + value.getCreateDate());
        tvType.setText("开户类型: " + value.getCardType());
        tvState.setText("订单状态: " + state);
        tvChecktime.setText("审核时间: " + value.getUpdateDate());

        tvPhonn.setText("开户号码: " + value.getNumber());
        tvMoney.setText("预存金额: " + value.getPrestore());
        tvActivity.setText("活动包: " + value.getPromotion());
        tvRegulai.setText("靓号规则: " + value.getIsLiang());
        tvPackage.setText("套餐选择: " + value.getPackagename());
        tvDate.setText("起止日期: ");
        tvName.setText("姓名: " + value.getCustomerName());
        tvIdCard.setText("身份证号码: " + Util.strPassword(value.getCertificatesNo()));
        tvAddress.setText("身份证地址: " + value.getAddress());
        BitmapUtil.LoadImageByUrl(OrderKhDetailActivity.this, imgIdAll, value.getPhotoFront(), DisplayUtil.getWindowWidth(OrderKhDetailActivity.this), DisplayUtil.dp2px(OrderKhDetailActivity.this, 224f));
        BitmapUtil.LoadImageByUrl(OrderKhDetailActivity.this, imgIdCard, value.getPhotoBack(), DisplayUtil.getWindowWidth(OrderKhDetailActivity.this), DisplayUtil.dp2px(OrderKhDetailActivity.this, 224f));
    }

}
