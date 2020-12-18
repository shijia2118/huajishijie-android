package com.test.tworldapplication.activity.card;

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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.order.OrderGhDetailActivity;
import com.test.tworldapplication.adapter.ProgressAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.OrderGH;
import com.test.tworldapplication.entity.PostTransferList;
import com.test.tworldapplication.entity.RequestTransferList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.OnOrderQueryListener;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;
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

public class ProgressQueryActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {

    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.vAll)
    View vAll;
    @BindView(R.id.llAll)
    LinearLayout llAll;
    @BindView(R.id.tvWait)
    TextView tvWait;
    @BindView(R.id.vWait)
    View vWait;
    @BindView(R.id.llWait)
    LinearLayout llWait;
    @BindView(R.id.tvPass)
    TextView tvPass;
    @BindView(R.id.vPass)
    View vPass;
    @BindView(R.id.llPass)
    LinearLayout llPass;
    @BindView(R.id.tvNotPass)
    TextView tvNotPass;
    @BindView(R.id.vNotPass)
    View vNotPass;
    @BindView(R.id.llNotPass)
    LinearLayout llNotPass;
    @BindView(R.id.imgArr)
    ImageView imgArr;
    @BindView(R.id.llArr)
    LinearLayout llArr;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.llCondition)
    LinearLayout llCondition;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    ProgressAdapter adapter;
    List<String> list1Order = new ArrayList<>();
    @BindView(R.id.tvBegin)
    TextView tvBegin;
    @BindView(R.id.tbEnd)
    TextView tbEnd;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    String mStateCode = "0";
    int page = 1;
    int linage = 10;
    @BindView(R.id.content_view)
    PullableListView ivProgress;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    int refresh = 0;
    PullToRefreshLayout pullToRefreshLayout;
    OrderGH[] arrOrderGh;
    List<OrderGH> orderGHList = new ArrayList<>();
    //    OrderGH[] arrOrderGh_true;
    ViewHolder viewHolder;
    PopupWindow mPopupWindow;
    String strBegin = "请选择", strEnd = "请选择", strNumber = "", strState = "过户";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_query);
        ButterKnife.bind(this);
        setBackGroundTitle("过户、补卡状态查询", true);
        init();
        dialog.getTvTitle().setText("正在查询");
        adapter = new ProgressAdapter(ProgressQueryActivity.this);
        adapter.setOrderGHList(orderGHList);
        adapter.setType("过户");
        ivProgress.setAdapter(adapter);
        refreshView.setOnRefreshListener(this);
        ivProgress.setOnItemClickListener(this);

    }

    public void init() {
        list1Order.clear();
        list1Order.add("补卡");
        list1Order.add("过户");
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(
                R.layout.view_pop_condition, null);
        viewHolder = new ViewHolder(view);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearData();

        imgArr.setBackgroundResource(R.drawable.arr_down);
        Util.setValue("请选择", "请选择", "过户", "", "账户充值");
        llCondition.setVisibility(View.GONE);
        search();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, OrderGhDetailActivity.class);
        intent.putExtra("orderNo", arrOrderGh[position].getId());
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @OnClick({R.id.llAll, R.id.llWait, R.id.llPass, R.id.llNotPass})
    public void onClick(View view) {
        clearClickState();
        page = 1;
        switch (view.getId()) {
            case R.id.llAll:
                clearData();
                mStateCode = "0";
                refresh = 0;
                search();
                tvAll.setTextColor(getResources().getColor(R.color.colorOrange));
                vAll.setVisibility(View.VISIBLE);
                llCondition.setVisibility(View.GONE);
                break;
            case R.id.llWait:
                clearData();
                mStateCode = "1";
                refresh = 0;
                search();
                tvWait.setTextColor(getResources().getColor(R.color.colorOrange));
                vWait.setVisibility(View.VISIBLE);
                llCondition.setVisibility(View.GONE);
                break;
            case R.id.llPass:
                clearData();
                mStateCode = "2";
                refresh = 0;
                search();
                tvPass.setTextColor(getResources().getColor(R.color.colorOrange));
                vPass.setVisibility(View.VISIBLE);
                llCondition.setVisibility(View.GONE);
                break;
            case R.id.llNotPass:
                clearData();
                mStateCode = "3";
                refresh = 0;
                search();
                tvNotPass.setTextColor(getResources().getColor(R.color.colorOrange));
                vNotPass.setVisibility(View.VISIBLE);
                llCondition.setVisibility(View.GONE);
                break;
        }

    }

    public void clearData() {
        strNumber = "";
        strBegin = "请选择";
        strEnd = "请选择";
        strState = "过户";
    }

    @OnClick(R.id.llArr)
    public void onClick() {
        imgArr.setBackgroundResource(R.drawable.arr_up);
        ivProgress.setAlpha((float) 0.2);

        viewHolder.etNumber.setText(strNumber);
        viewHolder.tvPopBegin.setText(strBegin);
        viewHolder.tvPopEnd.setText(strEnd);
        viewHolder.tvState.setText(strState);
        mPopupWindow.showAsDropDown(vLine, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivProgress.setAlpha((float) 1);
                imgArr.setBackgroundResource(R.drawable.arr_down);
            }
        });

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
        @BindView(R.id.tvState)
        TextView tvState;

        @BindView(R.id.tvTitleState)
        TextView tvTitleState;
        @BindView(R.id.llState)
        LinearLayout llState;
        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.tvInquery)
        TextView tvInquery;
        @BindView(R.id.tvReset)
        TextView tvReset;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            tvTitleState.setText("业务类型");
        }

        @OnClick({R.id.llBegin, R.id.llEnd, R.id.llState, R.id.tvInquery, R.id.tvReset})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llBegin:
                    showDatePikerDialog(tvPopBegin);
                    break;
                case R.id.llEnd:
                    showDatePikerDialog(tvPopEnd);
                    break;
                case R.id.llState:
                    Util.createDialog(ProgressQueryActivity.this, list1Order, tvState, null);
                    break;
                case R.id.tvInquery:
                    strBegin = tvPopBegin.getText().toString().trim();
                    strEnd = tvPopEnd.getText().toString().trim();
                    strNumber = etNumber.getText().toString().trim();
                    strState = tvState.getText().toString().trim();
                    if (Util.compare(strEnd, strBegin)) {
                        Util.createToast(ProgressQueryActivity.this, "请输入正确的起止日期");
                    } else {
                        tvBegin.setText("起始时间:" + Util.strBackNull(strBegin));
                        tbEnd.setText("截止时间:" + Util.strBackNull(strEnd));
                        tvPhone.setText("手机号码:" + Util.strBackNull(strNumber));
                        tvType.setText("业务类型:" + Util.strBackNull(strState));

                        llCondition.setVisibility(View.VISIBLE);
                        mPopupWindow.dismiss();
                        search();
                    }

                    break;
                case R.id.tvReset:
                    tvPopBegin.setText("请选择");
                    tvPopEnd.setText("请选择");
                    tvNumber.setText("");
                    tvState.setText("请选择");
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

    public void search() {
        Log.d("ddd", "search");
        String strStateName = "";
        if (mStateCode.equals("1")) {
            strStateName = "待审核";
        } else if (mStateCode.equals("2")) {
            strStateName = "审核通过";
        } else if (mStateCode.equals("3")) {
            strStateName = "审核不通过";
        }
        String type = "0";
        switch (strState) {
            case "过户":
                type = "0";
                break;
            case "补卡":
                type = "1";
                break;
        }
        HttpPost<PostTransferList> httpPost = new HttpPost<>();
        PostTransferList postTransferList = new PostTransferList();
        postTransferList.setSession_token(Util.getLocalAdmin(ProgressQueryActivity.this)[0]);
        if (Util.isNull(strNumber)) {
            postTransferList.setNumber(strNumber);
        }
        if (Util.isNull(strBegin)) {
            postTransferList.setStartTime(strBegin);
        }
        if (Util.isNull(strEnd)) {
            postTransferList.setEndTime(strEnd);
        }
        if (!mStateCode.equals("0")) {
            postTransferList.setStartCode(mStateCode);
            postTransferList.setStartName(strStateName);
        }
        postTransferList.setType(type);

        postTransferList.setPage(String.valueOf(page));
        postTransferList.setLinage(String.valueOf(linage));
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postTransferList);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postTransferList) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().orderTransferList(OrderRequest.orderTransferList(ProgressQueryActivity.this, dialog, new SuccessValue<RequestTransferList>() {
            @Override
            public void OnSuccess(RequestTransferList value) {
//                arrOrderGh = value.getOrder();
                switch (refresh) {
                    case 0:
                        orderGHList.clear();
                        break;
                    case 1:
                        orderGHList.clear();
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        break;
                    case 2:
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        break;
                }
                refresh = 0;
                orderGHList.addAll(Arrays.asList(arrOrderGh));
                adapter.setType(strState);
                adapter.notifyDataSetChanged();
                tvNumber.setText(String.valueOf("共" + orderGHList.size() + "条"));
            }
        }), httpPost);

    }

    public void clearClickState() {
        tvAll.setTextColor(getResources().getColor(R.color.colorGray1));
        tvWait.setTextColor(getResources().getColor(R.color.colorGray1));
        tvPass.setTextColor(getResources().getColor(R.color.colorGray1));
        tvNotPass.setTextColor(getResources().getColor(R.color.colorGray1));
        vAll.setVisibility(View.INVISIBLE);
        vWait.setVisibility(View.INVISIBLE);
        vPass.setVisibility(View.INVISIBLE);
        vNotPass.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        refresh = 1;
        this.pullToRefreshLayout = pullToRefreshLayout;
        search();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        refresh = 2;
        this.pullToRefreshLayout = pullToRefreshLayout;
        search();

    }


}
