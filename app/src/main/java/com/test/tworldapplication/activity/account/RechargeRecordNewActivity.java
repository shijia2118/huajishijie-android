package com.test.tworldapplication.activity.account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.OrderNewAdapter;
import com.test.tworldapplication.adapter.RechargeAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.OrderCz;
import com.test.tworldapplication.entity.PostRechargeList;
import com.test.tworldapplication.entity.RequestRechargeList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.DoubleDatePickerDialog;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeRecordNewActivity extends BaseActivity implements DialogInterface.OnKeyListener {

    @BindView(R.id.imgArr)
    ImageView imgArr;
    @BindView(R.id.llArr)
    LinearLayout llArr;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.llCondition)
    LinearLayout llCondition;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    //    @BindView(R.id.ivRecord)
//    List<String> list1Order = new ArrayList<>();
//    RechargeAdapter recordAdapter;
    OrderNewAdapter adapter;
    int flag = 5;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBegin)
    TextView tvBegin;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    @BindView(R.id.tvOrderState)
    TextView tvOrderState;
    //    @BindView(R.id.content_view)
//    PullableListView contentView;
//    @BindView(R.id.refresh_view)
//    PullToRefreshLayout refreshView;
    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.shadow_view)
    View shadowView;
    List<OrderCz> orderCzList = new ArrayList<>();

    private OrderCz[] arrCz = null;
    //    private OrderCz[] arrCz_true = null;
    int page = 1;
    int linage = 10;
    int refresh = 0;
    //    PullToRefreshLayout pullToRefreshLayout;
    ViewHolder viewHolder;
    PopupWindow mPopupWindow;
    private String strBegin = "", strEnd = "", strNumber = "";

    //建议使用三个list 可以去掉flag
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_new_record);
        ButterKnife.bind(this);
        init();
        setBackGroundTitle("账户余额充值查询", true);
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
        springView.setHeader(new DefaultHeader(RechargeRecordNewActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultFooter(RechargeRecordNewActivity.this));
        adapter = new OrderNewAdapter(RechargeRecordNewActivity.this);
        adapter.setFlag(flag);
        adapter.setOrderCzList(orderCzList);
        contentView.setAdapter(adapter);
//        recordAdapter = new RechargeAdapter(RechargeRecordNewActivity.this);
    }

    public void init() {

        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(
                R.layout.view_pop_condition_two, null);
        viewHolder = new ViewHolder(view);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));


    }

    class ViewHolder {
        @BindView(R.id.tvPopBegin)
        TextView tvPopBegin;
        @BindView(R.id.llBegin)
        LinearLayout llBegin;
        @BindView(R.id.tvPopEnd)
        TextView tvPopEnd;
        @BindView(R.id.llEnd)
        LinearLayout llEnd;

        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.tvInquery)
        TextView tvInquery;
        @BindView(R.id.tvReset)
        TextView tvReset;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.llBegin, R.id.llEnd, R.id.tvInquery, R.id.tvReset})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llBegin:
                    showDatePikerDialog(tvPopBegin);
                    break;
                case R.id.llEnd:
                    showDatePikerDialog(tvPopEnd);
                    break;
                case R.id.tvInquery:
                    strBegin = tvPopBegin.getText().toString().trim();
                    strEnd = tvPopEnd.getText().toString().trim();
                    strNumber = etNumber.getText().toString().trim();
                    if (strBegin.equals("请选择") && strEnd.equals("请选择") && strNumber.equals("")) {
                        llCondition.setVisibility(View.GONE);
                        mPopupWindow.dismiss();
                        search();
                    } else if (Util.compare(strEnd, strBegin)) {
                        Util.createToast(RechargeRecordNewActivity.this, "请输入正确的起止日期");
                    } else {
                        tvBegin.setText("起始时间:" + Util.strBackNull(strBegin));
                        tvEnd.setText("截止时间:" + Util.strBackNull(strEnd));
                        tvOrderState.setText("手机号码:" + Util.strBackNull(strNumber));

                        llCondition.setVisibility(View.VISIBLE);
                        mPopupWindow.dismiss();

                        search();
                    }
                    break;
                case R.id.tvReset:
                    tvPopBegin.setText("请选择");
                    tvPopEnd.setText("请选择");
                    etNumber.setText("");
                    break;
            }
        }
    }

    private void showDatePikerDialog(final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(this, "选择日期", 0, new DoubleDatePickerDialog.OnDateSetListener() {
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
    protected void onResume() {
        super.onResume();
        strBegin = "请选择";
        strEnd = "请选择";
        strNumber = "";
        imgArr.setBackgroundResource(R.drawable.arr_down);
        llCondition.setVisibility(View.GONE);
        search();

    }

    public void search() {
        dialog.getTvTitle().setText("正在获取数据!");
//        dialog.show();
        String s = "0";

        HttpPost<PostRechargeList> httpPost2 = new HttpPost<>();
        PostRechargeList postRechargeList = new PostRechargeList();
        postRechargeList.setSession_token(Util.getLocalAdmin(RechargeRecordNewActivity.this)[0]);
        if (Util.isNull(strNumber)) {
            postRechargeList.setNumber(strNumber);
        }
        if (Util.isNull(strBegin)) {
            postRechargeList.setStartTime(strBegin);
        }
        if (Util.isNull(strEnd)) {
            postRechargeList.setEndTime(strEnd);
        }
        postRechargeList.setRechargeType(s);
        postRechargeList.setPage(String.valueOf(page));
        postRechargeList.setLinage(String.valueOf(linage));
        httpPost2.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost2.setParameter(postRechargeList);
        httpPost2.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRechargeList) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost2));
        new OrderHttp().orderRechargeList(OrderRequest.orderRechargeList(RechargeRecordNewActivity.this, dialog, new SuccessValue<RequestRechargeList>() {
            @Override
            public void OnSuccess(RequestRechargeList value) {
//                arrCz = value.getOrder();
                switch (refresh) {
                    case 0:
                        orderCzList.clear();
                        break;
                    case 1:
                        orderCzList.clear();
                        springView.onFinishFreshAndLoad();
                        break;
                    case 2:
                        springView.onFinishFreshAndLoad();
                        break;
                }
                refresh = 0;
                orderCzList.addAll(Arrays.asList(arrCz));
                adapter.setFlag(flag);
                adapter.notifyDataSetChanged();
                tvNumber.setText(String.valueOf("共" + orderCzList.size() + "条"));

            }
        }), httpPost2);
    }

    @OnClick(R.id.llArr)
    public void onClick() {
        imgArr.setBackgroundResource(R.drawable.arr_up);
//        contentView.setAlpha((float) 0.2);
        shadowView.setVisibility(View.VISIBLE);
        mPopupWindow.showAsDropDown(vLine, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                contentView.setAlpha((float) 1);
                shadowView.setVisibility(View.GONE);
                imgArr.setBackgroundResource(R.drawable.arr_down);
            }
        });

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
}
