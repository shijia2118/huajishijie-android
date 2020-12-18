package com.test.tworldapplication.activity.account;

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
import com.test.tworldapplication.adapter.AccountAdapter;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Account;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountMainFragment extends Fragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.account_main_listview)
    ListView accountMainListview;
    List<Account> list = new ArrayList<Account>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View orderLayout = inflater.inflate(R.layout.activity_account_main, container, false);
        ButterKnife.bind(this, orderLayout);
        initView();
        accountMainListview.setOnItemClickListener(this);
        return orderLayout;
    }

    private void initView() {
        list.clear();
        list.add(new Account(R.mipmap.zhcxycz, "账户查询与充值"));
//        list.add(new Account(R.mipmap.czcx, "账户充值查询"));
        accountMainListview.setAdapter(new AccountAdapter(getActivity().getApplicationContext(), list));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {

            case 0:
                Util.saveClickTime(getActivity(), BaseCom.accountRecharge);
                gotoActy(AccountBalanceActivity.class);
                break;

//            case 1:
//                Util.saveClickTime(getActivity(), BaseCom.accountRecord);
//                gotoActy(RechargeRecordNewActivity.class);
//                break;
        }
    }

    private void gotoActy(Class clazz) {
        Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }


}
