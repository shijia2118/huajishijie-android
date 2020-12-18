package com.test.tworldapplication.activity.card;

import android.os.Bundle;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFailedActivity extends BaseActivity {

    @BindView(R.id.tvToast)
    TextView tvToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_failed);
        ButterKnife.bind(this);
        String toast = getIntent().getStringExtra("msg");
        tvToast.setText(toast);
    }
}
