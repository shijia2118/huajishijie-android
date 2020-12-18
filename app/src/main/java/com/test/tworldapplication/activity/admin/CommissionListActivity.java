package com.test.tworldapplication.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.tabs.TabLayout;
import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.CommissionListAdapter;
import com.test.tworldapplication.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommissionListActivity extends BaseActivity {
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.comm_list_tab)
    TabLayout comm_list_tab;
    @BindView(R.id.comm_list_vp)
    ViewPager comm_list_vp;
    @BindView(R.id.amount)
    TextView amount;
    public String dateStr;
    CommissionListAdapter adapter;
    SimpleDateFormat format = new SimpleDateFormat("yyyyMM", Locale.CHINA);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_list);
        ButterKnife.bind(this);
        setBackGroundTitle("我的佣金", true);
        tvCollection.setText("筛选");
        tvCollection.setVisibility(View.VISIBLE);
       // amount.setText(getIntent().getStringExtra("amount"));
        setVp();
        
    }

    private void setVp() {
        adapter = new CommissionListAdapter(getSupportFragmentManager(),"发展佣金","奖励佣金","扣罚佣金");
        comm_list_vp.setAdapter(adapter);
        comm_list_vp.setOffscreenPageLimit(8);
        comm_list_tab.setupWithViewPager(comm_list_vp);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.tvCollection)
    public void onClick() {
        Calendar calendar0 = Calendar.getInstance();
        calendar0.add(Calendar.MONTH, -3);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        TimePickerView pvTime = new TimePickerBuilder(CommissionListActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                dateStr = format.format(calendar.getTime());

//                for(int i=0;i<3;i++) {
//                    CommissionFragment fragment = (CommissionFragment) adapter.getItem(i);
//                    if(fragment!=null){
//                        fragment.page = 1;
//                    }
//                }

                adapter.getCurrentFragment().getData();
                adapter.getCurrentFragment().setSum();
            }
        }).setType(new boolean[]{true, true, false, false, false, false})
                .setRangDate(calendar0, calendar)
                .build();
        pvTime.show();

    }

    public void setSum(String text){
        amount.setText(text);
    }

}
