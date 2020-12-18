package com.test.tworldapplication.activity.card;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.NumberAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.WhitePreOpen;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.view.WheelView;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WhitePreOpenActivity extends BaseActivity {
    @BindView(R.id.content_view)
    PullableListView contentView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    @BindView(R.id.vLine)
    View vLine;

    List<WhitePreOpen> list = new ArrayList<>();
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.tvCondition0)
    TextView tvCondition0;
    @BindView(R.id.tvCondition1)
    TextView tvCondition1;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.vLine0)
    View vLine0;

    private AlertDialog alertDialogialog;
    private TextView asureTextView, cancelTextView;
    private WheelView provinceWheelView, cityWheelView, countyWheelView;
    private TextView chooseAreaTextView;
    int provinceIndex = 0, cityIndex = 0;
    PopupWindow mPopupWindow;
    Holder holder;
    private String currentProvince = "";
    private String currentCity = "";
    Area area;
    private String strAddress = "", strNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_pre_open);
        ButterKnife.bind(this);

        setBackGroundTitle("号码列表", true);
        tvCollection.setText("筛选");
        tvCollection.setVisibility(View.VISIBLE);
        area = BaseUtils.getArea(WhitePreOpenActivity.this);
        list.add(new WhitePreOpen("12345678900", "已激活"));
        list.add(new WhitePreOpen("12345678900", "已使用"));

        contentView.setAdapter(new NumberAdapter(WhitePreOpenActivity.this, list));
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WhitePreOpenActivity.this, PhoneDetailActivity.class);
                intent.putExtra("whitepreopen", (Serializable) list.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });


        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_four, null);
        holder = new Holder(music_popunwindwow);

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
    }


    class Holder {
        @BindView(R.id.ll_address)
        LinearLayout llAddress;
        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.tvInquery)
        TextView tvInquery;
        @BindView(R.id.tvReset)
        TextView tvReset;
        @BindView(R.id.tvAddress)
        TextView tvAddress;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.ll_address, R.id.tvReset, R.id.tvInquery})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_address:
                    showChooseAreaDialog_new();
                    break;
                case R.id.tvReset:
                    holder.tvAddress.setText("请选择");
                    holder.etNumber.setText("");
                    break;
                case R.id.tvInquery:
                    strAddress = holder.tvAddress.getText().toString();
                    strNumber = holder.etNumber.getText().toString();
                    mPopupWindow.dismiss();
                    llCondition.setVisibility(View.VISIBLE);
                    vLine0.setVisibility(View.VISIBLE);
                    tvCondition0.setText("筛选条件:" + strAddress);
                    tvCondition1.setText(strNumber);
                    Toast.makeText(WhitePreOpenActivity.this, "....", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    }

    private void dismissChosseAreaDialog() {
        if (alertDialogialog != null && alertDialogialog.isShowing()) {
            alertDialogialog.dismiss();
        }
    }

    private void showChooseAreaDialog_new() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_choose_area, null);
        alertDialogialog = new AlertDialog.Builder(this, R.style.ActionSheetDialogStyle).create();
        alertDialogialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialogialog.show();
        asureTextView = (TextView) view.findViewById(R.id.asure_textview);
        cancelTextView = (TextView) view.findViewById(R.id.cancel_textview);
        chooseAreaTextView = (TextView) view.findViewById(R.id.choose_area_textview);

        provinceWheelView = (WheelView) view.findViewById(R.id.province_wheelview);
        cityWheelView = (WheelView) view.findViewById(R.id.city_wheelview);
        countyWheelView = (WheelView) view.findViewById(R.id.county_wheelview);
        provinceWheelView.setDivideLineColor(getResources().getColor(R.color.colorBlue));
        provinceWheelView.setTextFocusColor(getResources().getColor(R.color.colorGray));
        provinceWheelView.setTextOutsideColor(getResources().getColor(R.color.colorGray6));
        provinceWheelView.setTextSize(14);
//        provinceWheelView.setFontFace(DisplayUtil.getFontFace());
        provinceWheelView.setOffset(4);
        Log.d("bbb", "0");
        provinceWheelView.setTextPadding(0, DisplayUtil.dp2px(this, 5), 0, DisplayUtil.dp2px(this, 5));
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
        cityWheelView.setDivideLineColor(getResources().getColor(R.color.colorBlue));
        cityWheelView.setTextFocusColor(getResources().getColor(R.color.colorGray));
        cityWheelView.setTextOutsideColor(getResources().getColor(R.color.colorGray6));
        cityWheelView.setTextSize(14);
//        cityWheelView.setFontFace(DisplayUtil.getFontFace());
        cityWheelView.setOffset(4);
        cityWheelView.setTextPadding(0, DisplayUtil.dp2px(this, 5), 0, DisplayUtil.dp2px(this, 5));
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
                holder.tvAddress.setText(currentProvince + currentCity);
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
        alertDialogialog.getWindow().setLayout(DisplayUtil.getWindowWidth(this), WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialogialog.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPopupWindow.dismiss();
    }

    @OnClick(R.id.tvCollection)
    public void onClick() {
        mPopupWindow.showAsDropDown(vLine, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                contentView.setAlpha((float) 1);
//                imgArr.setBackgroundResource(R.drawable.arr_down);
            }
        });

    }


}


