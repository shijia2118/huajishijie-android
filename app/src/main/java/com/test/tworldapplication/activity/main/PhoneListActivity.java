package com.test.tworldapplication.activity.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.PhoneAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangRule;
import com.test.tworldapplication.entity.Phone;
import com.test.tworldapplication.entity.PostCuteRegular;
import com.test.tworldapplication.entity.PostPreRandom;
import com.test.tworldapplication.entity.PreRandomNumberNew;
import com.test.tworldapplication.entity.RequestLiang;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.inter.OnSelectListener;
import com.test.tworldapplication.inter.RecyclerItemClickListener;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

public class PhoneListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSize)
    TextView tvSize;
    @BindView(R.id.vLine)
    View vLine;
    PhoneAdapter adapter;
    List<Phone> phones = new ArrayList<>();
    List<LiangRule> numberPoolNews = new ArrayList<>();
    List<String> strRegulars = new ArrayList<>();
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.shadow_view)
    View shadowView;
    ViewHolder viewHolder;
    PopupWindow mPopupWindow;
    private AlertDialog alertDialogialog;
    private TextView asureTextView, cancelTextView;
    private WheelView provinceWheelView, cityWheelView, countyWheelView;
    private TextView chooseAreaTextView;
    int provinceIndex = 0, cityIndex = 0;
    private String currentProvince = "";
    private String currentCity = "";
    Area area;
    Integer seletionRegular = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_list);
        ButterKnife.bind(this);
        tvTitle.setText("写卡激活号码");
        tvCollection.setText("筛选");
        tvSize.setText("0");
        tvCollection.setVisibility(View.VISIBLE);
        area = BaseUtils.getArea(PhoneListActivity.this);
        adapter = new PhoneAdapter(phones, PhoneListActivity.this);
        adapter.setListener(new RecyclerItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llContent:
                        if (!phones.get(position).getNumber().equals("")) {
                            Intent intent = new Intent(PhoneListActivity.this, PhoneDetailActivity.class);
//                        Intent intent = new Intent(PhoneListActivity.this, SelectActivity.class);
                            intent.putExtra("number", phones.get(position).getNumber());
                            intent.putExtra("from", "2");
                            intent.putExtra("type", "1");
                            startActivity(intent);
                        }

                        break;
                }
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(PhoneListActivity.this, 2));
        recyclerView.setAdapter(adapter);
        initView();
        getWhiteNumberPool();
        search();

    }

    private void getWhiteNumberPool() {
        HttpPost<PostCuteRegular> httpPost = new HttpPost();
        PostCuteRegular postSessionToken = new PostCuteRegular();
        postSessionToken.setSession_token(Util.getLocalAdmin(PhoneListActivity.this)[0]);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postSessionToken);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postSessionToken) + BaseCom.APP_PWD));
        new CardHttp().getLiangRule_list(httpPost, new Subscriber<HttpRequest<RequestLiang>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLiang> requestLiangHttpRequest) {
                if (requestLiangHttpRequest.getCode() == BaseCom.NORMAL) {
                    numberPoolNews.clear();
                    numberPoolNews.addAll(requestLiangHttpRequest.getData().getLiangRule());
                    strRegulars.clear();
                    for (int i = 0; i < numberPoolNews.size(); i++) {
                        strRegulars.add(numberPoolNews.get(i).getRuleName());
                    }
                } else if (requestLiangHttpRequest.getCode() == BaseCom.LOSELOG | requestLiangHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(PhoneListActivity.this, LoginActivity.class);
                else
                    Toast.makeText(PhoneListActivity.this, requestLiangHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        LayoutInflater mLayoutInflater = (LayoutInflater) PhoneListActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_nine, null);
        viewHolder = new ViewHolder(music_popunwindwow);
        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
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
                String address = "";
                if (provinceIndex == 1 || provinceIndex == 2 || provinceIndex == 6 || provinceIndex == 8 || provinceIndex == 12 || provinceIndex == 22)
                    address = currentCity;
                else
                    address = currentProvince + "," + currentCity;
                viewHolder.tvNumberPop.setText(address);

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

    private void dismissChosseAreaDialog() {
        if (alertDialogialog != null && alertDialogialog.isShowing()) {
            alertDialogialog.dismiss();
        }
    }

    @OnClick({R.id.imgBack, R.id.tvCollection, R.id.llRefresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tvCollection:
                shadowView.setVisibility(View.VISIBLE);
//        contentView.setAlpha((float) 0.2);
                mPopupWindow.showAsDropDown(vLine, 0, 0);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                contentView.setAlpha((float) 1);
                        shadowView.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.llRefresh:
                search();
                if (strRegulars.size() == 0)
                    getWhiteNumberPool();
                break;
        }

    }

    private void search() {
        HttpPost<PostPreRandom> httpPost = new HttpPost();
        PostPreRandom postPreRandom = new PostPreRandom();
        postPreRandom.setSession_token(Util.getLocalAdmin(PhoneListActivity.this)[0]);
        String province = viewHolder.tvNumberPop.getText().toString();
        if (!province.equals("请选择")) {
            postPreRandom.setProvince(area.getList().get(provinceIndex).getP_id());
            if (province.split(",").length == 2)
                postPreRandom.setCity(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
        }
        String regular = viewHolder.tvRegularPop.getText().toString();
        if (!regular.equals("请选择"))
            postPreRandom.setNumberRule(numberPoolNews.get(seletionRegular).getId() + "");
        if (!viewHolder.etPhone.getText().toString().equals(""))
            postPreRandom.setNumber(viewHolder.etPhone.getText().toString());

        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPreRandom);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPreRandom) + BaseCom.APP_PWD));
        new OrderHttp().preRandomNumber(new Subscriber<HttpRequest<PreRandomNumberNew>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<PreRandomNumberNew> getNumbersHttpRequest) {
                mPopupWindow.dismiss();
                if (getNumbersHttpRequest.getCode() == BaseCom.NORMAL) {
                    phones.clear();
                    phones.addAll(getNumbersHttpRequest.getData().getNumbers());
                    tvSize.setText(getNumbersHttpRequest.getData().getCount() + "");
                    if (phones.size() % 2 == 1) {
                        phones.add(new Phone(""));
                    }
                    adapter.notifyDataSetChanged();

                } else if (getNumbersHttpRequest.getCode() == BaseCom.LOSELOG | getNumbersHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(PhoneListActivity.this, LoginActivity.class);
                else
                    Toast.makeText(PhoneListActivity.this, getNumbersHttpRequest.getMes() + "", Toast.LENGTH_SHORT).show();


            }
        }, httpPost);
    }

    class ViewHolder {
        @BindView(R.id.tvNumberPop)
        TextView tvNumberPop;
        @BindView(R.id.llProvince)
        LinearLayout llNumber;
        @BindView(R.id.tvRegularPop)
        TextView tvRegularPop;
        @BindView(R.id.llRegular)
        LinearLayout llRegular;
        @BindView(R.id.tvInquery)
        TextView tvInquery;
        @BindView(R.id.tvReset)
        TextView tvReset;
        @BindView(R.id.etPhone)
        TextView etPhone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.llProvince, R.id.llRegular, R.id.tvInquery, R.id.tvReset})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llProvince:
                    showChooseAreaDialog_new();

                    break;
                case R.id.llRegular:
                    if (strRegulars.size() > 0)
                        Util.createDialog(PhoneListActivity.this, strRegulars, tvRegularPop, new OnSelectListener() {
                            @Override
                            public void onSelect(int selection) {
                                seletionRegular = selection;
                            }
                        });
                    else
                        Toast.makeText(PhoneListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tvInquery:
//                    if (numberPools.size() > 0)
//                        tvNumber.setText("号码池:" + Util.strBackNull(numberPools.get(seletionPool).getName()));
//                    if (numberTypes.size() > 0)
//                        tvRegular.setText("靓号规则:" + Util.strBackNull(numberTypes.get(seletionRegular).getRuleName()));
//                    llCondition.setVisibility(View.GONE);
//                    shadowView.setVisibility(View.GONE);
//                    mPopupWindow.dismiss();
                    search();
                    break;
                case R.id.tvReset:
                    tvNumberPop.setText("请选择");
                    seletionRegular = 0;
                    etPhone.setText("");
                    tvRegularPop.setText("请选择");

                    break;
            }
        }
    }
}
