package com.test.tworldapplication.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.inter.OnSelectListener;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dasiy on 17/6/8.
 */

public class MyPopWindow extends PopupWindow {
    @BindView(R.id.tvRegular)
    TextView tvRegular;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.llSelection)
    LinearLayout llSelection;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvActivation)
    TextView tvActivation;
    @BindView(R.id.tvUsed)
    TextView tvUsed;
    @BindView(R.id.tvPopBegin)
    TextView tvPopBegin;
    @BindView(R.id.llBegin)
    LinearLayout llBegin;
    @BindView(R.id.tvPopEnd)
    TextView tvPopEnd;
    @BindView(R.id.llEnd)
    LinearLayout llEnd;
    @BindView(R.id.llAddress)
    LinearLayout llAddress;
    @BindView(R.id.llRegular)
    LinearLayout llRegular;
    @BindView(R.id.tvPool)
    TextView tvPool;
    @BindView(R.id.llPool)
    LinearLayout llPool;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.llNumber)
    LinearLayout llNumber;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.tvInquery)
    TextView tvInquery;
    @BindView(R.id.vLine0)
    View vLine0;
    @BindView(R.id.vLine1)
    View vLine1;
    @BindView(R.id.vLine2)
    View vLine2;
    @BindView(R.id.vLine3)
    View vLine3;
    @BindView(R.id.vLine4)
    View vLine4;
    @BindView(R.id.vLine5)
    View vLine5;
    @BindView(R.id.vLine6)
    View vLine6;
    @BindView(R.id.vLine7)
    View vLine7;
    @BindView(R.id.llState)
    LinearLayout llState;
    private Context context;
    private View contentView;
    private LayoutInflater layoutInflater;
    Area area;
    private AlertDialog alertDialogialog;
    private TextView asureTextView, cancelTextView;
    private WheelView provinceWheelView, cityWheelView, countyWheelView;
    private TextView chooseAreaTextView;
    int provinceIndex = -1, cityIndex = -1;
    private String currentProvince = "";
    private String currentCity = "";
    private int regularIndex = 0;
    private int poolIndex = 0;
    private int stateIndex = -1;
    private int selection = 0;
    private String provinceCode = "";
    private String cityCode = "";
    private OnQueryListener onQueryListener;

    private List<String> regularList = new ArrayList<>();
    private List<String> poolList = new ArrayList<>();
    private List<String> stateList = new ArrayList<>();

    public void setOnQueryListener(OnQueryListener onQueryListener) {
        this.onQueryListener = onQueryListener;
    }

    public MyPopWindow(Context context, int flag, List<String> regularList, List<String> poolList, List<String> stateList) {
        super(context);
        this.context = context;
        area = BaseUtils.getArea((Activity) context);
        layoutInflater = LayoutInflater.from(context);
        contentView = layoutInflater.inflate(R.layout.view_my_pop, null);
        ButterKnife.bind(this, contentView);
        this.regularList = regularList;
        this.poolList = poolList;
        this.stateList = poolList;
        switch (flag) {
            case 0:
                /*代理商靓号平台*/
                llSelection.setVisibility(View.VISIBLE);
                vLine0.setVisibility(View.VISIBLE);
                llAddress.setVisibility(View.VISIBLE);
                vLine3.setVisibility(View.VISIBLE);
                llRegular.setVisibility(View.VISIBLE);
                vLine4.setVisibility(View.VISIBLE);
                llNumber.setVisibility(View.VISIBLE);
                vLine7.setVisibility(View.VISIBLE);
//                llBegin.setVisibility(View.GONE);
//                llEnd.setVisibility(View.GONE);
//                vLine1.setVisibility(View.GONE);
//                vLine2.setVisibility(View.GONE);
//                llPool.setVisibility(View.GONE);
//                vLine5.setVisibility(View.GONE);
//                llState.setVisibility(View.GONE);
//                vLine6.setVisibility(View.GONE);
                break;
            case 1:
                /*成卡白卡过户补卡订单列表页*/
                llBegin.setVisibility(View.VISIBLE);
                vLine1.setVisibility(View.VISIBLE);
                llEnd.setVisibility(View.VISIBLE);
                vLine2.setVisibility(View.VISIBLE);
                llState.setVisibility(View.VISIBLE);
                vLine6.setVisibility(View.VISIBLE);
                llNumber.setVisibility(View.VISIBLE);
                vLine7.setVisibility(View.VISIBLE);


                break;
            case 2:
                /*话费充值订单列表页*/
                llBegin.setVisibility(View.VISIBLE);
                vLine1.setVisibility(View.VISIBLE);
                llEnd.setVisibility(View.VISIBLE);
                vLine2.setVisibility(View.VISIBLE);
                llNumber.setVisibility(View.VISIBLE);
                vLine7.setVisibility(View.VISIBLE);


                break;
            case 3:
                /*账户充值订单列表页*/
                llBegin.setVisibility(View.VISIBLE);
                vLine1.setVisibility(View.VISIBLE);
                llEnd.setVisibility(View.VISIBLE);
                vLine2.setVisibility(View.VISIBLE);
                break;
            case 4:
                llRegular.setVisibility(View.VISIBLE);
                vLine4.setVisibility(View.VISIBLE);
                llPool.setVisibility(View.VISIBLE);
                vLine5.setVisibility(View.VISIBLE);
                break;
        }


//        regularList.add("0");
//        regularList.add("1");
//        regularList.add("2");
//        regularList.add("3");
//        regularList.add("4");
//        poolList.add("1");
//        poolList.add("2");
//        poolList.add("3");
//        poolList.add("4");
//        poolList.add("5");
//        stateList.add("0");
//        stateList.add("1");
//        stateList.add("2");
//        stateList.add("3");
//        stateList.add("4");
        contentView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int top = contentView.findViewById(R.id.ll_view_pop_window).getTop();
                int bottom = contentView.findViewById(R.id.ll_view_pop_window).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top || y > bottom) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        //获取屏幕的宽高

        int h = DisplayUtil.getWindowHeight((Activity) context);
        int w = DisplayUtil.getWindowWidth((Activity) context);
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);


    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    private void showChooseAreaDialog_new() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_area, null);
        alertDialogialog = new AlertDialog.Builder(context, R.style.ActionSheetDialogStyle).create();
        alertDialogialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialogialog.show();
        asureTextView = (TextView) view.findViewById(R.id.asure_textview);
        cancelTextView = (TextView) view.findViewById(R.id.cancel_textview);
        chooseAreaTextView = (TextView) view.findViewById(R.id.choose_area_textview);

        provinceWheelView = (WheelView) view.findViewById(R.id.province_wheelview);
        cityWheelView = (WheelView) view.findViewById(R.id.city_wheelview);
        countyWheelView = (WheelView) view.findViewById(R.id.county_wheelview);
        provinceWheelView.setDivideLineColor(context.getResources().getColor(R.color.colorBlue));
        provinceWheelView.setTextFocusColor(context.getResources().getColor(R.color.colorGray));
        provinceWheelView.setTextOutsideColor(context.getResources().getColor(R.color.colorGray6));
        provinceWheelView.setTextSize(14);
//        provinceWheelView.setFontFace(DisplayUtil.getFontFace());
        provinceWheelView.setOffset(4);
        Log.d("bbb", "0");
        provinceWheelView.setTextPadding(0, DisplayUtil.dp2px(context, 5), 0, DisplayUtil.dp2px(context, 5));
        provinceWheelView.setItems(BaseUtils.provinceStrList);
        provinceWheelView.setSeletion(2);
        provinceIndex = 2;
        List<String> provinceStrList = BaseUtils.provinceStrList;
        currentProvince = provinceStrList.get(2);
        provinceWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                provinceIndex = selectedIndex - 4;
                currentProvince = item;
                List<String> c_list = new ArrayList<String>();
                for (int i = 0; i < area.getList().get(provinceIndex).getP_list().size(); i++) {
                    c_list.add(area.getList().get(provinceIndex).getP_list().get(i).getC_name());
                }
                cityWheelView.refresh(c_list);
                cityWheelView.setSeletion(0);
                cityIndex = 0;
                currentCity = c_list.get(0);
            }
        });
        cityWheelView.setDivideLineColor(context.getResources().getColor(R.color.colorBlue));
        cityWheelView.setTextFocusColor(context.getResources().getColor(R.color.colorGray));
        cityWheelView.setTextOutsideColor(context.getResources().getColor(R.color.colorGray6));
        cityWheelView.setTextSize(14);
//        cityWheelView.setFontFace(DisplayUtil.getFontFace());
        cityWheelView.setOffset(4);
        cityWheelView.setTextPadding(0, DisplayUtil.dp2px(context, 5), 0, DisplayUtil.dp2px(context, 5));
        cityWheelView.setItems(BaseUtils.cityStrList);
        cityIndex = 0;
        cityWheelView.setSeletion(0);
        currentCity = BaseUtils.cityStrList.get(0);
        cityWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                cityIndex = selectedIndex - 4;
                currentCity = item;
            }
        });

        asureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissChosseAreaDialog();
                tvAddress.setText(currentProvince + "," + currentCity);
                provinceCode = area.getList().get(provinceIndex).getP_id();
                cityCode = area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id();
                Log.d("aaa", currentProvince);
                Log.d("aaa", provinceIndex + "");
                Log.d("aaa", currentCity);
                Log.d("aaa", cityIndex + "");

            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissChosseAreaDialog();

            }
        });

        alertDialogialog.setCancelable(false);
        alertDialogialog.setCanceledOnTouchOutside(false);
        alertDialogialog.getWindow().setLayout(DisplayUtil.getWindowWidth((Activity) context), WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialogialog.setContentView(view);
    }

    private void dismissChosseAreaDialog() {
        if (alertDialogialog != null && alertDialogialog.isShowing()) {
            alertDialogialog.dismiss();
        }
    }


    @OnClick({R.id.tvAll, R.id.tvActivation, R.id.tvUsed, R.id.llBegin, R.id.llEnd, R.id.llAddress, R.id.llRegular, R.id.llPool, R.id.llState, R.id.tvReset, R.id.tvInquery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAll:
                selection = 0;
                tvAll.setTextColor(context.getResources().getColor(R.color.colorBlue));
                tvAll.setBackgroundResource(R.drawable.shape_recharge);
                tvActivation.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvActivation.setBackgroundResource(R.drawable.shape_recharge_gray);
                tvUsed.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvUsed.setBackgroundResource(R.drawable.shape_recharge_gray);
                break;
            case R.id.tvActivation:
                selection = 1;
                tvActivation.setTextColor(context.getResources().getColor(R.color.colorBlue));
                tvActivation.setBackgroundResource(R.drawable.shape_recharge);
                tvAll.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvAll.setBackgroundResource(R.drawable.shape_recharge_gray);
                tvUsed.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvUsed.setBackgroundResource(R.drawable.shape_recharge_gray);
                break;
            case R.id.tvUsed:
                selection = 2;
                tvUsed.setTextColor(context.getResources().getColor(R.color.colorBlue));
                tvUsed.setBackgroundResource(R.drawable.shape_recharge);
                tvActivation.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvActivation.setBackgroundResource(R.drawable.shape_recharge_gray);
                tvAll.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvAll.setBackgroundResource(R.drawable.shape_recharge_gray);
                break;
            case R.id.llBegin:
                showDatePikerDialog(tvPopBegin);
                break;
            case R.id.llEnd:
                showDatePikerDialog(tvPopEnd);
                break;
            case R.id.llAddress:
                showChooseAreaDialog_new();
                break;
            case R.id.llRegular:
                if (regularList != null && regularList.size() > 0) {
                    Util.createDialog((Activity) context, regularList, tvRegular, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            regularIndex = selection;
                        }
                    });
                }

                break;
            case R.id.llPool:
                if (poolList != null && poolList.size() > 0) {
                    Util.createDialog((Activity) context, poolList, tvPool, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            poolIndex = selection;
                        }
                    });
                }
                break;
            case R.id.llState:
                if (stateList != null && stateList.size() > 0) {
                    Util.createDialog((Activity) context, stateList, tvState, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            stateIndex = selection;
                        }
                    });
                }
                break;
            case R.id.tvReset:
                selection = 0;
                tvAll.setTextColor(context.getResources().getColor(R.color.colorBlue));
                tvAll.setBackgroundResource(R.drawable.shape_recharge);
                tvActivation.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvActivation.setBackgroundResource(R.drawable.shape_recharge_gray);
                tvUsed.setTextColor(context.getResources().getColor(R.color.colorGray5));
                tvUsed.setBackgroundResource(R.drawable.shape_recharge_gray);


                tvPopBegin.setText("请选择");
                tvPopEnd.setText("请选择");
                tvAddress.setText("请选择");
                tvRegular.setText("请选择");
                tvPool.setText("请选择");
                tvState.setText("请选择");
                etNumber.setText("");
                regularIndex = 0;
                poolIndex = 0;
                stateIndex = -1;
                selection = 0;

                provinceIndex = -1;
                cityIndex = -1;
                provinceCode = "";
                cityCode = "";

                break;
            case R.id.tvInquery:
                if (Util.compare(tvPopEnd.getText().toString(), tvPopBegin.getText().toString())) {
                    Util.createToast(context, "请输入正确的起止日期");
                } else
                    onQueryListener.OnQuery(tvPopBegin.getText().toString(), tvPopEnd.getText().toString(), tvAddress.getText().toString(), tvRegular.getText().toString(), tvPool.getText().toString(), etNumber.getText().toString(), regularIndex, poolIndex, stateIndex, selection, provinceCode, cityCode);
                break;
        }
    }

    private void showDatePikerDialog(final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(context, "选择日期", 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                                  DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                String date = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
                textView.setText(date);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), true);
        dialog.show();
    }

    public interface OnQueryListener {
        void OnQuery(String strPopBegin, String strPopEnd, String strAddress, String strRegular, String strPool, String strNumber, int regularIndex, int poolIndex, int stateIndex, int selection, String provinceCode, String cityCode);
    }
}
