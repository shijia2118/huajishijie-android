package com.test.tworldapplication.activity.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.OrderNewAdapter;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.OrderCz;
import com.test.tworldapplication.entity.OrderGH;
import com.test.tworldapplication.entity.OrderKH;
import com.test.tworldapplication.entity.PostOrderList;
import com.test.tworldapplication.entity.PostRechargeList;
import com.test.tworldapplication.entity.PostTransferList;
import com.test.tworldapplication.entity.RequestOrderList;
import com.test.tworldapplication.entity.RequestRechargeList;
import com.test.tworldapplication.entity.RequestTransferList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;
import com.test.tworldapplication.view.DoubleDatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by dasiy on 16/12/1.
 */

public class OrderMainFragment extends Fragment implements AdapterView.OnItemClickListener, DialogInterface.OnKeyListener {

    @BindView(R.id.tvCkkh)
    TextView tvCkkh;
    @BindView(R.id.vCkkh)
    View vCkkh;
    @BindView(R.id.llCkkh)
    LinearLayout llCkkh;
    @BindView(R.id.tvBkkh)
    TextView tvBkkh;
    @BindView(R.id.vBkkh)
    View vBkkh;
    @BindView(R.id.llBkkh)
    LinearLayout llBkkh;
    @BindView(R.id.tvGh)
    TextView tvGh;
    @BindView(R.id.vGh)
    View vGh;
    @BindView(R.id.llGh)
    LinearLayout llGh;
    @BindView(R.id.tvBk)
    TextView tvBk;
    @BindView(R.id.vBk)
    View vBk;
    @BindView(R.id.llBk)
    LinearLayout llBk;
    @BindView(R.id.tvCz)
    TextView tvCz;
    @BindView(R.id.vCz)
    View vCz;
    @BindView(R.id.llCz)
    LinearLayout llCz;
    @BindView(R.id.imgArr)
    ImageView imgArr;
    @BindView(R.id.llArr)
    LinearLayout llArr;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvBegin)
    TextView tvBegin;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    @BindView(R.id.tvDdState)
    TextView tvDdState;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvCzType)
    TextView tvCzType;
    @BindView(R.id.llCondition)
    LinearLayout llCondition;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;
//    @BindView(R.id.content_view)
//    PullableListView contentView;
//    @BindView(R.id.refresh_view)
//    PullToRefreshLayout refreshView;

    PopupWindow mPopupWindow;
    CheckResultDialog dialog;
    OrderNewAdapter adapter;
    //    PullToRefreshLayout pullToRefreshLayout;
    static int flag = 0;//设置选中某一个    默认0成卡开户
    int refresh = 0;//列表状态  刷新or加载
    List<String> list0Order = new ArrayList<>();
    List<String> list1Order = new ArrayList<>();
    List<String> list2Order = new ArrayList<>();
    List<OrderKH> listOrderKh = new ArrayList<>();
    List<OrderGH> orderGHList = new ArrayList<>();
    List<OrderCz> orderCzList = new ArrayList<>();
    private OrderKH[] arrKh = null;
    private OrderGH[] arrGh = null;
    private OrderCz[] arrCz = null;
    Gson gson = new Gson();
    ViewHolder viewHolder;
    String strBegin = "请选择", strEnd = "请选择", strNumber = "", strState = "请选择";
    int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View orderLayout = inflater.inflate(R.layout.activity_order_main, container, false);
        ButterKnife.bind(this, orderLayout);
        init();
        contentView.setOnItemClickListener(this);
        return orderLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        mPopupWindow.dismiss();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void init() {
        Log.d("fff", "init");
        dialog = new CheckResultDialog(getActivity(), R.style.CustomDialog);
        dialog.getTvTitle().setText("正在查询");
        dialog.setOnKeyListener(this);
        adapter = new OrderNewAdapter(getActivity());

        setClickState();
        list0Order.clear();
        list0Order.add("全部");
        list0Order.add("已提交");
        list0Order.add("等待中");
        list0Order.add("成功");
        list0Order.add("失败");
        list0Order.add("已取消");
        list1Order.clear();
        list1Order.add("全部");
        list1Order.add("成功");
        list1Order.add("失败");
        list1Order.add("待定");
        list1Order.add("出错");
        list2Order.clear();
        list2Order.add("全部");
        list2Order.add("待审核");
        list2Order.add("审核通过");
        list2Order.add("审核不通过");
        imgArr.setBackgroundResource(R.drawable.arr_down);
        llCondition.setVisibility(View.GONE);


        LayoutInflater mLayoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition, null);
        viewHolder = new ViewHolder(music_popunwindwow);

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));

        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refresh = 1;
                search();
            }

            @Override
            public void onLoadmore() {
                page++;
                refresh = 2;
                search();
            }
        });
        springView.setHeader(new DefaultHeader(getActivity()));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultFooter(getActivity()));
//        refreshView.setOnRefreshListener(this);

        adapter.setOrderKHList(listOrderKh);
        adapter.setOrderGHList(orderGHList);
        adapter.setOrderCzList(orderCzList);
        adapter.setFlag(flag);
        contentView.setAdapter(adapter);
//        adapter.setArrKh();
//        recordAdapter = new RechargeAdapter(getActivity().getApplicationContext());
    }

//    @Override
//    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        page = 1;
//        refresh = 1;
//        this.pullToRefreshLayout = pullToRefreshLayout;
//        search();
//    }
//
//    @Override
//    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//        page++;
//        refresh = 2;
//        this.pullToRefreshLayout = pullToRefreshLayout;
//        search();
//    }

    private void gotoActy(Class clazz, String orderNo) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        intent.putExtra("orderNo", orderNo);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        pos = position;
        switch (flag) {
            case 0:
                flag = 0;
//                gotoActy(OrderKHDetailNewActivity.class, listOrderKh.get(position).getOrderNo());
                gotoActy(OrderKhDetailActivity.class, listOrderKh.get(position).getOrderNo());
                break;
            case 1:
                flag = 1;
                gotoActy(OrderKhDetailActivity.class, listOrderKh.get(position).getOrderNo());
                break;
            case 2:
                flag = 2;
                Intent intent = new Intent(getActivity().getApplicationContext(), OrderGhDetailActivity.class);
                intent.putExtra("orderNo", orderGHList.get(position).getId());
                intent.putExtra("time", orderGHList.get(position).getStartTime());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case 3:
                flag = 3;
                Intent intent1 = new Intent(getActivity().getApplicationContext(), OrderBkDetailActivity.class);
                intent1.putExtra("orderNo", orderGHList.get(position).getId());
                intent1.putExtra("time", orderGHList.get(position).getStartTime());
                getActivity().startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
        }
    }


    class ViewHolder {
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

        ViewHolder(View view) {
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
                    switch (flag) {
                        case 0:
                        case 1:
                            Util.createDialog(getActivity(), list0Order, tvState, null);
                            break;
                        case 2:
                        case 3:
                            Util.createDialog(getActivity(), list2Order, tvState, null);
                            break;
                        case 4:
                            Util.createDialog(getActivity(), list1Order, tvState, null);
                            break;
                    }

                    break;
                case R.id.tvReset:
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
                    if (strBegin.equals("请选择") && strEnd.equals("请选择") && strNumber.equals("") && strState.equals("请选择")) {
                        llCondition.setVisibility(View.GONE);
                        search();
                        mPopupWindow.dismiss();
                    } else if (Util.compare(strEnd, strBegin)) {
                        Util.createToast(getActivity(), "请输入正确的起止日期");
                    } else {
                        llCondition.setVisibility(View.VISIBLE);
                        tvBegin.setText("起始时间:" + Util.strBackNull(strBegin));
                        tvEnd.setText("截止时间:" + Util.strBackNull(strEnd));
                        tvDdState.setText("订单状态:" + Util.strBackNull(strState));
                        Log.d("ccc", Util.strBackNull(strNumber));
                        tvPhone.setText("手机号码:" + Util.strBackNull(strNumber));

                        search();
                        mPopupWindow.dismiss();
                    }

                    break;
            }
        }
    }

    public void search() {
        String type = "";
        String statusCode = "";
        switch (flag) {
            case 0:
                type = "SIM";
                break;
            case 1:
                type = "ESIM";
                break;
            case 2:
                type = "0";
                break;
            case 3:
                type = "1";
                break;
        }
        switch (flag) {
            case 0:
            case 1:
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
                }
                HttpPost<PostOrderList> httpPost = new HttpPost<>();
                PostOrderList postOrderList = new PostOrderList();
                postOrderList.setSession_token(Util.getLocalAdmin(getActivity())[0]);
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
                new OrderHttp().orderKhList(OrderRequest.orderKhList(getActivity(), dialog, new SuccessValue<RequestOrderList>() {
                    @Override
                    public void OnSuccess(RequestOrderList value) {
//                        arrKh = value.getOrder();
//                        Log.d("aaa", value.getOrder().length + "");
                        switch (refresh) {
                            case 0:
                                listOrderKh.clear();
                                break;
                            case 1:
                                listOrderKh.clear();
                                springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                break;
                        }
                        refresh = 0;
                        listOrderKh.addAll(Arrays.asList(arrKh));
                        adapter.setFlag(flag);
                        adapter.notifyDataSetChanged();
                        tvNumber.setText(String.valueOf("共" + listOrderKh.size() + "条"));


                    }
                }), httpPost);

                break;
            case 2:
            case 3:
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
                HttpPost<PostTransferList> httpPost1 = new HttpPost<>();
                PostTransferList postTransferList = new PostTransferList();
                postTransferList.setSession_token(Util.getLocalAdmin(getActivity())[0]);
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
                    postTransferList.setStartCode(statusCode);
                    postTransferList.setStartName(strState);
                }
                postTransferList.setType(type);
                postTransferList.setPage(String.valueOf(page));
                postTransferList.setLinage(String.valueOf(BaseCom.linage));
                httpPost1.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost1.setParameter(postTransferList);
                httpPost1.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postTransferList) + BaseCom.APP_PWD));
                new OrderHttp().orderTransferList(OrderRequest.orderTransferList(getActivity(), dialog, new SuccessValue<RequestTransferList>() {
                    @Override
                    public void OnSuccess(RequestTransferList value) {
//                        arrGh = value.getOrder();
//                        Log.d("aaa", value.getOrder().length + "");
                        switch (refresh) {
                            case 0:
                                orderGHList.clear();
                                break;
                            case 1:
                                orderGHList.clear();
                                springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                break;
                        }
                        refresh = 0;
                        orderGHList.addAll(Arrays.asList(arrGh));
                        adapter.setFlag(flag);
                        adapter.notifyDataSetChanged();
                        tvNumber.setText(String.valueOf("共" + orderGHList.size() + "条"));
                    }
                }), httpPost1);
                break;
            case 4:
                HttpPost<PostRechargeList> httpPost2 = new HttpPost<>();
                PostRechargeList postRechargeList = new PostRechargeList();
                postRechargeList.setSession_token(Util.getLocalAdmin(getActivity())[0]);
                if (Util.isNull(strNumber)) {
                    postRechargeList.setNumber(strNumber);
                }
                if (Util.isNull(strBegin)) {
                    postRechargeList.setStartTime(strBegin);
                }
                if (Util.isNull(strEnd)) {
                    postRechargeList.setEndTime(strEnd);
                }
                postRechargeList.setRechargeType("1");
                postRechargeList.setPage(String.valueOf(page));
                postRechargeList.setLinage(String.valueOf(BaseCom.linage));
                httpPost2.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost2.setParameter(postRechargeList);
                httpPost2.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRechargeList) + BaseCom.APP_PWD));
                Log.d("aaa", gson.toJson(httpPost2));
                new OrderHttp().orderRechargeList(OrderRequest.orderRechargeList(getActivity(), dialog, new SuccessValue<RequestRechargeList>() {
                    @Override
                    public void OnSuccess(RequestRechargeList value) {
//                        arrCz = value.getOrder();
//                        Log.d("aaa", value.getOrder().length + "");
                        switch (refresh) {
                            case 0:
                                orderCzList.clear();
                                break;
                            case 1:
                                orderCzList.clear();
                                springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                springView.onFinishFreshAndLoad();
//                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                break;
                        }
                        refresh = 0;
                        orderCzList.addAll(Arrays.asList(arrCz));
                        adapter.setFlag(flag);
                        adapter.notifyDataSetChanged();
                        tvNumber.setText(String.valueOf("共" + orderCzList.size() + "条"));

                    }
                }), httpPost2);
                break;
        }
    }

    public void clearData() {
        strBegin = "请选择";
        strEnd = "请选择";
        strNumber = "";
        strState = "请选择";
    }

    @OnClick({R.id.llCkkh, R.id.llBkkh, R.id.llGh, R.id.llBk, R.id.llCz, R.id.llArr, R.id.llCondition})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llCkkh:
                page = 1;
                flag = 0;
                refresh = 0;
                setClickState();
                Util.saveClickTime(getActivity(), BaseCom.orderQueryRenew);
                break;
            case R.id.llBkkh:
                page = 1;
                flag = 1;
                refresh = 0;
                setClickState();
                Util.saveClickTime(getActivity(), BaseCom.orderQueryNew);
                break;
            case R.id.llGh:
                page = 1;
                flag = 2;
                refresh = 0;
                setClickState();
                Util.saveClickTime(getActivity(), BaseCom.orderQueryTransform);
                break;
            case R.id.llBk:
                page = 1;
                flag = 3;
                refresh = 0;
                setClickState();
                Util.saveClickTime(getActivity(), BaseCom.orderQueryReplace);
                break;
            case R.id.llCz:
                page = 1;
                flag = 4;
                refresh = 0;
                setClickState();
                Util.saveClickTime(getActivity(), BaseCom.orderQueryRecharge);
                break;
            case R.id.llArr:
                imgArr.setBackgroundResource(R.drawable.arr_up);
                contentView.setAlpha((float) 0.2);
                viewHolder.tvPopBegin.setText(strBegin);
                viewHolder.tvPopEnd.setText(strEnd);
                viewHolder.tvState.setText(strState);
                viewHolder.etNumber.setText(strNumber);
                if (flag == 4) {
                    viewHolder.llPhoneNumber.setVisibility(View.GONE);
                    viewHolder.linePhoneNumber.setVisibility(View.GONE);
                    viewHolder.llState.setVisibility(View.GONE);
                    viewHolder.vLine1.setVisibility(View.GONE);
                } else {
                    viewHolder.llPhoneNumber.setVisibility(View.VISIBLE);
                    viewHolder.linePhoneNumber.setVisibility(View.VISIBLE);
                    viewHolder.llState.setVisibility(View.VISIBLE);
                    viewHolder.vLine1.setVisibility(View.VISIBLE);
                }
                mPopupWindow.showAsDropDown(vLine, 0, 0);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        contentView.setAlpha((float) 1);
                        imgArr.setBackgroundResource(R.drawable.arr_down);
                    }
                });
                break;
            case R.id.llCondition:
                break;
        }
    }

    /*清除选中状态*/
    public void clearClickState() {
        tvCkkh.setTextColor(getResources().getColor(R.color.colorGray1));
        tvBkkh.setTextColor(getResources().getColor(R.color.colorGray1));
        tvGh.setTextColor(getResources().getColor(R.color.colorGray1));
        tvBk.setTextColor(getResources().getColor(R.color.colorGray1));
        tvCz.setTextColor(getResources().getColor(R.color.colorGray1));
        vCkkh.setVisibility(View.INVISIBLE);
        vBkkh.setVisibility(View.INVISIBLE);
        vBk.setVisibility(View.INVISIBLE);
        vGh.setVisibility(View.INVISIBLE);
        vCz.setVisibility(View.INVISIBLE);
        clearData();
        llCondition.setVisibility(View.GONE);
        search();
    }

    public void setClickState() {
        clearClickState();
        switch (flag) {
            case 0:
                tvCkkh.setTextColor(getResources().getColor(R.color.colorOrange));
                vCkkh.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvBkkh.setTextColor(getResources().getColor(R.color.colorOrange));
                vBkkh.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvGh.setTextColor(getResources().getColor(R.color.colorOrange));
                vGh.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvBk.setTextColor(getResources().getColor(R.color.colorOrange));
                vBk.setVisibility(View.VISIBLE);
                break;
            case 4:
                tvCz.setTextColor(getResources().getColor(R.color.colorOrange));
                vCz.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void showDatePikerDialog(final int click_flag, final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(getActivity(), "选择日期", 0, new DoubleDatePickerDialog.OnDateSetListener() {
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

}
