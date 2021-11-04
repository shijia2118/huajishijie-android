package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestPreNumberDetails;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class PackageSelectDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gvSelect)
    GridView gvSelect;
    //    List<Package> list = new ArrayList<>();
    int pos;
    MyAdapter adapter;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    @BindView(R.id.tvSure)
    TextView tvSure;
    Package[] packages;
    RequestCheck requestCheck;
    RequestImsi requestImsi = null;
    AgentsLiangNumber agentsLiangNumber;
    String flag = "";
    RequestPreNumberDetails requestPreNumberDetails;
    /*0成卡1白卡点击grideview传递回去pos*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_select_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("套餐选择", true);
        flag = getIntent().getStringExtra("flag");
        //成卡  白卡  靓号开户
        switch (flag) {
            case "0":
                requestCheck = (RequestCheck) getIntent().getExtras().getSerializable("check");
                packages = requestCheck.getPackages();
                break;
            case "1":
                requestImsi = (RequestImsi) getIntent().getExtras().getSerializable("imsi");
                packages = requestImsi.getPackages();
                break;
            case "2":
                agentsLiangNumber = (AgentsLiangNumber) getIntent().getExtras().getSerializable("agentsLiangNumber");
                packages = agentsLiangNumber.getPackages();
                break;
            case "3":
                requestPreNumberDetails = (RequestPreNumberDetails) getIntent().getExtras().getSerializable("preNumber");
                packages = requestPreNumberDetails.getPackages();
                break;
        }
        initView();


    }

    private void initView() {
        adapter = new MyAdapter();
        gvSelect.setAdapter(adapter);
        if (packages.length > 0)
            tvDetail.setText(packages[0].getProductDetails());
        gvSelect.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
        adapter.notifyDataSetChanged();
        tvDetail.setText(packages[pos].getProductDetails());
    }

    @OnClick(R.id.tvSure)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.putExtra("select", pos);
            setResult(0, intent);
            AppManager.getAppManager().finishActivity();
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return packages.length;
        }

        @Override
        public Object getItem(int position) {
            return packages[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(PackageSelectDetailActivity.this).inflate(R.layout.adapter_package_detail, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvMoney.setText(packages[position].getName());
            if (position == pos) {
                viewHolder.llClick.setBackgroundResource(R.drawable.shape_recharge_click);
                viewHolder.tvMoney.setTextColor(getResources().getColor(R.color.colorWhite));
            } else {
                viewHolder.llClick.setBackgroundResource(R.drawable.shape_recharge);
                viewHolder.tvMoney.setTextColor(getResources().getColor(R.color.colorBlue3));
            }

            return convertView;
        }


        class ViewHolder {
            @BindView(R.id.tvMoney)
            TextView tvMoney;
            @BindView(R.id.ll_click)
            LinearLayout llClick;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);

            }
        }
    }
}
