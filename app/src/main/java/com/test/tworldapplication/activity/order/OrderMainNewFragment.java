package com.test.tworldapplication.activity.order;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.home.YewuListActivity;
import com.test.tworldapplication.adapter.AccountAdapter;
import com.test.tworldapplication.entity.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/12/1.
 */

public class OrderMainNewFragment extends Fragment implements AdapterView.OnItemClickListener {


    @BindView(R.id.order_main_listview)
    ListView orderMainListview;
    List<Account> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View orderLayout = inflater.inflate(R.layout.fragment_order_main, container, false);
        ButterKnife.bind(this, orderLayout);
        initView();

        return orderLayout;
    }

    private void initView() {
        list.clear();

        list.add(new Account(R.mipmap.order_ckkh, "成卡开户订单"));
        list.add(new Account(R.mipmap.order_bkkh, "白卡开户订单"));
        list.add(new Account(R.mipmap.order_gh, "过户订单"));
        list.add(new Account(R.mipmap.order_bk, "补卡订单"));
        list.add(new Account(R.mipmap.order_ywdd, "业务订单"));
        list.add(new Account(R.mipmap.order_zhcz, "账户充值查询"));
//        list.add(new Account(R.mipmap.product_order, "流量包"));
        list.add(new Account(R.mipmap.card_bkyk, "白卡申请订单"));
        list.add(new Account(R.mipmap.order_icon_ownership_small, "预订单"));
        list.add(new Account(R.mipmap.order_icon_establish_small, "写卡激活订单"));
        orderMainListview.setAdapter(new AccountAdapter(getActivity().getApplicationContext(), list));
        orderMainListview.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                gotoActy("0");
                break;
            case 1:
                gotoActy("1");
                break;
            case 2:
                gotoActy("2");
                break;
            case 3:
                gotoActy("3");
                break;
            case 4:
                Intent intent = new Intent(getContext(), YewuListActivity.class);
                startActivity(intent);
                break;
            case 5:
                gotoActy("5");
                break;
//            case 6:
//                Intent intent0 = new Intent(getContext(), ProductRecordActivity.class);
//                startActivity(intent0);
////                gotoActy("5");
//                break;
            case 6:
                Intent intent1 = new Intent(getContext(), BkOrderListActivity.class);
                startActivity(intent1);
                break;
            case 7:
                gotoActy("6");
                break;
            case 8:
                gotoActy("7");
                break;
        }
    }

    private void gotoActy(String from) {
        Intent intent = new Intent(getActivity(), OrderKhListActivity.class);
        intent.putExtra("from", from);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}