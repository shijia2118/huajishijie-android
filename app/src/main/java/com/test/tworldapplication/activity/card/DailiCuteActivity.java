package com.test.tworldapplication.activity.card;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.account.RechargeRecordNewActivity;
import com.test.tworldapplication.activity.admin.AdminActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.adapter.NumberAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.City;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpPostNew;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangAgent;
import com.test.tworldapplication.entity.LiangRule;
import com.test.tworldapplication.entity.PostAgentsLiang;
import com.test.tworldapplication.entity.PostCaution;
import com.test.tworldapplication.entity.PostCuteRegular;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.Province;
import com.test.tworldapplication.entity.RequestCaution;
import com.test.tworldapplication.entity.RequestLiang;
import com.test.tworldapplication.entity.RequestLiangAgents;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.entity.WhitePreOpen;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.inter.OnSelectListener;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.WheelView;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class DailiCuteActivity extends BaseActivity {
    List<LiangAgent> list = new ArrayList<>();
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
    private String strAddress = "", strRegular = "";
    private List<String> regularList = new ArrayList<>();
    private List<LiangRule> liangRuleList = new ArrayList<>();
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvCondition)
    TextView tvCondition;
    @BindView(R.id.tvCondition0)
    TextView tvCondition0;
    @BindView(R.id.tvCondition1)
    TextView tvCondition1;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.vLine0)
    View vLine0;
    @BindView(R.id.shadow_view)
    View shadowView;


    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;

//    @BindView(R.id.content_view)
//    PullableListView contentView;
//    @BindView(R.id.refresh_view)
//    PullToRefreshLayout refreshView;

    private String from = "";
    private int page = 1, linage = 10;

    //    PullToRefreshLayout pullToRefreshLayout;
    MyAdapter myAdapter;

    int refresh = 0;
    int regularIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daili_cute);
        ButterKnife.bind(this);


        from = getIntent().getStringExtra("from");
        if (from.equals("0")) {
            setBackGroundTitle("代理商靓号", true);
        } else if (from.equals("1")) {
            setBackGroundTitle("话机世界靓号", true);
        }
        tvCollection.setVisibility(View.VISIBLE);
        tvCollection.setText("筛选");
        area = BaseUtils.getArea(DailiCuteActivity.this);

        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refresh = 1;
                if (from.equals("0")) {
                    getDailiData();
                } else if (from.equals("1")) {
                    getHuajiData();
                }
            }

            @Override
            public void onLoadmore() {
                page++;
                refresh = 2;
                if (from.equals("0")) {
                    getDailiData();
                } else if (from.equals("1")) {
                    getHuajiData();
                }
            }
        });
        springView.setHeader(new DefaultHeader(DailiCuteActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultFooter(DailiCuteActivity.this));

//        regularList.add("AAAA");
//        regularList.add("BBBB");
//        regularList.add("CCCC");
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//
//        refreshView.setOnRefreshListener(this);
        myAdapter = new MyAdapter(DailiCuteActivity.this, list);
        contentView.setAdapter(myAdapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (from.equals("0")) {
//                    HttpPost<PostOpenPower> httpPost0=new HttpPost<>();
//                    PostOpenPower postOpenPower=new PostOpenPower();
//                    postOpenPower.setSession_token(Util.getLocalAdmin(DailiCuteActivity.this)[0]);
//                    httpPost0.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
//                    httpPost0.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpenPower) + BaseCom.APP_PWD));
//                    httpPost0.setParameter(postOpenPower);
//                    Log.d("aaa", gson.toJson(httpPost0));
//                    new AdminHttp().getOpenPower( AdminRequest.getOpenPower( DailiCuteActivity.this, new SuccessValue<RequestOpenPower>() {
//                        @Override
//                        public void OnSuccess(RequestOpenPower value) {
//                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putInt( "pattern",value.getPattern() );
//                            editor.putInt( "modes",value.getModes());
//                            editor.putInt( "readModes",value.getReadModes());
//                            editor.commit();
                            Intent intent = new Intent();
                            intent.setClass(DailiCuteActivity.this, DailiCuteDetailActivity.class);
                            intent.putExtra("data", (Serializable) list.get(position));
                            startActivity(intent);
//                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                        }
//                    } ),httpPost0 );
//                    Intent intent = new Intent();
//                    intent.setClass(DailiCuteActivity.this, DailiCuteDetailActivity.class);
//                    intent.putExtra("data", (Serializable) list.get(position));
//                    startActivity(intent);
//                    Intent intent = new Intent(DailiCuteActivity.this, DailiCuteDetailActivity.class);

                } else if (from.equals("1")) {

//                    HttpPost<PostOpenPower> httpPost0=new HttpPost<>();
//                    PostOpenPower postOpenPower=new PostOpenPower();
//                    postOpenPower.setSession_token(Util.getLocalAdmin(DailiCuteActivity.this)[0]);
//                    httpPost0.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
//                    httpPost0.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpenPower) + BaseCom.APP_PWD));
//                    httpPost0.setParameter(postOpenPower);
//                    Log.d("aaa", gson.toJson(httpPost0));
//                    new AdminHttp().getOpenPower( AdminRequest.getOpenPower( DailiCuteActivity.this, new SuccessValue<RequestOpenPower>() {
//                        @Override
//                        public void OnSuccess(RequestOpenPower value) {
//                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putInt( "pattern",value.getPattern() );
//                            editor.putInt( "modes",value.getModes());
//                            editor.putInt( "readModes",value.getReadModes());
//                            editor.commit();
                            Intent intent = new Intent(DailiCuteActivity.this, HuajiCuteDetailActivity.class);
                            intent.putExtra("data", (Serializable) list.get(position));
                            startActivity(intent);
//                        }
//                    } ),httpPost0 );


//                    intent.setClass(DailiCuteActivity.this, HuajiCuteDetailActivity.class);
//                    startActivity(HuajiCuteDetailActivity.class);

                }

            }
        });
        getRegularList();

        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_six, null);
        holder = new Holder(music_popunwindwow);

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        refresh = 0;
//        page = 1;
//        if (from.equals("0")) {
//            getDailiData();
//        } else if (from.equals("1")) {
//            getHuajiData();
//        }

    }

    private void startActivity(Class clazz) {
        Intent intent = new Intent(DailiCuteActivity.this, clazz);
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }


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

//    @Override
//    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        this.pullToRefreshLayout = pullToRefreshLayout;
//        page = 1;
//        refresh = 1;
//        if (from.equals("0")) {
//            getDailiData();
//        } else if (from.equals("1")) {
//            getHuajiData();
//        }
//
//    }
//
//    @Override
//    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//        this.pullToRefreshLayout = pullToRefreshLayout;
//        page++;
//        refresh = 2;
//        if (from.equals("0")) {
//            getDailiData();
//        } else if (from.equals("1")) {
//            getHuajiData();
//        }
//
//    }

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

    class Holder {
        @BindView(R.id.ll_address)
        LinearLayout llAddress;
        @BindView(R.id.ll_state)
        LinearLayout llState;
        //        @BindView(R.id.etNumber)
//        EditText etNumber;
        @BindView(R.id.tvInquery)
        TextView tvInquery;
        @BindView(R.id.tvReset)
        TextView tvReset;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvRegular)
        TextView tvRegular;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.ll_address, R.id.ll_state, R.id.tvReset, R.id.tvInquery})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_address:
                    showChooseAreaDialog_new();
                    break;
                case R.id.ll_state:
                    if (regularList.size() > 0)
                        Util.createDialog(DailiCuteActivity.this, regularList, tvRegular, new OnSelectListener() {
                            @Override
                            public void onSelect(int selection) {
                                regularIndex = selection;
                            }
                        });
                    else
                        Toast.makeText(DailiCuteActivity.this, "请稍后", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.tvReset:
                    page = 1;
                    holder.tvAddress.setText("请选择");
                    holder.tvRegular.setText("请选择");
                    break;
                case R.id.tvInquery:
                    page = 1;
                    strAddress = holder.tvAddress.getText().toString();
                    strRegular = holder.tvRegular.getText().toString();
                    mPopupWindow.dismiss();
                    if (!strAddress.equals("请选择") || !strRegular.equals("请选择")) {
                        tvCondition0.setText("筛选条件:  " + Util.strBack(strAddress));
                        tvCondition1.setText(Util.strBack(strRegular));
                    } else {
                        tvCondition0.setText("");
                        tvCondition1.setText("");
                    }
//                    if (strAddress.equals("请选择") && strRegular.equals("请选择")) {
//                        llCondition.setVisibility(View.GONE);
//                        vLine0.setVisibility(View.GONE);
//                    } else {
//                        llCondition.setVisibility(View.VISIBLE);
//                        vLine0.setVisibility(View.VISIBLE);
//                        tvCondition0.setText("筛选条件:  " + Util.strBack(strAddress));
//                        tvCondition1.setText(Util.strBack(strRegular));
//                    }
                    if (from.equals("0")) {
                        getDailiData();
                    } else if (from.equals("1")) {
                        getHuajiData();
                    }

                    break;
            }


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
//            Log.d("aaa", list.get(position).getNumber());
//            Log.d("aaa", list.get(position).getStatus());
            String province = "";
            String city = "";
            List<Province> provinces = area.getList();
            for (int i = 0; i < provinces.size(); i++) {
                if (provinces.get(i).getP_id().equals(list.get(position).getProvinceCode())) {
                    province = provinces.get(i).getP_name();
                    if (i == 1 || i == 2 || i == 6 || i == 8 || i == 12 || i == 22)
                        province = "";
                    List<City> cities = provinces.get(i).getP_list();
                    for (int j = 0; j < cities.size(); j++) {
                        if (cities.get(j).getC_id().equals(list.get(position).getCityCode())) {
                            city = cities.get(j).getC_name();
                            break;
                        }
                    }
                    break;
                }
            }
            viewHolder.tvPhone.setText("手机号:  " + list.get(position).getNumber() + "  归属地:  ");
            viewHolder.tvStatus.setText(province + city);
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

    /***
     * 代理商靓号列表
     */
    private void getRegularList() {
        regularList.clear();

        PostCuteRegular postCuteRegular = new PostCuteRegular();
        postCuteRegular.setSession_token(Util.getLocalAdmin(DailiCuteActivity.this)[0]);
        HttpPost<PostCuteRegular> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCuteRegular) + BaseCom.APP_PWD));
        httpPost.setParameter(postCuteRegular);
        Log.d("aaa", gson.toJson(httpPost));
        new CardHttp().getLiangRule(httpPost, new Subscriber<HttpRequest<RequestLiang>>() {
            @Override
            public void onCompleted() {
                if (from.equals("0")) {
                    getDailiData();
                } else if (from.equals("1")) {
                    getHuajiData();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLiang> requestLiangHttpRequest) {
                for (int i = 0; i < requestLiangHttpRequest.getData().getLiangRule().size(); i++) {
                    regularList.add(requestLiangHttpRequest.getData().getLiangRule().get(i).getRuleName());
                    liangRuleList.add(requestLiangHttpRequest.getData().getLiangRule().get(i));
                }
            }
        });


    }

    private void getDailiData() {
        PostAgentsLiang postAgentsLiang = new PostAgentsLiang();
        postAgentsLiang.setSession_token(Util.getLocalAdmin(DailiCuteActivity.this)[0]);
        postAgentsLiang.setPage(String.valueOf(page));
        postAgentsLiang.setLinage(String.valueOf(linage));
        if (!strAddress.equals("请选择") && !strAddress.equals("")) {
            postAgentsLiang.setProvinceCode(area.getList().get(provinceIndex).getP_id());
            postAgentsLiang.setCityCode(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
        }
//        if (!strRegular.equals("请选择") && !strRegular.equals(""))
        if (liangRuleList.size() > 0)
            postAgentsLiang.setLiangRuleId(liangRuleList.get(regularIndex).getId());

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
                if (requestLiangAgentsHttpRequest.getCode() == BaseCom.NORMAL) {
                    finishGetData();
                    tvCondition.setText(requestLiangAgentsHttpRequest.getData().getCount() + "");
                    list.addAll(requestLiangAgentsHttpRequest.getData().getLiangAgents());
                } else if (requestLiangAgentsHttpRequest.getCode() == BaseCom.LOSELOG || requestLiangAgentsHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(DailiCuteActivity.this, LoginActivity.class);
                }
            }
        });


    }

    private void getHuajiData() {
        PostAgentsLiang postAgentsLiang = new PostAgentsLiang();
        postAgentsLiang.setSession_token(Util.getLocalAdmin(DailiCuteActivity.this)[0]);
        postAgentsLiang.setPage(String.valueOf(page));
        postAgentsLiang.setLinage(String.valueOf(linage));
        if (!strAddress.equals("请选择") && !strAddress.equals("")) {
            postAgentsLiang.setProvinceCode(area.getList().get(provinceIndex).getP_id());
            postAgentsLiang.setCityCode(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
        }
//        if (!strRegular.equals("请选择") && !strRegular.equals(""))
        if (liangRuleList.size() > 0 && !strRegular.equals("请选择") && !strRegular.equals(""))
            postAgentsLiang.setLiangRuleId(liangRuleList.get(regularIndex).getId());

        HttpPost<PostAgentsLiang> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postAgentsLiang);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postAgentsLiang) + BaseCom.APP_PWD));
        new CardHttp().getHjsjLiangList(httpPost, new Subscriber<HttpRequest<RequestLiangAgents>>() {
            @Override
            public void onCompleted() {
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLiangAgents> requestLiangAgentsHttpRequest) {
                if (requestLiangAgentsHttpRequest.getCode() == BaseCom.NORMAL) {
                    finishGetData();
                    tvCondition.setText(requestLiangAgentsHttpRequest.getData().getCount() + "");
                    list.addAll(requestLiangAgentsHttpRequest.getData().getLiangAgents());
                } else if (requestLiangAgentsHttpRequest.getCode() == BaseCom.LOSELOG || requestLiangAgentsHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(DailiCuteActivity.this, LoginActivity.class);
                }
            }
        });


    }
}


