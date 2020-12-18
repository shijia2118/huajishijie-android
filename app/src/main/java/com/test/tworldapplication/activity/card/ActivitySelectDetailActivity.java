package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
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
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.PostPromotion;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestPromotion;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class ActivitySelectDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, SuccessValue<RequestPromotion> {

    @BindView(R.id.gvSelect)
    GridView gvSelect;
    int pos;
    MyAdapter adapter;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    @BindView(R.id.tvSure)
    TextView tvSure;
    Package mPackage = null;
    Promotion[] promotions;
    String flag = "";

    /*成卡白卡共用 根据传递过来的package获取promotion成功之后显示确定按钮,点击确定传回promotion*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_detail);
        ButterKnife.bind(this);
        setBackGroundTitle("活动包选择", true);
        flag = getIntent().getStringExtra("flag");
        mPackage = (Package) getIntent().getExtras().getSerializable("package");
        initView();
        initData();

    }

    private void initView() {
        gvSelect.setOnItemClickListener(this);
        tvSure.setVisibility(View.GONE);
    }

    private void initData() {
        dialog.getTvTitle().setText("正在查询数据");
        HttpPost<PostPromotion> httpPost = new HttpPost<>();
        PostPromotion postPromotion = new PostPromotion();
        postPromotion.setSession_token(Util.getLocalAdmin(ActivitySelectDetailActivity.this)[0]);
        postPromotion.setPackageId(String.valueOf(mPackage.getId()));
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPromotion);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPromotion) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new CardHttp().promotion(CardRequest.promotion(ActivitySelectDetailActivity.this,dialog,ActivitySelectDetailActivity.this, this), httpPost);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
        adapter.notifyDataSetChanged();
        tvDetail.setText(promotions[pos].getName());
    }

    @OnClick(R.id.tvSure)
    public void onClick() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("select", promotions[pos]);
        intent.putExtras(bundle);
        setResult(1, intent);
//        finish();
        AppManager.getAppManager().finishActivity();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void OnSuccess(RequestPromotion value) {
        tvSure.setVisibility(View.VISIBLE);
        tvDetail.setText(value.getPromotions()[0].getName());
        promotions = value.getPromotions();
        adapter = new MyAdapter();
        gvSelect.setAdapter(adapter);

    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return promotions.length;
        }

        @Override
        public Object getItem(int position) {
            return promotions[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(ActivitySelectDetailActivity.this).inflate(R.layout.adapter_package_detail, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvMoney.setText(promotions[position].getName());
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
