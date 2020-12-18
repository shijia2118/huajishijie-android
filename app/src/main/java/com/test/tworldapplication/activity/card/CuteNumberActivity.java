package com.test.tworldapplication.activity.card;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.TextActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangAgent;
import com.test.tworldapplication.entity.LiangRule;
import com.test.tworldapplication.entity.PostAgentsLiang;
import com.test.tworldapplication.entity.PostCuteRegular;
import com.test.tworldapplication.entity.RequestLiang;
import com.test.tworldapplication.entity.RequestLiangAgents;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.inter.OnSelectListener;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.MyPopWindow;
import com.test.tworldapplication.view.WheelView;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class CuteNumberActivity extends BaseActivity {

    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.vLine0)
    View vLine0;
    //    @BindView(R.id.content_view)
//    PullableListView contentView;
    //    @BindView(R.id.refresh_view)
//    PullToRefreshLayout refreshView;
    List<LiangAgent> list = new ArrayList<>();
    PopupWindow mPopupWindow;
    Holder holder;
    @BindView(R.id.vLine)
    View vLine;
    int selection = 0;
    //    @BindView(R.id.tvCondition0)
//    TextView tvCondition0;
    @BindView(R.id.tvCondition1)
    TextView tvCondition1;
    @BindView(R.id.tvCondition)
    TextView tvCondition;
    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.shadow_view)
    View shadowView;
    String filterNumber = "";
    String filterStatus = "-1";//0全部1已激活2已使用
    private String strAddress = "", strRegular = "";
    Area area;


//    @BindView(R.id.refresh_view)
//    PullToRefreshLayout refreshView;


    private int page = 1, linage = 10;
    int refresh = 0;
    //    PullToRefreshLayout pullToRefreshLayout;
    MyAdapter myAdapter;

    private List<String> regularList = new ArrayList<>();
    private List<LiangRule> liangRuleList = new ArrayList<>();


    private AlertDialog alertDialogialog;
    private TextView asureTextView, cancelTextView;
    private WheelView provinceWheelView, cityWheelView, countyWheelView;
    private TextView chooseAreaTextView;
    int provinceIndex = 0, cityIndex = 0;
    private String currentProvince = "";
    private String currentCity = "";
    int first = 0;
    int regularIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cute_number);
        ButterKnife.bind(this);
        setBackGroundTitle("靓号平台", true);
        tvCollection.setText("筛选");
        tvCollection.setVisibility(View.VISIBLE);
        area = BaseUtils.getArea(CuteNumberActivity.this);
        getRegularList();
//        list.add(new WhitePreOpen("12345678900", "已激活"));
//        list.add(new WhitePreOpen("12345678900", "已使用"));

        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refresh = 1;
                getDailiData();
            }

            @Override
            public void onLoadmore() {
                page++;
                refresh = 2;
                getDailiData();
            }
        });
        springView.setHeader(new DefaultHeader(CuteNumberActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultFooter(CuteNumberActivity.this));


        myAdapter = new MyAdapter(CuteNumberActivity.this, list);
        contentView.setAdapter(myAdapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CuteNumberActivity.this, CutePhoneDetailActivity.class);
                intent.putExtra("data", list.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });


        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_five, null);
        holder = new Holder(music_popunwindwow);

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh = 0;

//        if (first != 0)
//            getDailiData();
        first++;

    }

    private void getRegularList() {
        regularList.clear();

        PostCuteRegular postCuteRegular = new PostCuteRegular();
        postCuteRegular.setSession_token(Util.getLocalAdmin(CuteNumberActivity.this)[0]);
        HttpPost<PostCuteRegular> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCuteRegular) + BaseCom.APP_PWD));
        httpPost.setParameter(postCuteRegular);
        Log.d("aaa", gson.toJson(httpPost));
        new CardHttp().getLiangRule_list(httpPost, new Subscriber<HttpRequest<RequestLiang>>() {
            @Override
            public void onCompleted() {
                getDailiData();


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLiang> requestLiangHttpRequest) {
                liangRuleList.clear();
                regularList.clear();
                liangRuleList.addAll(requestLiangHttpRequest.getData().getLiangRule());
                for (int i = 0; i < liangRuleList.size(); i++) {
                    regularList.add(liangRuleList.get(i).getRuleName());
                }
            }
        });


//        PostCuteRegular postCuteRegular = new PostCuteRegular();
//        postCuteRegular.setSession_token(Util.getLocalAdmin(CuteNumberActivity.this)[0]);
//        HttpPost<PostCuteRegular> httpPost = new HttpPost<>();
//        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
//        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCuteRegular) + BaseCom.APP_PWD));
//        httpPost.setParameter(postCuteRegular);
//        Log.d("aaa", gson.toJson(httpPost));
//        new CardHttp().getLiangRule(httpPost, new Subscriber<Object>() {
//            @Override
//            public void onCompleted() {
////                if (first == 0)
//                getDailiData();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//                LiangRule liangRule = (LiangRule) o;
//                regularList.add(liangRule.getRuleName());
//                liangRuleList.add(liangRule);
//            }
//        });


    }

//
//    public void initPop() {
//        myPopuWindow = new MyPopWindow(CuteNumberActivity.this, 0, regularList, null, null);
//        myPopuWindow.setOnQueryListener(new MyPopWindow.OnQueryListener() {
//            @Override
//            public void OnQuery(String strPopBegin, String strPopEnd, String strAddress, String strRegular, String strPool, String strNumber, int regularIndex, int poolIndex, int stateIndex, int selection, String provinceCode, String cityCode) {
//                Log.d("aaa", strPopBegin);
//                Log.d("aaa", strPopEnd);
//                Log.d("aaa", strAddress);
//                Log.d("aaa", strRegular);
//                Log.d("aaa", strPool);
//                Log.d("aaa", strNumber);
//                Log.d("aaa", regularIndex + "");
//                Log.d("aaa", poolIndex + "");
//                Log.d("aaa", stateIndex + "");
//                Log.d("aaa", selection + "");
//                Log.d("aaa", provinceCode);
//                Log.d("aaa", cityCode);
//                this.strAddress =
//            }
//        });
//
//    }

    @OnClick(R.id.tvCollection)
    public void onClick() {
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
    }

    private void finishGetData() {
        switch (refresh) {
            case 0:
                list.clear();
                break;
            case 1:
                list.clear();
                springView.onFinishFreshAndLoad();
                break;
            case 2:
                springView.onFinishFreshAndLoad();
                break;
        }
        refresh = 0;


    }

    private void getDailiData() {
        PostAgentsLiang postAgentsLiang = new PostAgentsLiang();
        postAgentsLiang.setSession_token(Util.getLocalAdmin(CuteNumberActivity.this)[0]);
        postAgentsLiang.setPage(String.valueOf(page));
        postAgentsLiang.setLinage(String.valueOf(linage));
        if (!filterNumber.equals(""))
            postAgentsLiang.setNumber(filterNumber);
        postAgentsLiang.setLiangStatus(filterStatus);
        if (!strAddress.equals("请选择") && !strAddress.equals("")) {
            postAgentsLiang.setProvinceCode(area.getList().get(provinceIndex).getP_id());
            postAgentsLiang.setCityCode(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
        }
        postAgentsLiang.setLiangRuleId(liangRuleList.get(regularIndex).getId());

//        if (!strAddress.equals("请选择") && !strAddress.equals("")) {
//            postAgentsLiang.setProvinceCode(area.getList().get(provinceIndex).getP_id());
//            postAgentsLiang.setCityCode(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
//        }
//        if (!strRegular.equals("请选择") && !strRegular.equals(""))
//            postAgentsLiang.setLiangRuleId(liangRuleList.get(regularIndex).getId());

        HttpPost<PostAgentsLiang> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postAgentsLiang);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postAgentsLiang) + BaseCom.APP_PWD));
        new CardHttp().getAgentsLiangList(httpPost, new Subscriber<HttpRequest<RequestLiangAgents>>() {
            @Override
            public void onCompleted() {
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLiangAgents> requestLiangAgentsHttpRequest) {
                Util.createToast(CuteNumberActivity.this, requestLiangAgentsHttpRequest.getMes());
                if (requestLiangAgentsHttpRequest.getCode() == BaseCom.NORMAL) {
                    finishGetData();


//                    tvCondition.setText(requestLiangAgentsHttpRequest.getData().getCount() + "");
//
//
// tvCondition0.setText("筛选条件:");
//                    tvCondition1.setText("全部");
                    list.addAll(requestLiangAgentsHttpRequest.getData().getLiangAgents());
                    SpannableString title = new SpannableString("共" + list.size() + "条");
                    title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(list.size()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvCondition.setText(title);
                } else if (requestLiangAgentsHttpRequest.getCode() == BaseCom.LOSELOG || requestLiangAgentsHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(CuteNumberActivity.this, LoginActivity.class);
                }
            }
        });


    }


    class Holder {
        @BindView(R.id.tvAll)
        TextView tvAll;
        @BindView(R.id.tvActivation)
        TextView tvActivation;
        @BindView(R.id.tvUsed)
        TextView tvUsed;
        @BindView(R.id.etNumber)
        EditText etNumber;


        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.ll_address)
        LinearLayout llAddress;
        @BindView(R.id.tvRegular)
        TextView tvRegular;
        @BindView(R.id.ll_regular)
        LinearLayout llRegular;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.tvAll, R.id.tvActivation, R.id.tvUsed, R.id.tvInquery, R.id.tvReset, R.id.ll_address, R.id.ll_regular})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvAll:
                    selection = 0;
                    tvAll.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge);
                    tvActivation.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvActivation.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvUsed.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvUsed.setBackgroundResource(R.drawable.shape_recharge_gray);
                    break;
                case R.id.tvActivation:
                    selection = 1;
                    tvActivation.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvActivation.setBackgroundResource(R.drawable.shape_recharge);
                    tvAll.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvUsed.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvUsed.setBackgroundResource(R.drawable.shape_recharge_gray);
                    break;
                case R.id.tvUsed:
                    selection = 2;
                    tvUsed.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvUsed.setBackgroundResource(R.drawable.shape_recharge);
                    tvActivation.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvActivation.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvAll.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge_gray);
                    break;
                case R.id.ll_address:
                    showChooseAreaDialog_new();
                    break;
                case R.id.ll_regular:
                    Util.createDialog(CuteNumberActivity.this, regularList, tvRegular, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            regularIndex = selection;
                        }
                    });
                    break;
                case R.id.tvInquery:
                    mPopupWindow.dismiss();
                    llCondition.setVisibility(View.VISIBLE);
                    vLine0.setVisibility(View.VISIBLE);
                    page = 1;
                    String filter = "";
                    switch (selection) {
                        case 0:
                            filter += "全部";
                            filterStatus = "-1";
                            break;
                        case 1:
                            filter += "已激活";
                            filterStatus = "1";
                            break;
                        case 2:
                            filter += "已使用";
                            filterStatus = "2";
                            break;
                    }

                    if (!tvAddress.getText().toString().equals("") && !tvAddress.getText().toString().equals("请选择")) {
                        filter += "  " + tvAddress.getText().toString();
                        strAddress = tvAddress.getText().toString();
                    }
                    if (!tvRegular.getText().toString().equals("") && !tvRegular.getText().toString().equals("请选择")) {
                        filter += "  " + tvRegular.getText().toString();
                        strRegular = tvRegular.getText().toString();
                    }


                    filterNumber = etNumber.getText().toString();
                    String s = "筛选条件:  " + filter + "  ";
                    SpannableString content = new SpannableString(s + filterNumber);
                    content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), s.length(), (s + filterNumber).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvCondition1.setText(content);

                    getDailiData();
                    break;
                case R.id.tvReset:
                    page = 1;
                    tvAll.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge);
                    tvActivation.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvActivation.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvUsed.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvUsed.setBackgroundResource(R.drawable.shape_recharge_gray);
                    etNumber.setText("");
                    selection = 0;
                    regularIndex = 0;
                    strAddress = "";
                    tvAddress.setText("请选择");
                    tvRegular.setText("请选择");

                    break;
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        List<LiangAgent> list;

        public MyAdapter(Context context, List<LiangAgent> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_whitepreopen_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            String province = "";
//            String city = "";
//            List<Province> provinces = area.getList();
//            for (int i = 0; i < provinces.size(); i++) {
//                if (provinces.get(i).getP_id().equals(list.get(position).getProvinceCode())) {
//                    province = provinces.get(i).getP_name();
//                    List<City> cities = provinces.get(i).getP_list();
//                    for (int j = 0; j < cities.size(); j++) {
//                        if (cities.get(j).getC_id().equals(list.get(position).getCityCode())) {
//                            city = cities.get(j).getC_name();
//                            break;
//                        }
//                    }
//                    break;
//                }
//            }
            viewHolder.tvPhone.setText("手机号:  " + list.get(position).getNumber() + "  状态:  ");
            String status = "";
            switch (list.get(position).getLiangStatus()) {
                case 0:
                    status = "未分配";
                    break;
                case 1:
                    status = "已激活";
                    break;
                case 2:
                    status = "已使用";
                    break;
                case 3:
                    status = "锁定";
                    break;
                case 4:
                    status = "开户中";
                    break;
            }
            viewHolder.tvStatus.setText(status);
            return convertView;
        }

    }

    class ViewHolder {
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvStatus)
        TextView tvStatus;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
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
                String address = "";
                if (provinceIndex == 1 || provinceIndex == 2 || provinceIndex == 6 || provinceIndex == 8 || provinceIndex == 12 || provinceIndex == 22)
                    address = currentCity;
                else
                    address = currentProvince + "," + currentCity;
                holder.tvAddress.setText(address);

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
}
