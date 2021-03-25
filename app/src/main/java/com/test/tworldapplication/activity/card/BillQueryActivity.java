package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.SubAccountActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostAgencyGetBill;
import com.test.tworldapplication.entity.PostGetSonOrderList;
import com.test.tworldapplication.entity.RequestAgencyGetBill;
import com.test.tworldapplication.entity.RequestGetSonOrderList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static cn.finalteam.toolsfinal.DateUtils.getTime;

public class BillQueryActivity extends BaseActivity{
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvQuery)
    TextView tvQuery;

    private String dateStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_query);
        ButterKnife.bind(this);
        setBackGroundTitle("账单查询", true);

    }


    @OnClick({R.id.tvQuery, R.id.tvDay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvQuery:

                HttpPost<PostAgencyGetBill> httpPost = new HttpPost<>();
                PostAgencyGetBill postCheck = new PostAgencyGetBill();
                postCheck.setTel(etPhone.getText().toString());
                postCheck.setAccountPeriod(dateStr);
                postCheck.setSession_token(Util.getLocalAdmin(BillQueryActivity.this)[0]);

                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost.setParameter(postCheck);
                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCheck) + BaseCom.APP_PWD));
                new OrderHttp().agencyGetBill(OrderRequest.agencyGetBill(BillQueryActivity.this, dialog, new SuccessValue<RequestAgencyGetBill>() {
                    @Override
                    public void OnSuccess(RequestAgencyGetBill value) {
                        Intent intent = new Intent(BillQueryActivity.this, BillDetailActivity.class);
                        intent.putExtra("data", value);
                        startActivity(intent);
                    }

                }), httpPost);

                break;
            case R.id.tvDay:
                selectBillDay();
                break;
        }
    }


    private void selectBillDay(){
        Calendar calendar0 = Calendar.getInstance();
        calendar0.add(Calendar.MONTH, -3);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        TimePickerView pvTime = new TimePickerBuilder(BillQueryActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMM", Locale.CHINA);
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
                dateStr = format.format(date);
                tvDay.setText(format2.format(date));

            }
        }).setType(new boolean[]{true, true, false, false, false, false})
                .setRangDate(calendar0, calendar)
                .build();
        pvTime.show();
    }

}
