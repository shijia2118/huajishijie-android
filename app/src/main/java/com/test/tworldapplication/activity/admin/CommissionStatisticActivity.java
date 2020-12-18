package com.test.tworldapplication.activity.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.card.BillQueryActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Commission;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostAdmin;
import com.test.tworldapplication.entity.PostCommissions;
import com.test.tworldapplication.entity.PostIntroduction;
import com.test.tworldapplication.entity.PostStatistic;
import com.test.tworldapplication.entity.RequestAdmin;
import com.test.tworldapplication.entity.RequestCommissions;
import com.test.tworldapplication.entity.RequestIntroduction;
import com.test.tworldapplication.entity.RequestStatistic;
import com.test.tworldapplication.entity.Statistics;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static cn.finalteam.toolsfinal.DateUtils.getTime;

public class CommissionStatisticActivity extends BaseActivity {


    int numbers_state = 0;
    int money_state = 0;
    @BindView(R.id.tvHalf)
    TextView tvHalf;
    @BindView(R.id.vHalfMoney)
    View vHalfMoney;
    @BindView(R.id.llHalfMoney)
    RelativeLayout llHalfMoney;
    @BindView(R.id.vOneMoney)
    View vOneMoney;
    @BindView(R.id.llOneMoney)
    RelativeLayout llOneMoney;
    @BindView(R.id.tvHalf1)
    TextView tvHalf1;
    @BindView(R.id.vHalfNumber)
    View vHalfNumber;
    @BindView(R.id.llHalfNumber)
    RelativeLayout llHalfNumber;
    @BindView(R.id.vOneNumber)
    View vOneNumber;
    @BindView(R.id.llOneNumber)
    RelativeLayout llOneNumber;
    @BindView(R.id.tvCommission)
    TextView tvCommission;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.chartMoney)
    LineChart chartMoney;
    @BindView(R.id.chartNumber)
    LineChart chartNumber;
    LineData moneyData, numberData;



    /*初始化界面获取数据,点击按钮,改变按钮状态  获取数据*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_statistic);
        ButterKnife.bind(this);
        setBackGroundTitle("佣金统计", true, false, false);
        dialog.getTvTitle().setText("正在获取数据");

        //initTimePicker();


    }

    private void initTimePicker() {

        TimePickerView pvTime = new TimePickerBuilder(CommissionStatisticActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
                tvDate.setText(simpleDateFormat.format(date));
            }
        }).setType(new boolean[]{true, true, false, false, false, false})
                .build();
        pvTime.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpPost<PostIntroduction> httpPost = new HttpPost<>();
        PostIntroduction postIntroduction = new PostIntroduction();
        postIntroduction.setSession_token(Util.getLocalAdmin(CommissionStatisticActivity.this)[0]);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postIntroduction);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postIntroduction) + BaseCom.APP_PWD));
        new AdminHttp().introduction(AdminRequest.introduction(CommissionStatisticActivity.this, new SuccessValue<RequestIntroduction>() {
            @Override
            public void OnSuccess(RequestIntroduction value) {
                dialog.show();
                searchMoney();
                searchNumbers();
            }
        }), httpPost);

    }


    @OnClick({R.id.llHalfMoney, R.id.llOneMoney, R.id.llHalfNumber, R.id.llOneNumber, R.id.detail_ll})
    public void onClick(View view) {
//        if (Util.isLog(CommissionStatisticActivity.this)) {
        switch (view.getId()) {
            case R.id.llHalfMoney:
                clearMoneyClick();
                vHalfMoney.setBackgroundResource(R.drawable.shape_checkbox_click);
                money_state = 0;
                dialog.show();
                searchMoney();
                break;
            case R.id.llOneMoney:
                clearMoneyClick();
                vOneMoney.setBackgroundResource(R.drawable.shape_checkbox_click);
                money_state = 1;
                dialog.show();
                searchMoney();
                break;
            case R.id.llHalfNumber:
                clearNumberClick();
                vHalfNumber.setBackgroundResource(R.drawable.shape_checkbox_click);
                numbers_state = 0;
                dialog.show();
                searchNumbers();
                break;
            case R.id.llOneNumber:
                clearNumberClick();
                vOneNumber.setBackgroundResource(R.drawable.shape_checkbox_click);
                numbers_state = 1;
                dialog.show();
                searchNumbers();
                break;
            case R.id.detail_ll:
//                if(TextUtils.isEmpty(tvDate.getText())){
//                    Toast.makeText(this,"请先选择账期",Toast.LENGTH_SHORT).show();
//                }else {
//                    gotoActy(CommissionListActivity.class);
//                }
                Intent intent = new Intent(this, CommissionListActivity.class);
                intent.putExtra("amount",tvCommission.getText());
                startActivity(intent);

                break;
        }
//        }
    }

    private void clearMoneyClick() {
        vHalfMoney.setBackgroundResource(R.drawable.shape_checkbox_noclick);
        vOneMoney.setBackgroundResource(R.drawable.shape_checkbox_noclick);
    }

    private void clearNumberClick() {
        vHalfNumber.setBackgroundResource(R.drawable.shape_checkbox_noclick);
        vOneNumber.setBackgroundResource(R.drawable.shape_checkbox_noclick);
    }

    public void searchNumbers() {
        Log.d("aaa", "money");
        String numbers = "";
        switch (numbers_state) {
            case 0:
                numbers = "month";
                break;
            case 1:
                numbers = "year";
                break;
        }
        HttpPost<PostStatistic> httpPost = new HttpPost<>();
        PostStatistic postStatistic = new PostStatistic();
        postStatistic.setSession_token(Util.getLocalAdmin(CommissionStatisticActivity.this)[0]);
        postStatistic.setDate(numbers);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postStatistic);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postStatistic) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new AdminHttp().statistic(AdminRequest.statistic(CommissionStatisticActivity.this, dialog, new SuccessValue<RequestStatistic>() {
            @Override
            public void OnSuccess(RequestStatistic value) {
                Statistics[] statisticses = value.getStatisticsList();
                Log.d("ccc", statisticses.length + "");
                numberData = Util.getLineData(statisticses.length, 1, numbers_state, statisticses, null);
                Util.showChart(numbers_state, chartNumber, numberData, Color.BLACK);
            }
        }), httpPost);
    }

    public void searchMoney() {
        Log.d("aaa", "money");

        String moneys = "";
        switch (money_state) {
            case 0:
                moneys = "month";
                break;
            case 1:
                moneys = "year";
                break;
        }
        HttpPost<PostCommissions> httpPost = new HttpPost<>();
        PostCommissions postCommissions = new PostCommissions();
        postCommissions.setSession_token(Util.getLocalAdmin(CommissionStatisticActivity.this)[0]);
        postCommissions.setDate(moneys);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postCommissions);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCommissions) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new AdminHttp().commissionsQuery(AdminRequest.commissionsQuery(CommissionStatisticActivity.this, dialog, new SuccessValue<RequestCommissions>() {
            @Override
            public void OnSuccess(RequestCommissions value) {
                Commission[] commissions = value.getCommissionsList();
                Log.d("ccc", commissions.length + "");
                moneyData = Util.getLineData(commissions.length, 0, money_state, null, commissions);
                Util.showChart(money_state, chartMoney, moneyData, Color.BLACK);
                if (money_state == 0) {
                    //tvDate.setText(commissions[commissions.length - 1].getTime());
                    tvCommission.setText(commissions[commissions.length - 1].getCommission() + "元");
                }
            }
        }), httpPost);
    }

}
