package com.test.tworldapplication.activity.card;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.RequestAgencyGetBill;
import com.test.tworldapplication.utils.DisplayUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static cn.finalteam.toolsfinal.DateUtils.getTime;

public class BillDetailActivity extends BaseActivity {
    @BindView(R.id.accountTel)
    TextView accountTel;
    @BindView(R.id.billingCycle)
    TextView billingCycle;
    @BindView(R.id.accountName)
    TextView accountName;
    @BindView(R.id.costItem)
    TextView costItem;
    @BindView(R.id.meal)
    TextView meal;
//    @BindView(R.id.calling)
//    TextView calling;
    @BindView(R.id.sum)
    TextView sum;
    @BindView((R.id.basic_ll))
    LinearLayout basic_ll;
    @BindView((R.id.other_ll))
    LinearLayout other_ll;



    private RequestAgencyGetBill requestAgencyGetBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("账单详情", true);
        requestAgencyGetBill = (RequestAgencyGetBill) getIntent().getSerializableExtra("data");
        initData();
    }

    private void initData() {
        accountTel.setText(requestAgencyGetBill.getAccountTel());
        billingCycle.setText(requestAgencyGetBill.getBillingCycle());
        accountName.setText(requestAgencyGetBill.getAccountName());
        meal.setText(requestAgencyGetBill.getMeal());
        sum.setText(requestAgencyGetBill.getSum());
        costItem.setText(requestAgencyGetBill.getCostItem());

        List<RequestAgencyGetBill.BasicBean> basicBeans = requestAgencyGetBill.getBasic();
        for(int i=0;i<basicBeans.size();i++){
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(this, 28));
            linearLayout.setLayoutParams(params);


            TextView textView = new TextView(this);
            textView.setText(basicBeans.get(i).getName());
            textView.setTextSize(16);
            textView.setTextColor(getResources().getColor(R.color.colorGray99));
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(DisplayUtil.dp2px(this,15),0,0,0);
            textView.setLayoutParams(textParams);

            TextView textView2 = new TextView(this);
            textView2.setText(basicBeans.get(i).getValue());
            textView2.setTextSize(16);
            textView2.setTextColor(getResources().getColor(R.color.colorGray99));
            textView2.setGravity(Gravity.END);
            LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams2.weight = 1;
            textParams2.setMargins(DisplayUtil.dp2px(this,6),0,DisplayUtil.dp2px(this,15),0);
            textView2.setLayoutParams(textParams2);

            linearLayout.addView(textView);
            linearLayout.addView(textView2);
            basic_ll.addView(linearLayout);
        }


        List<RequestAgencyGetBill.OthersBean> othersBeans = requestAgencyGetBill.getOthers();
        for(int i=0;i<othersBeans.size();i++){
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(this, 28));
            linearLayout.setLayoutParams(params);


            TextView textView = new TextView(this);
            textView.setText(othersBeans.get(i).getName());
            textView.setTextSize(16);
            textView.setTextColor(getResources().getColor(R.color.colorGray99));
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(DisplayUtil.dp2px(this,15),0,0,0);
            textView.setLayoutParams(textParams);

            TextView textView2 = new TextView(this);
            textView2.setText(othersBeans.get(i).getValue());
            textView2.setTextSize(16);
            textView2.setTextColor(getResources().getColor(R.color.colorGray99));
            textView2.setGravity(Gravity.END);
            LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams2.weight = 1;
            textParams2.setMargins(DisplayUtil.dp2px(this,6),0,DisplayUtil.dp2px(this,15),0);
            textView2.setLayoutParams(textParams2);

            linearLayout.addView(textView);
            linearLayout.addView(textView2);
            other_ll.addView(linearLayout);
        }


    }
}
