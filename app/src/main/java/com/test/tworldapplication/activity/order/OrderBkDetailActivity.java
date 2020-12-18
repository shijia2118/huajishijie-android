package com.test.tworldapplication.activity.order;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostRemarkOrderInfo;
import com.test.tworldapplication.entity.RequestRemarkOrderInfo;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderBkDetailActivity extends BaseActivity implements SuccessValue<RequestRemarkOrderInfo> {

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

    String orderNo = "";
    String time = "";


    @BindView(R.id.ll_ordermsg)
    LinearLayout llOrdermsg;
    @BindView(R.id.tvTel)
    TextView tvTel;
    @BindView(R.id.ll_cardmsg)
    LinearLayout llCardmsg;
    @BindView(R.id.ll_postrmsg)
    LinearLayout llPostrmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bk_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);
        orderNo = getIntent().getStringExtra("orderNo");
//        imgIdCard.setVisibility(View.GONE);
        time = getIntent().getStringExtra("time");
        dialog.getTvTitle().setText("正在获取数据");
    }

    @Override
    protected void onResume() {
        super.onResume();

        HttpPost<PostRemarkOrderInfo> httpPost = new HttpPost<>();
        PostRemarkOrderInfo postRemarkOrderInfo = new PostRemarkOrderInfo();
        postRemarkOrderInfo.setSession_token(Util.getLocalAdmin(OrderBkDetailActivity.this)[0]);
        postRemarkOrderInfo.setId(orderNo);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postRemarkOrderInfo);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRemarkOrderInfo) + BaseCom.APP_PWD));
        new OrderHttp().remarkOrderInfo(OrderRequest.remarkOrderInfo(OrderBkDetailActivity.this, dialog, this), httpPost);
    }

    @Override
    public void OnSuccess(RequestRemarkOrderInfo value) {
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
        tvNumber.setText("补卡人号码: " + value.getNumber());
        tvChecktime.setText("审核时间: " + updateData);
        tvCheckResult.setText("审核结果: " + startName);
        tvReason.setText("取消原因: " + value.getDescription());


        tvName.setText("补卡人姓名: " + value.getName());
        tvIdCard.setText("证件号码: " + Util.strPassword(value.getCardId()));
        tvIdAddress.setText("证件地址: " + value.getAddress());
        tvTel.setText("联系电话: " + value.getTel());

        tvLast0.setText("近期联系人电话1:" + value.getNumOne());
        tvLast1.setText("近期联系人电话2:" + value.getNumTwo());
        tvLast2.setText("近期联系人电话3:" + value.getNumThree());

        BitmapUtil.LoadImageByUrl(OrderBkDetailActivity.this, imgIdAll, value.getPhoto(), DisplayUtil.dp2px(OrderBkDetailActivity.this, 132f), DisplayUtil.dp2px(OrderBkDetailActivity.this, 132f));


        tvNameGet.setText("收件人姓名: " + value.getReceiveName());
        tvPhoneGet.setText("收件人号码: " + value.getReceiveTel());
        tvAddress.setText("收件人地址: " + value.getMailingAddress());
        switch (value.getMailMethod()) {
            case "0":
                tvItem.setText("邮寄选项: 顺丰到付");
                break;
            case "1":
                tvItem.setText("邮寄选项: 充100免费邮寄");
                break;
        }

//        BitmapUtil.LoadImageByUrl(OrderBkDetailActivity.this, imgIdCard, value.getPhoto(), DisplayUtil.getWindowWidth(OrderBkDetailActivity.this), DisplayUtil.dp2px(OrderBkDetailActivity.this, 224f));

    }

}
