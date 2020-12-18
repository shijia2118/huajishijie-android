package com.test.tworldapplication.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.inter.OnOrderQueryListener;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.Util;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 27733 on 2016/10/13.
 */
public class PopView extends PopupWindow {
    Activity context;
    View conentView;
    TextView textView0;
    TextView textView1;
    @BindView(R.id.llBegin)
    LinearLayout llBegin;
    @BindView(R.id.llEnd)
    LinearLayout llEnd;
    @BindView(R.id.llState)
    LinearLayout llState;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.tvInquery)
    TextView tvInquery;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.tvBegin)
    TextView tvBegin;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    int flag = 0;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.llCzState)
    LinearLayout llCzState;
    @BindView(R.id.llPhoneNumber)
    RelativeLayout llPhoneNumber;
    @BindView(R.id.lineCzState)
    View lineCzState;
    @BindView(R.id.tvCzState)
    TextView tvCzState;
    @BindView(R.id.linePhoneNumber)
    View linePhoneNumber;
    List<String> list0Order, list1Order, list2Order, list1Cz;
    static List<String> itemList;
    OnOrderQueryListener mOnOrderQueryListener;
    @BindView(R.id.tvTitleState)
    TextView tvTitleState;
    @BindView(R.id.vLine0)
    View vLine0;
    @BindView(R.id.vLine1)
    View vLine1;

    public PopView(Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.view_pop_condition, null);
        ButterKnife.bind(this, conentView);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        this.setBackgroundDrawable(context.getResources().getDrawable(R.color.colorBackground));
//        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();


    }

    public void setText(String strBegin, String strEnd, String strState, String strNumber, String strType) {
        tvBegin.setText(strBegin);
        tvEnd.setText(strEnd);
        tvState.setText(strState);
        etNumber.setText(strNumber);
        tvCzState.setText(strType);
    }

    public void setList(List<String> list0Order, List<String> list1Order, List<String> list1Cz, List<String> list2Order) {
        this.list0Order = list0Order;
        this.list1Order = list1Order;
        this.list2Order = list2Order;
        this.list1Cz = list1Cz;
    }

    public void setOnOrderQueryListener(OnOrderQueryListener mOnOrderQueryListener) {
        this.mOnOrderQueryListener = mOnOrderQueryListener;
    }

    public void showPopupWindow(int flag, View parent) {
        this.flag = flag;
        switch (flag) {
            case 0:
            case 1:
            case 2:
            case 3:
                tvTitleState.setText("订单状态");
                linePhoneNumber.setVisibility(View.VISIBLE);
                llPhoneNumber.setVisibility(View.VISIBLE);
                lineCzState.setVisibility(View.GONE);
                llCzState.setVisibility(View.GONE);
                break;

            case 4:
                linePhoneNumber.setVisibility(View.GONE);
                llPhoneNumber.setVisibility(View.GONE);
                lineCzState.setVisibility(View.VISIBLE);
                llCzState.setVisibility(View.VISIBLE);
                break;
            case 5:
                linePhoneNumber.setVisibility(View.GONE);
                llPhoneNumber.setVisibility(View.GONE);
                lineCzState.setVisibility(View.GONE);
                llCzState.setVisibility(View.GONE);
                break;
            case 6:
                tvTitleState.setText("业务类型");
                linePhoneNumber.setVisibility(View.VISIBLE);
                llPhoneNumber.setVisibility(View.VISIBLE);
                lineCzState.setVisibility(View.GONE);
                llCzState.setVisibility(View.GONE);
                break;
            case 7:
                tvTitleState.setText("时间段");
                vLine0.setVisibility(View.GONE);
                vLine1.setVisibility(View.GONE);
                linePhoneNumber.setVisibility(View.VISIBLE);
                llPhoneNumber.setVisibility(View.VISIBLE);
                lineCzState.setVisibility(View.GONE);
                llCzState.setVisibility(View.GONE);
                llBegin.setVisibility(View.GONE);
                llEnd.setVisibility(View.GONE);
                break;

        }

        if (!this.isShowing()) {
            this.showAsDropDown(parent,0,0);
            etNumber.clearFocus();
//            this.showAtLocation(parent, Gravity.BOTTOM,0,0);
        } else {
            this.dismiss();
        }
    }

    @OnClick({R.id.llBegin, R.id.llEnd, R.id.llState, R.id.tvInquery, R.id.tvReset, R.id.llCzState})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBegin:
                flag = 0;
                showDatePikerDialog();
                break;
            case R.id.llEnd:
                flag = 1;
                showDatePikerDialog();
                break;
            case R.id.llState:
                switch (flag) {
                    case 0:
                    case 1:
                        Util.createDialog(context, list0Order, tvState, null);
                        break;
                    case 2:
                    case 3:
                        Util.createDialog(context, list2Order, tvState, null);
                        break;
                    case 4:
                        Util.createDialog(context, list1Order, tvState, null);
                        break;
                    case 5:
                        Util.createDialog(context, list0Order, tvState, null);
                        break;
                    case 6:
                        Util.createDialog(context, list0Order, tvState, null);
                        break;
                    case 7:
                        Util.createDialog(context, list0Order, tvState, null);
                        break;
                }
//                if (flag == 4) {
//                    Util.createDialog(context, list1Order, tvState);
//                } else {
//                    Util.createDialog(context, list0Order, tvState);
//                }

                break;
            case R.id.llCzState:
                Util.createDialog(context, list1Cz, tvCzState, null);
                break;
            case R.id.tvInquery:
                String strBegin = tvBegin.getText().toString();
                String strEnd = tvEnd.getText().toString();
                String strState = tvState.getText().toString();
                String strType = tvCzState.getText().toString();
                mOnOrderQueryListener.getOrderCondition(strBegin, strEnd, strState, etNumber.getText().toString(), strType);
                dismiss();
                break;
            case R.id.tvReset:
                tvBegin.setText("请选择");
                tvEnd.setText("请选择");
                if (flag == 6) {
                    tvState.setText("过户");
                } else {
                    tvState.setText("请选择");
                }

                etNumber.setText("");
                tvCzState.setText("话费充值");
                break;

        }
    }

    private void showDatePikerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(context, "选择日期", 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                                  DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                String date = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
                switch (flag) {
                    case 0:
                        tvBegin.setText(date);
                        break;
                    case 1:
                        tvEnd.setText(date);
                        break;
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), true);
        dialog.show();
    }
}
