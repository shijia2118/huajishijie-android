package com.test.tworldapplication.activity.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostGetSonOrder;
import com.test.tworldapplication.entity.PostGetSonOrderList;
import com.test.tworldapplication.entity.RequestGetSonOrder;
import com.test.tworldapplication.entity.RequestGetSonOrderList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubAccountDetailActivity extends BaseActivity {
    @BindView(R.id.number)
    TextView number;

    @BindView(R.id.orderNo)
    TextView orderNo;

    @BindView(R.id.createDate)
    TextView createDate;

    @BindView(R.id.startName)
    TextView startName;

    @BindView(R.id.acceptUser)
    TextView acceptUser;

    @BindView(R.id.iccid)
    TextView iccid;

    @BindView(R.id.operatorname)
    TextView operatorname;

    @BindView(R.id.network)
    TextView network;

    @BindView(R.id.province)
    TextView province;

    @BindView(R.id.city)
    TextView city;

    @BindView(R.id.packagename)
    TextView packagename;

    @BindView(R.id.promotion)
    TextView promotion;

    @BindView(R.id.cancelInfo)
    TextView cancelInfo;

    @BindView(R.id.pace)
    View pace;

    @BindView(R.id.cancel_ll)
    LinearLayout cancel_ll;


    private String orderNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("子账户开户详情", true);
        orderNum = getIntent().getStringExtra("orderNum");
        initData();

    }

    private void initData() {
        HttpPost<PostGetSonOrder> httpPost = new HttpPost<>();
        PostGetSonOrder postCheck = new PostGetSonOrder();
        postCheck.setSession_token(Util.getLocalAdmin(SubAccountDetailActivity.this)[0]);
        postCheck.setOrderNo(orderNum);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postCheck);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCheck) + BaseCom.APP_PWD));

        new OrderHttp().getSonOrder(OrderRequest.getSonOrder(SubAccountDetailActivity.this, dialog, new SuccessValue<RequestGetSonOrder>() {
            @Override
            public void OnSuccess(RequestGetSonOrder value) {
                if(value.getStartName().equals("已取消")){
                    cancel_ll.setVisibility(View.VISIBLE);
                    pace.setVisibility(View.VISIBLE);
                    cancelInfo.setText(value.getCancelInfo());
                }
                number.setText(value.getNumber());
                orderNo.setText(value.getOrderNo());
                createDate.setText(value.getCreateDate());
                startName.setText(value.getStartName());
                acceptUser.setText(value.getAcceptUser());
                iccid.setText(value.getIccid());
                operatorname.setText(value.getOperatorname());
                network.setText(value.getNetwork());
                province.setText(value.getProvince());
                city.setText(value.getCity());
                packagename.setText(value.getPackagename());  promotion.setText(value.getPromotion());

            }
        }), httpPost);
    }
}
