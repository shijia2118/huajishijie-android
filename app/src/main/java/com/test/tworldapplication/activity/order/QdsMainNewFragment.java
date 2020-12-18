package com.test.tworldapplication.activity.order;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.QdsNewAdapter;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.ChannelsOpen;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.Organization;
import com.test.tworldapplication.entity.PostChannelsList;
import com.test.tworldapplication.entity.PostChannelsOpenCount;
import com.test.tworldapplication.entity.Qds;
import com.test.tworldapplication.entity.QdsOrder;
import com.test.tworldapplication.entity.RequestChannelOpenCount;
import com.test.tworldapplication.entity.RequestChannelsList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by dasiy on 16/10/27.
 */

public class QdsMainNewFragment extends Fragment implements PullToRefreshLayout.OnRefreshListener, DialogInterface.OnKeyListener {
    View qdsLayout;
    @BindView(R.id.tvQds)
    TextView tvQds;
    @BindView(R.id.vQds)
    View vQds;
    @BindView(R.id.llQds)
    LinearLayout llQds;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.vOrder)
    View vOrder;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.imgArr)
    ImageView imgArr;
    @BindView(R.id.llArr)
    LinearLayout llArr;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llCondition)
    LinearLayout llCondition;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.ll_main_order)
    LinearLayout llMainOrder;
    List<Qds> qdsList = new ArrayList<>();
    List<QdsOrder> qdsOrderList = new ArrayList<>();
    QdsNewAdapter adapter = null;
    List<String> list = new ArrayList<>();
    Gson gson = new Gson();
    CheckResultDialog dialog;
    Organization[] organizations;
    ChannelsOpen[] channelsOpens;

    List<Organization> organizationList = new ArrayList<>();
    List<ChannelsOpen> channelsOpenList = new ArrayList<>();

    @BindView(R.id.content_view)
    PullableListView contentView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    @BindView(R.id.shadow_view)
    View shadowView;
    int flag = 0;
    int page = 1;
    int linage = 10;
    int refresh = 0;
        PopupWindow mPopupWindow;
    ViewHolder viewHolder;
    PullToRefreshLayout pullToRefreshLayout;
    String strState = "", strNumber = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        qdsLayout = inflater.inflate(R.layout.activity_qds_main, container, false);
        ButterKnife.bind(this, qdsLayout);
        init();
        refreshView.setOnRefreshListener(this);
        adapter = new QdsNewAdapter(getActivity());
        adapter.setChannelsOpenList(channelsOpenList);
        adapter.setOrganizationList(organizationList);
        contentView.setAdapter(adapter);
        return qdsLayout;
    }

    public void init() {
        list.clear();
        list.add("一个月");
        list.add("两个月");
        list.add("三个月");
        list.add("四个月");
        list.add("五个月");
        list.add("六个月");
        dialog = new CheckResultDialog(getActivity(), R.style.CustomDialog);
        dialog.getTvTitle().setText("正在请求数据");
        dialog.setOnKeyListener(this);
        LayoutInflater mLayoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_one, null);
        viewHolder = new ViewHolder(music_popunwindwow);
        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
    }


    class ViewHolder {
        @BindView(R.id.tvState)
        TextView tvState;
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
        }

        @OnClick({R.id.llState, R.id.tvInquery, R.id.tvReset})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llState:
                    Util.createDialog(getActivity(), list, tvState, null);
                    break;
                case R.id.tvInquery:
                    strState = tvState.getText().toString().trim();
                    strNumber = etNumber.getText().toString().trim();
                    if (strState.equals("请选择") && strNumber.equals("")) {
                        llCondition.setVisibility(View.GONE);
                    } else {
//                        tvTime.setText(strState);
//                        tvPhone.setText(strNumber);
                        tvTime.setText("时间段:" + Util.strBackNull(strState));
                        tvPhone.setText("工号:" + Util.strBackNull(strNumber));
                        llCondition.setVisibility(View.VISIBLE);


                    }
                    mPopupWindow.dismiss();

                    searchOrder();
//                    if ()
                    break;
                case R.id.tvReset:
                    tvState.setText("请选择");
                    etNumber.setText("");
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPopupWindow.dismiss();

        imgArr.setBackgroundResource(R.drawable.arr_down);
        qdsList.clear();

        searchQDS();
        qdsOrderList.clear();
        strState = "请选择";
        strNumber = "";
    }

    public void searchQDS() {
//        if (Util.isLog(getActivity())) {
        dialog.show();
        HttpPost<PostChannelsList> httpPost = new HttpPost<>();
        PostChannelsList postChannelsList = new PostChannelsList();
        postChannelsList.setPage(String.valueOf(page));
        postChannelsList.setLinage(String.valueOf(linage));
        postChannelsList.setSession_token(Util.getLocalAdmin(getActivity())[0]);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postChannelsList);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postChannelsList) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().channelsList(OrderRequest.channelsList(getActivity(), dialog, new SuccessValue<RequestChannelsList>() {
            @Override
            public void OnSuccess(RequestChannelsList value) {
                organizations = value.getChannels();
                switch (refresh) {
                    case 0:
                        organizationList.clear();
                        break;
                    case 1:
                        organizationList.clear();
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        break;
                    case 2:
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        break;
                }
                refresh = 0;
                organizationList.addAll(Arrays.asList(organizations));
                adapter.setFlag(flag);
                adapter.notifyDataSetChanged();
                tvNumber.setText(String.valueOf("共" + organizationList.size() + "条"));


            }
        }), httpPost);
    }
//    }

    public void searchOrder() {
        dialog.show();
        HttpPost<PostChannelsOpenCount> httpPost = new HttpPost<>();
        PostChannelsOpenCount postChannelsOpenCount = new PostChannelsOpenCount();
        postChannelsOpenCount.setSession_token(Util.getLocalAdmin(getActivity())[0]);
        String strOrderState = "";
        switch (strState) {
            case "一个月":
                strOrderState = "1";
                break;
            case "两个月":
                strOrderState = "2";
                break;
            case "三个月":
                strOrderState = "3";
                break;
            case "四个月":
                strOrderState = "4";
                break;
            case "五个月":
                strOrderState = "5";
                break;
            case "六个月":
                strOrderState = "6";
                break;
        }
        if (Util.isNull(strState)) {
            postChannelsOpenCount.setOpenTime(Integer.valueOf(strOrderState));
        }
        if (Util.isNull(strNumber)) {
            postChannelsOpenCount.setOrgCode(strNumber);
        }
        postChannelsOpenCount.setPage(String.valueOf(page));
        postChannelsOpenCount.setLinage(String.valueOf(linage));
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postChannelsOpenCount);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postChannelsOpenCount) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));

        new OrderHttp().channelsOpenCount(OrderRequest.channelsOpenCount(getActivity(), dialog, new SuccessValue<RequestChannelOpenCount>() {
            @Override
            public void OnSuccess(RequestChannelOpenCount value) {


                channelsOpens = value.getChannelsOpenCount();
                switch (refresh) {
                    case 0:
                        channelsOpenList.clear();
                        break;
                    case 1:
                        channelsOpenList.clear();
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        break;
                    case 2:
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        break;
                }
                refresh = 0;
                channelsOpenList.addAll(Arrays.asList(channelsOpens));
                adapter.setFlag(flag);
                adapter.notifyDataSetChanged();
                tvNumber.setText(String.valueOf("共" + channelsOpenList.size() + "条"));

            }
        }), httpPost);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.llQds, R.id.llOrder})
    public void onClick(View view) {
        clearClickState();
        qdsList.clear();
        qdsOrderList.clear();
        switch (view.getId()) {
            case R.id.llQds:
                page = 1;
                flag = 0;
                refresh = 0;
                strState = "请选择";
                strNumber = "";
                tvQds.setTextColor(getResources().getColor(R.color.colorOrange));
                vQds.setVisibility(View.VISIBLE);
                llArr.setVisibility(View.GONE);
                llCondition.setVisibility(View.GONE);
                vLine.setVisibility(View.GONE);
                searchQDS();
                llCondition.setVisibility(View.GONE);
                Util.saveClickTime(getActivity(), BaseCom.qdsList);
                break;
            case R.id.llOrder:
                page = 1;
                flag = 1;
                refresh = 0;
                strState = "请选择";
                strNumber = "";
                tvOrder.setTextColor(getResources().getColor(R.color.colorOrange));
                vOrder.setVisibility(View.VISIBLE);
                llArr.setVisibility(View.VISIBLE);
                vLine.setVisibility(View.VISIBLE);
                searchOrder();
                llCondition.setVisibility(View.GONE);
                Util.saveClickTime(getActivity(), BaseCom.qdsOrderList);
                break;

        }
    }

    public void clearClickState() {
        tvQds.setTextColor(getResources().getColor(R.color.colorGray1));
        tvOrder.setTextColor(getResources().getColor(R.color.colorGray1));
        vOrder.setVisibility(View.INVISIBLE);
        vQds.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.llArr)
    public void onClick() {
        imgArr.setBackgroundResource(R.drawable.arr_up);

        viewHolder.tvState.setText(strState);
        viewHolder.etNumber.setText(strNumber);
//        contentView.setAlpha((float) 0.2);
        shadowView.setVisibility(View.VISIBLE);
        mPopupWindow.showAsDropDown(vLine, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shadowView.setVisibility(View.GONE);
//                contentView.setAlpha((float) 1);
                imgArr.setBackgroundResource(R.drawable.arr_down);
            }
        });

    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        refresh = 1;
        this.pullToRefreshLayout = pullToRefreshLayout;
        switch (flag) {
            case 0:
                searchQDS();
                break;
            case 1:
                searchOrder();
                break;
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        refresh = 2;
        this.pullToRefreshLayout = pullToRefreshLayout;
        switch (flag) {
            case 0:
                searchQDS();
                break;
            case 1:
                searchOrder();
                break;
        }
    }
}
