package com.test.tworldapplication.activity.order;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostTransferOrderInfo;
import com.test.tworldapplication.entity.RequestTransferOrderInfo;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderGhDetailActivity extends BaseActivity implements SuccessValue<RequestTransferOrderInfo> {
    String orderNo = "";
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvOrdertime)
    TextView tvOrdertime;
    @BindView(R.id.tvTel)
    TextView tvTel;
    @BindView(R.id.tvState)
    TextView tvState;
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
    String time = "";
    @BindView(R.id.ll_ordermsg)
    LinearLayout llOrdermsg;
    @BindView(R.id.ll_moneymsg)
    LinearLayout llMoneymsg;
    @BindView(R.id.ll_customermsg)
    LinearLayout llCustomermsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_gh_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);
        orderNo = getIntent().getStringExtra("orderNo");
        time = getIntent().getStringExtra("time");
        dialog.getTvTitle().setText("正在获取数据");
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (Util.isLog(OrderGhDetailActivity.this)) {
        HttpPost<PostTransferOrderInfo> httpPost = new HttpPost<>();
        PostTransferOrderInfo postTransferOrderInfo = new PostTransferOrderInfo();
        postTransferOrderInfo.setSession_token(Util.getLocalAdmin(OrderGhDetailActivity.this)[0]);
        postTransferOrderInfo.setId(orderNo);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postTransferOrderInfo);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTransferOrderInfo) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().transferOrderInfo(OrderRequest.transferOrderInfo(OrderGhDetailActivity.this, dialog, this), httpPost);
    }

//    }

    @Override
    public void OnSuccess(RequestTransferOrderInfo value) {
        String startName = "";
        if (value.getStartName() != null) {
            startName = value.getStartName();
        } else {
            startName = "无";
        }
        String updateData = "";
        if (value.getUpdateDate() != null) {
            updateData = value.getUpdateDate();
        } else {
            updateData = "无";
        }
        tvId.setText("订单编号: " + orderNo);
        tvOrdertime.setText("订单时间: " + time);
        tvTel.setText(value.getTel());
//        tvState.setText("订单状态: " + startName);
        tvChecktime.setText("审核时间: " + updateData);
        tvCheckResult.setText("审核结果: " + startName);
        tvName.setText("姓名: " + value.getName());
        tvNumber.setText("过户号码: " + value.getNumber());
        tvIdCard.setText("证件号码: " + Util.strPassword(value.getCardId()));
        tvAddress.setText("证件地址: " + value.getAddress());
        tvLast0.setText("近期联系电话1: " + value.getNumOne());
        tvLast1.setText("近期联系电话2: " + value.getNumTwo());
        tvLast2.setText("近期联系电话3: " + value.getNumThree());
        tvReason.setText("取消原因: " + value.getDescription());
        BitmapUtil.LoadImageByUrl(OrderGhDetailActivity.this, imgAllBefore, value.getPhotoFour(), DisplayUtil.getWindowWidth(OrderGhDetailActivity.this), DisplayUtil.dp2px(OrderGhDetailActivity.this, 224f));
        BitmapUtil.LoadImageByUrl(OrderGhDetailActivity.this, imgCardBefore, value.getPhotoTwo(), DisplayUtil.getWindowWidth(OrderGhDetailActivity.this), DisplayUtil.dp2px(OrderGhDetailActivity.this, 224f));
        BitmapUtil.LoadImageByUrl(OrderGhDetailActivity.this, imgAllNow, value.getPhotoThree(), DisplayUtil.getWindowWidth(OrderGhDetailActivity.this), DisplayUtil.dp2px(OrderGhDetailActivity.this, 224f));
        BitmapUtil.LoadImageByUrl(OrderGhDetailActivity.this, imgCardNow, value.getPhotoOne(), DisplayUtil.getWindowWidth(OrderGhDetailActivity.this), DisplayUtil.dp2px(OrderGhDetailActivity.this, 224f));

    }

}
