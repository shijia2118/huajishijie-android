package com.test.tworldapplication.activity.order;

import android.os.Bundle;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.ProductRecord;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductRecordDetailActivity extends BaseActivity {
    ProductRecord productRecord;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvDetail)
    TextView tvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_record_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("订单详情", true);

        productRecord = (ProductRecord) getIntent().getSerializableExtra("data");
        tvName.setText(productRecord.getProdOfferName());
        tvDate.setText(productRecord.getCreateDate());
        tvDetail.setText(productRecord.getProdOfferDesc());
        tvNumber.setText(productRecord.getNumber());
        tvStatus.setText(productRecord.getOrderStatusName());

    }
}
