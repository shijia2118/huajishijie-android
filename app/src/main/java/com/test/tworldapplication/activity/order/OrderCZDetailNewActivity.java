package com.test.tworldapplication.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.OrderCz;
import com.test.tworldapplication.entity.OrderEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderCZDetailNewActivity extends BaseActivity {
    OrderCz orderCz;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvOrdertime)
    TextView tvOrdertime;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvValue)
    TextView tvValue;
    @BindView(R.id.llName)
    LinearLayout llName;
    @BindView(R.id.llValue)
    LinearLayout llValue;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    String from = "";
    OrderEntity orderEntity;
    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_czdetail_new);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra("from");
        switch (from) {
            case "4":
                setBackGroundTitle("订单详情", true);
                orderEntity = (OrderEntity) getIntent().getSerializableExtra("data");
                index = getIntent().getStringExtra("index");
                break;
            case "5":
                setBackGroundTitle("账户充值详情", true);
                orderCz = (OrderCz) getIntent().getSerializableExtra("data");
                break;
        }


        initView();
    }

    private void initView() {
        switch (from) {
            case "4":
                tvId.setText(orderEntity.getOrderNo());
                tvOrdertime.setText(orderEntity.getCreateDate());
                tvNumber.setText(orderEntity.getNumber());
                tvValue.setText(orderEntity.getOrderAmount() + "元");
                tvName.setText(orderEntity.getProductName());
                tvStatus.setText(orderEntity.getStatusName());
                if (index.equals("0")) {
                    llValue.setVisibility(View.VISIBLE);
                    llName.setVisibility(View.GONE);

                } else {
                    llValue.setVisibility(View.GONE);
                    llName.setVisibility(View.VISIBLE);
                }

                break;
            case "5":
                tvId.setText(orderCz.getOrderNo());
                tvOrdertime.setText(orderCz.getStartTime());
                tvNumber.setText(orderCz.getNumber());
                tvValue.setText(orderCz.getPayAmount() + "元");
                tvStatus.setText(orderCz.getStartName());
                llName.setVisibility(View.GONE);
                break;
        }

    }
}
