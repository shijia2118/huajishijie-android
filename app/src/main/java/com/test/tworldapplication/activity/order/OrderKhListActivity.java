package com.test.tworldapplication.activity.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.RechargeRecordNewActivity;
import com.test.tworldapplication.activity.card.DailiCuteDetailActivity;
import com.test.tworldapplication.activity.card.HuajiCuteDetailActivity;
import com.test.tworldapplication.activity.card.MessageCollectionActivity;
import com.test.tworldapplication.activity.card.MessageCollectionNewActivity;
import com.test.tworldapplication.activity.card.OrderJHDetailActivity;
import com.test.tworldapplication.activity.card.SelectActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.PhoneDetailActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Numbers;
import com.test.tworldapplication.entity.OrderAudit;
import com.test.tworldapplication.entity.OrderCz;
import com.test.tworldapplication.entity.OrderGH;
import com.test.tworldapplication.entity.OrderKH;
import com.test.tworldapplication.entity.PostAuditList;
import com.test.tworldapplication.entity.PostOrderList;
import com.test.tworldapplication.entity.PostPreNumberListNew;
import com.test.tworldapplication.entity.PostRechargeList;
import com.test.tworldapplication.entity.PostTransferList;
import com.test.tworldapplication.entity.RequestAuditList;
import com.test.tworldapplication.entity.RequestOrderList;
import com.test.tworldapplication.entity.RequestPreNumberNew;
import com.test.tworldapplication.entity.RequestRechargeList;
import com.test.tworldapplication.entity.RequestTransferList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.DoubleDatePickerDialog;
import com.test.tworldapplication.entity.Number;
import com.test.tworldapplication.view.MyPopWindow;
import com.test.tworldapplication.view.hellocharts.model.Line;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class OrderKhListActivity extends BaseActivity {
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvCondition)
    TextView tvCondition;
    @BindView(R.id.tvCondition1)
    TextView tvCondition1;

    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.vLine0)
    View vLine0;
    List<OrderKH> kh_list = new ArrayList<>();
    List<OrderGH> gh_list = new ArrayList<>();
    List<OrderCz> cz_list = new ArrayList<>();
    List<OrderAudit> ad_list = new ArrayList<>();
    List<Number> jh_list = new ArrayList<>();

    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.shadow_view)
    View shadowView;
    private String from = "";
    int refresh = 0;
    private int page = 1, linage = 10;
    MyAdapter myAdapter;
    Holder holder;
    PopupWindow mPopupWindow;
    //    List<String> list0Order = new ArrayList<>();
    List<String> list0Order = new ArrayList<>();
    //    MyPopWindow myPopWindow = null;
    String strBegin = "请选择", strEnd = "请选择", strNumber = "";
    String strState = "请选择";
    String rechargeType = "";
//    int stateIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ckkh_list);
        ButterKnife.bind(this);

        from = getIntent().getStringExtra("from");//0成卡开户,1白卡开户
        switch (from) {
            case "0":
                /*成卡开户*/
                setBackGroundTitle("成卡开户订单", true);
                list0Order.clear();
                list0Order.add("全部");
                list0Order.add("已提交");
                list0Order.add("等待中");
                list0Order.add("成功");
                list0Order.add("失败");
                list0Order.add("已取消");


//                myPopWindow = new MyPopWindow(OrderKhListActivity.this, 1, null, null, list0Order);
                break;
            case "1":
                /*白卡开户*/
                setBackGroundTitle("白卡开户订单", true);
                list0Order.clear();
                list0Order.add("全部");
                list0Order.add("已提交");
                list0Order.add("等待中");
                list0Order.add("成功");
                list0Order.add("失败");
                list0Order.add("已取消");
                list0Order.add("已关闭");
//                myPopWindow = new MyPopWindow(OrderKhListActivity.this, 1, null, null, list0Order);
                break;
            case "2":
                /*过户*/
                setBackGroundTitle("过户订单", true);
                list0Order.clear();
                list0Order.add("全部");
                list0Order.add("待审核");
                list0Order.add("审核通过");
                list0Order.add("审核不通过");
//                myPopWindow = new MyPopWindow(OrderKhListActivity.this, 1, null, null, list0Order);
                break;
            case "3":
                /*补卡*/
                setBackGroundTitle("补卡订单", true);
                list0Order.clear();
                list0Order.add("全部");
                list0Order.add("待审核");
                list0Order.add("审核通过");
                list0Order.add("审核不通过");
//                myPopWindow = new MyPopWindow(OrderKhListActivity.this, 1, null, null, list0Order);
                break;
            case "4":
                rechargeType = "1";
                setBackGroundTitle("话费充值订单", true);
//                myPopWindow = new MyPopWindow(OrderKhListActivity.this, 2, null, null, null);
                break;
            case "5":
                rechargeType = "0";
                setBackGroundTitle("账户充值订单", true);
//                myPopWindow = new MyPopWindow(OrderKhListActivity.this, 3, null, null, null);
                break;
            case "6":
                setBackGroundTitle("预订单", true);
                list0Order.clear();
                list0Order.add("全部");
                list0Order.add("待审核");
                list0Order.add("审核通过");
                list0Order.add("审核不同过");
                list0Order.add("发货");
                list0Order.add("取消订单");
                break;
            case "7":
                setBackGroundTitle("写卡激活订单", true);
                list0Order.clear();
                list0Order.add("全部");
                list0Order.add("待开户");
                list0Order.add("已开户");
                break;
        }

//        myPopWindow.setOnQueryListener(new MyPopWindow.OnQueryListener() {
//            @Override
//            public void OnQuery(String strPopBegin, String strPopEnd, String strAddress, String strRegular, String strPool, String strNumber, int regularIndex, int poolIndex, int stateIndex, int selection, String provinceCode, String cityCode) {
//                page = 1;
//                refresh = 0;
//                OrderKhListActivity.this.strBegin = strPopBegin;
//                OrderKhListActivity.this.strEnd = strPopEnd;
//                OrderKhListActivity.this.stateIndex = stateIndex;
//                OrderKhListActivity.this.strNumber = strNumber;
//                myPopWindow.showPopupWindow(vLine);
//                getData();
//            }
//        });


        tvCollection.setVisibility(View.VISIBLE);
        tvCollection.setText("筛选");

        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refresh = 1;
                getData();
            }

            @Override
            public void onLoadmore() {
                page++;
                refresh = 2;
                getData();
            }
        });
        springView.setHeader(new DefaultHeader(OrderKhListActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultHeader(OrderKhListActivity.this));

//        regularList.add("AAAA");
//        regularList.add("BBBB");
//        regularList.add("CCCC");
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//        list.add(new WhitePreOpen("12345678900", "浙江省杭州市"));
//
//        refreshView.setOnRefreshListener(this);
        myAdapter = new MyAdapter();
        contentView.setAdapter(myAdapter);
        getData();
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (from) {
                    case "0":
                    case "1":
                        intent.setClass(OrderKhListActivity.this, OrderKHDetailNewActivity.class);
                        intent.putExtra("from", from);
                        intent.putExtra("data", kh_list.get(position));
                        break;
                    case "2":
                        intent.setClass(OrderKhListActivity.this, OrderGHDetailNewActivity.class);
                        intent.putExtra("data", gh_list.get(position));
                        break;
                    case "3":
                        intent.setClass(OrderKhListActivity.this, OrderBKDetailNewActivity.class);
                        intent.putExtra("data", gh_list.get(position));
                        break;
                    case "4":
                    case "5":
                        intent.setClass(OrderKhListActivity.this, OrderCZDetailNewActivity.class);
                        intent.putExtra("from", from);
                        intent.putExtra("data", cz_list.get(position));
                        break;
                    case "6":
                        intent.setClass(OrderKhListActivity.this, OrderYKHDetailNewActivity.class);
                        intent.putExtra("from", from);
                        intent.putExtra("data", ad_list.get(position));
                        break;
                    case "7":
                        intent.setClass(OrderKhListActivity.this, OrderJHDetailActivity.class);
//                        intent.putExtra("from", from);
                        intent.putExtra("data", jh_list.get(position));
                        break;
                }


//                if (from.equals("0")) {
//                    intent.setClass(OrderKhListActivity.this, DailiCuteDetailActivity.class);
////                    Intent intent = new Intent(DailiCuteActivity.this, DailiCuteDetailActivity.class);
//
//                } else if (from.equals("1")) {
//                    intent.setClass(OrderKhListActivity.this, HuajiCuteDetailActivity.class);
////                    startActivity(HuajiCuteDetailActivity.class);
//
//                }
                if (!Util.isFastDoubleClick()) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
            }
        });

        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition, null);
        holder = new Holder(music_popunwindwow);
        switch (from) {
            case "4":
                holder.llState.setVisibility(View.GONE);
                holder.vLine1.setVisibility(View.GONE);
                break;
            case "5":
                holder.llState.setVisibility(View.GONE);
                holder.vLine1.setVisibility(View.GONE);
                holder.llPhoneNumber.setVisibility(View.GONE);
                holder.linePhoneNumber.setVisibility(View.GONE);
                break;
        }

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @OnClick(R.id.tvCollection)
    public void onClick() {
//        contentView.setAlpha((float) 0.2);
        shadowView.setVisibility(View.VISIBLE);
        mPopupWindow.showAsDropDown(vLine, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                contentView.setAlpha((float) 1);
                shadowView.setVisibility(View.GONE);
            }
        });
    }

    class Holder {
        @BindView(R.id.tvReset)
        TextView tvReset;
        @BindView(R.id.tvPopBegin)
        TextView tvPopBegin;
        @BindView(R.id.llBegin)
        LinearLayout llBegin;
        @BindView(R.id.tvPopEnd)
        TextView tvPopEnd;
        @BindView(R.id.llEnd)
        LinearLayout llEnd;
        @BindView(R.id.tvTitleState)
        TextView tvTitleState;
        @BindView(R.id.tvState)
        TextView tvState;


        @BindView(R.id.vLine1)
        View vLine1;
        @BindView(R.id.llState)
        LinearLayout llState;
        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.tvInquery)
        TextView tvInquery;

        @BindView(R.id.linePhoneNumber)
        View linePhoneNumber;
        @BindView(R.id.llPhoneNumber)
        RelativeLayout llPhoneNumber;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }


        @OnClick({R.id.llBegin, R.id.llEnd, R.id.llState, R.id.tvReset, R.id.tvInquery})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llBegin:
                    if (!Util.isFastDoubleClick())
                        showDatePikerDialog(0, tvPopBegin);
                    break;
                case R.id.llEnd:
                    if (!Util.isFastDoubleClick())
                        showDatePikerDialog(1, tvPopEnd);
                    break;
                case R.id.llState:
                    Util.createDialog(OrderKhListActivity.this, list0Order, tvState, null);

                    break;
                case R.id.tvReset:
                    page = 1;
                    tvPopBegin.setText("请选择");
                    tvPopEnd.setText("请选择");
                    etNumber.setText("");
                    tvState.setText("请选择");
                    break;
                case R.id.tvInquery:
                    strBegin = tvPopBegin.getText().toString().trim();
                    strEnd = tvPopEnd.getText().toString().trim();
                    strState = tvState.getText().toString().trim();
                    strNumber = etNumber.getText().toString().trim();


                    if (Util.compare(strEnd, strBegin)) {
                        Util.createToast(OrderKhListActivity.this, "请输入正确的起止日期");
                    } else {
                        page = 1;
                        mPopupWindow.dismiss();
                        String s = "";
                        if (!strBegin.equals("") || !strEnd.equals("") || !strState.equals("") || !strNumber.equals(""))
                            s += "筛选条件:  " + Util.strBack(strBegin) + "    " + Util.strBack(strEnd) + "  ";
                        s += Util.strBack(strState) + "  ";
                        SpannableString content = new SpannableString(s + Util.strBack(strNumber));
                        content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), s.length(), (s + Util.strBack(strNumber)).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        tvCondition1.setText(content);


                        getData();
                    }
//                    if (strBegin.equals("请选择") && strEnd.equals("请选择") && strNumber.equals("") && strState.equals("请选择")) {
//                        llCondition.setVisibility(View.GONE);
//                        search();
//                        mPopupWindow.dismiss();
//                    } else if (Util.compare(strEnd, strBegin)) {
//                        Util.createToast(getActivity(), "请输入正确的起止日期");
//                    } else {
//                        llCondition.setVisibility(View.VISIBLE);
//                        tvBegin.setText("起始时间:" + Util.strBackNull(strBegin));
//                        tvEnd.setText("截止时间:" + Util.strBackNull(strEnd));
//                        tvDdState.setText("订单状态:" + Util.strBackNull(strState));
//                        Log.d("ccc", Util.strBackNull(strNumber));
//                        tvPhone.setText("手机号码:" + Util.strBackNull(strNumber));
//
//                        search();
//                        mPopupWindow.dismiss();
//                    }

                    break;
            }
        }
    }

    private void getData() {
        String statusCode = "";
        String type = "";
        switch (from) {
            case "0":
                type = "SIM";
                break;
            case "1":
                type = "ESIM";
                break;
            case "2":
                type = "0";
                break;
            case "3":
                type = "1";
                break;
        }

        switch (from) {
            case "0":
            case "1":

                HttpPost<PostOrderList> httpPost = new HttpPost<>();
                PostOrderList postOrderList = new PostOrderList();
                postOrderList.setSession_token(Util.getLocalAdmin(OrderKhListActivity.this)[0]);
                if (Util.isNull(strNumber)) {
                    postOrderList.setNumber(strNumber);
                }
                if (Util.isNull(strBegin)) {
                    postOrderList.setStartTime(strBegin);
                }
                if (Util.isNull(strEnd)) {
                    postOrderList.setEndTime(strEnd);
                }
                if (Util.isNull(strState)) {
                    switch (strState) {
                        case "已提交":
                            statusCode = "PENDING";
                            break;
                        case "等待中":
                            statusCode = "WAITING";
                            break;
                        case "成功":
                            statusCode = "SUCCESS";
                            break;
                        case "失败":
                            statusCode = "FAIL";
                            break;
                        case "已取消":
                            statusCode = "CANCLE";
                            break;
                        case "已关闭":
                            statusCode = "CLOSED";
                            break;
                    }
                    postOrderList.setOrderStatusCode(statusCode);
                    postOrderList.setOrderStatusName(strState);
                }
                postOrderList.setType(type);
                postOrderList.setPage(String.valueOf(page));
                postOrderList.setLinage(String.valueOf(BaseCom.linage));
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postOrderList);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postOrderList) + BaseCom.APP_PWD));
                Log.d("aaa", gson.toJson(httpPost));
                new OrderHttp().orderKhList(new Subscriber<HttpRequest<RequestOrderList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        springView.onFinishFreshAndLoad();
                    }

                    @Override
                    public void onNext(HttpRequest<RequestOrderList> requestOrderListHttpRequest) {
                        Util.createToast(OrderKhListActivity.this, requestOrderListHttpRequest.getMes());
                        if (requestOrderListHttpRequest.getCode() == BaseCom.NORMAL) {
                            switch (refresh) {
                                case 0:
                                    kh_list.clear();
                                    break;
                                case 1:
                                    kh_list.clear();
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                            }

                            refresh = 0;
                            kh_list.addAll(requestOrderListHttpRequest.getData().getOrder());
                            SpannableString title = new SpannableString("共" + kh_list.size() + "条");
                            title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(kh_list.size()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvCondition.setText(title);
                            myAdapter.notifyDataSetChanged();
                        } else if (requestOrderListHttpRequest.getCode() == BaseCom.LOSELOG || requestOrderListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                            Util.gotoActy(OrderKhListActivity.this, LoginActivity.class);
                    }
                }, httpPost);
                break;
            case "2":
            case "3":

                HttpPost<PostTransferList> httpPost1 = new HttpPost<>();
                PostTransferList postTransferList = new PostTransferList();
                postTransferList.setSession_token(Util.getLocalAdmin(OrderKhListActivity.this)[0]);
                if (Util.isNull(strNumber)) {
                    postTransferList.setNumber(strNumber);
                }
                if (Util.isNull(strBegin)) {
                    postTransferList.setStartTime(strBegin);
                }
                if (Util.isNull(strEnd)) {
                    postTransferList.setEndTime(strEnd);
                }
                if (Util.isNull(strState)) {
                    switch (strState) {
                        case "待审核":
                            statusCode = "1";
                            break;
                        case "审核通过":
                            statusCode = "2";
                            break;
                        case "审核不通过":
                            statusCode = "3";
                            break;

                    }
                    postTransferList.setStartCode(statusCode);
                    postTransferList.setStartName(strState);
                }
                postTransferList.setType(type);
                postTransferList.setPage(String.valueOf(page));
                postTransferList.setLinage(String.valueOf(BaseCom.linage));
                httpPost1.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost1.setParameter(postTransferList);
                httpPost1.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postTransferList) + BaseCom.APP_PWD));
                new OrderHttp().orderTransferList(new Subscriber<HttpRequest<RequestTransferList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        springView.onFinishFreshAndLoad();

                    }

                    @Override
                    public void onNext(HttpRequest<RequestTransferList> requestTransferListHttpRequest) {
                        Util.createToast(OrderKhListActivity.this, requestTransferListHttpRequest.getMes());
                        if (requestTransferListHttpRequest.getCode() == BaseCom.NORMAL) {
                            switch (refresh) {
                                case 0:
                                    gh_list.clear();
                                    break;
                                case 1:
                                    gh_list.clear();
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                            }

                            refresh = 0;
                            gh_list.addAll(requestTransferListHttpRequest.getData().getOrder());

                            SpannableString title = new SpannableString("共" + gh_list.size() + "条");
                            title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(gh_list.size()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvCondition.setText(title);

                            myAdapter.notifyDataSetChanged();
                        } else if (requestTransferListHttpRequest.getCode() == BaseCom.LOSELOG || requestTransferListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                            Util.gotoActy(OrderKhListActivity.this, LoginActivity.class);
                    }
                }, httpPost1);

                break;
            case "4":
            case "5":
                HttpPost<PostRechargeList> httpPost2 = new HttpPost<>();
                PostRechargeList postRechargeList = new PostRechargeList();
                postRechargeList.setSession_token(Util.getLocalAdmin(OrderKhListActivity.this)[0]);
                if (Util.isNull(strNumber)) {
                    postRechargeList.setNumber(strNumber);
                }
                if (Util.isNull(strBegin)) {
                    postRechargeList.setStartTime(strBegin);
                }
                if (Util.isNull(strEnd)) {
                    postRechargeList.setEndTime(strEnd);
                }
                postRechargeList.setRechargeType(rechargeType);
                postRechargeList.setPage(String.valueOf(page));
                postRechargeList.setLinage(String.valueOf(BaseCom.linage));
                httpPost2.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost2.setParameter(postRechargeList);
                httpPost2.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRechargeList) + BaseCom.APP_PWD));
                Log.d("aaa", gson.toJson(httpPost2));
                new OrderHttp().orderRechargeList(new Subscriber<HttpRequest<RequestRechargeList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        springView.onFinishFreshAndLoad();
                    }

                    @Override
                    public void onNext(HttpRequest<RequestRechargeList> requestRechargeListHttpRequest) {
                        Util.createToast(OrderKhListActivity.this, requestRechargeListHttpRequest.getMes());
                        if (requestRechargeListHttpRequest.getCode() == BaseCom.NORMAL) {
                            switch (refresh) {
                                case 0:
                                    cz_list.clear();
                                    break;
                                case 1:
                                    cz_list.clear();
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                            }
                            refresh = 0;
                            cz_list.addAll(requestRechargeListHttpRequest.getData().getOrder());
                            myAdapter.notifyDataSetChanged();

                            SpannableString title = new SpannableString("共" + cz_list.size() + "条");
                            title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(cz_list.size()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvCondition.setText(title);

                        } else if (requestRechargeListHttpRequest.getCode() == BaseCom.LOSELOG || requestRechargeListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                            Util.gotoActy(OrderKhListActivity.this, LoginActivity.class);
                    }
                }, httpPost2);


                break;
            case "6":
                HttpPost<PostAuditList> httpPost3 = new HttpPost<>();
                PostAuditList postAuditList = new PostAuditList();
                postAuditList.setSession_token(Util.getLocalAdmin(OrderKhListActivity.this)[0]);
                if (Util.isNull(strNumber)) {
                    postAuditList.setCon_number(strNumber);
                }
                if (Util.isNull(strBegin)) {
                    postAuditList.setStartTime(strBegin);
                }
                if (Util.isNull(strEnd)) {
                    postAuditList.setEndTime(strEnd);
                }
                if (Util.isNull(strState)) {
                    String state_code = "";
                    switch (strState) {
                        case "待预审":
                            state_code = "0";
                            break;
                        case "预审通过":
                            state_code = "1";
                            break;
                        case "预审不通过":
                            state_code = "2";
                            break;
                        case "已发货":
                            state_code = "3";
                            break;
                        case "已取消":
                            state_code = "4";
                            break;
                    }
                    postAuditList.setOrderStatus(state_code);
                }
//                postAuditList.setOrderStatus(strState);
                postAuditList.setPage(String.valueOf(page));
                postAuditList.setLinage(String.valueOf(BaseCom.linage));
                httpPost3.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost3.setParameter(postAuditList);
                httpPost3.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postAuditList) + BaseCom.APP_PWD));
                Log.d("aaa", gson.toJson(httpPost3));
                new OrderHttp().orderAuditList(new Subscriber<HttpRequest<RequestAuditList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpRequest<RequestAuditList> requestAuditListHttpRequest) {
                        Util.createToast(OrderKhListActivity.this, requestAuditListHttpRequest.getMes());
                        if (requestAuditListHttpRequest.getCode() == BaseCom.NORMAL) {
                            switch (refresh) {
                                case 0:
                                    ad_list.clear();
                                    break;
                                case 1:
                                    ad_list.clear();
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                            }
                            refresh = 0;
                            ad_list.addAll(requestAuditListHttpRequest.getData().getOrders());
                            myAdapter.notifyDataSetChanged();

                            SpannableString title = new SpannableString("共" + ad_list.size() + "条");
                            title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(ad_list.size()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvCondition.setText(title);

                        } else if (requestAuditListHttpRequest.getCode() == BaseCom.LOSELOG || requestAuditListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                            Util.gotoActy(OrderKhListActivity.this, LoginActivity.class);

                    }
                }, httpPost3);
                break;
            case "7":
                HttpPost<PostPreNumberListNew> httpPost4 = new HttpPost<>();
                PostPreNumberListNew postPreNumberListNew = new PostPreNumberListNew();
                postPreNumberListNew.setSession_token(Util.getLocalAdmin(OrderKhListActivity.this)[0]);

                if (Util.isNull(strNumber)) {
                    postPreNumberListNew.setNumber(strNumber);
                }
                if (Util.isNull(strBegin)) {
                    postPreNumberListNew.setStartTime(strBegin);
                }
                if (Util.isNull(strEnd)) {
                    postPreNumberListNew.setEndTime(strEnd);
                }
                if (Util.isNull(strState)) {
                    if (strState.equals("待开户"))
                        postPreNumberListNew.setPreState("0");
                    else if (strState.equals("已开户"))
                        postPreNumberListNew.setPreState("1");
                }
                postPreNumberListNew.setPage(page + "");
                postPreNumberListNew.setLinage("10");
                httpPost4.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost4.setParameter(postPreNumberListNew);
                httpPost4.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPreNumberListNew) + BaseCom.APP_PWD));

                new OrderHttp().ePreNumberList(new Subscriber<HttpRequest<RequestPreNumberNew>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpRequest<RequestPreNumberNew> requestPreNumberNewHttpRequest) {
                        Util.createToast(OrderKhListActivity.this, requestPreNumberNewHttpRequest.getMes());
                        if (requestPreNumberNewHttpRequest.getCode() == BaseCom.NORMAL) {
                            switch (refresh) {
                                case 0:
                                    jh_list.clear();
                                    break;
                                case 1:
                                    jh_list.clear();
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                            }
                            refresh = 0;
                            jh_list.addAll(requestPreNumberNewHttpRequest.getData().getNumbers());
                            myAdapter.notifyDataSetChanged();

                            SpannableString title = new SpannableString("共" + requestPreNumberNewHttpRequest.getData().getCount() + "条");
                            title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(requestPreNumberNewHttpRequest.getData().getCount()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvCondition.setText(title);

                        } else if (requestPreNumberNewHttpRequest.getCode() == BaseCom.LOSELOG || requestPreNumberNewHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                            Util.gotoActy(OrderKhListActivity.this, LoginActivity.class);
                    }
                }, httpPost4);
                break;

        }


    }

    private void showDatePikerDialog(final int click_flag, final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(OrderKhListActivity.this, "选择日期", 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                                  DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                String date = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
                textView.setText(date);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), true);
        dialog.show();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;

        public MyAdapter() {
            layoutInflater = LayoutInflater.from(OrderKhListActivity.this);
        }

        @Override
        public int getCount() {
            int size = 0;
            switch (from) {
                case "0":
                case "1":
                    size = kh_list.size();
                    break;
                case "2":
                case "3":
                    size = gh_list.size();
                    break;
                case "4":
                case "5":
                    size = cz_list.size();
                    break;
                case "6":
                    size = ad_list.size();
                    break;
                case "7":
                    size = jh_list.size();
                    break;
            }
            return size;
        }

        @Override
        public Object getItem(int position) {
            Object object = null;
            switch (from) {
                case "0":
                case "1":
                    object = kh_list.get(position);
                    break;
                case "2":
                case "3":
                    object = gh_list.get(position);
                    break;
                case "4":
                case "5":
                    object = cz_list.get(position);
                    break;
                case "6":
                    object = ad_list.get(position);
                    break;
                case "7":
                    object = jh_list.get(position);
                    break;
            }
            return object;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.inflate_order_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String strName = "";
            String strStatus = "";
            String strTime = "";
            String strNumber = "";
            String strCzNumber = "";
            switch (from) {
                case "0":
                    viewHolder.llDjShow.setVisibility(View.VISIBLE);
                    strName = kh_list.get(position).getCustomerName();
                    strStatus = kh_list.get(position).getOrderStatusName();
                    strTime = kh_list.get(position).getStartTime();
                    strNumber = kh_list.get(position).getNumber();
                    break;
                case "1":
                    viewHolder.llDjShow.setVisibility(View.VISIBLE);
                    strName = kh_list.get(position).getCustomerName();
                    strStatus = kh_list.get(position).getOrderStatusName();
                    strTime = kh_list.get(position).getStartTime();
                    strNumber = kh_list.get(position).getNumber();
                    if (strStatus.equals("已取消"))
                        viewHolder.tvAgain.setVisibility(View.VISIBLE);
                    else
                        viewHolder.tvAgain.setVisibility(View.GONE);

                    break;
                case "2":
                case "3":
                    viewHolder.llDjShow.setVisibility(View.VISIBLE);
                    strName = gh_list.get(position).getName();
                    strStatus = gh_list.get(position).getStartName();
                    strTime = gh_list.get(position).getStartTime();
                    strNumber = gh_list.get(position).getNumber();
                    break;
                case "4":
                case "5":
                    viewHolder.llCzShow.setVisibility(View.VISIBLE);
                    strTime = cz_list.get(position).getStartTime();
                    strStatus = cz_list.get(position).getPayAmount() + "元";
                    strCzNumber = cz_list.get(position).getNumber();
                    break;
                case "6":
                    viewHolder.llDjShow.setVisibility(View.VISIBLE);
                    strName = ad_list.get(position).getCustomerName();
                    strStatus = ad_list.get(position).getOrderStatusName();
                    strTime = ad_list.get(position).getCreateDate();
                    strNumber = ad_list.get(position).getNumber();
                    break;
                case "7":
                    viewHolder.llDjShow.setVisibility(View.VISIBLE);
                    strName = jh_list.get(position).getePreNo();
                    strStatus = jh_list.get(position).getState();
                    strNumber = jh_list.get(position).getNumber();
                    strTime = jh_list.get(position).getCreatedate();
                    viewHolder.tvAgain.setText("重写");

                    if (strStatus != null && (strStatus.equals("失败") || strStatus.equals("已锁定"))) {
                        viewHolder.tvAgain.setVisibility(View.VISIBLE);
                    } else viewHolder.tvAgain.setVisibility(View.GONE);

                    break;

            }
            viewHolder.tvName.setText(strName);
            viewHolder.tvStatus.setText(strStatus);
            viewHolder.tvTime.setText(strTime);
            viewHolder.tvNumber.setText(strNumber);
            viewHolder.tvCZNumber.setText(strCzNumber);
//            viewHolder.tvAgain.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//            viewHolder.tvAgain.getPaint().setAntiAlias(true);//抗锯齿

            viewHolder.tvAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (from) {
                        case "1":
                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("pattern", 3);
                            editor.putInt("modes", 3);
                            editor.putInt("readModes", 3);
                            editor.commit();
                            Intent intent1 = new Intent(OrderKhListActivity.this, MessageCollectionNewActivity.class);
                            intent1.putExtra("from", "4");
                            intent1.putExtra("type", "1");
                            intent1.putExtra("orderNO", kh_list.get(position).getOrderNo());
                            startActivity(intent1);
                            break;
                        case "7":
                            Intent intent = new Intent(OrderKhListActivity.this, PhoneDetailActivity.class);
                            intent.putExtra("number", jh_list.get(position).getNumber());
                            if (jh_list.get(position).getState().equals("失败")) {
                                intent.putExtra("orderNo", jh_list.get(position).getePreNo());
                                intent.putExtra("from", "0");
                            } else if (jh_list.get(position).getState().equals("已锁定")) {
                                intent.putExtra("from", "1");
                                intent.putExtra("orderNo", jh_list.get(position).getePreNo());

                            }
                            startActivity(intent);

                            break;
                    }


                }
            });

            return convertView;
        }


    }

    class ViewHolder {
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAgain)
        TextView tvAgain;
        @BindView(R.id.tvNumber)
        TextView tvNumber;
        @BindView(R.id.tvCZNumber)
        TextView tvCZNumber;
        @BindView(R.id.ll_djShow)
        LinearLayout llDjShow;
        @BindView(R.id.ll_czShow)
        LinearLayout llCzShow;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
