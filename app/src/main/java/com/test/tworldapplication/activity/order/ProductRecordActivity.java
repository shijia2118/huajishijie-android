package com.test.tworldapplication.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.card.ProductBusinessActivity;
import com.test.tworldapplication.adapter.ProductRecordListAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostRecord;
import com.test.tworldapplication.entity.PostVerification;
import com.test.tworldapplication.entity.ProductRecord;
import com.test.tworldapplication.entity.RequestOrderProduct;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ProductRecordActivity extends BaseActivity {
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.record_listview)
    ListView listView;
    @BindView(R.id.shadow_view)
    View shadowView;
    @BindView(R.id.vLine)
    View vLine;
    List<ProductRecord> productRecords = new ArrayList<>();
    ProductRecordListAdapter adapter;
    PopupWindow mPopupWindow;
    Holder holder;
    int page = 1;
    int linage = 5;
    int refresh = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_record);
        ButterKnife.bind(this);
        setBackGroundTitle("流量包订单", true);
        tvCollection.setVisibility(View.VISIBLE);
        tvCollection.setText("筛选");
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refresh = 1;
                holder.etNumber.setText("");
                getRecords();
            }

            @Override
            public void onLoadmore() {
                page++;
                refresh = 0;
                getRecords();
            }
        });
        springView.setHeader(new DefaultHeader(ProductRecordActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultFooter(ProductRecordActivity.this));

        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition, null);
        holder = new Holder(music_popunwindwow);
        holder.llState.setVisibility(View.GONE);
        holder.llBegin.setVisibility(View.GONE);
        holder.llEnd.setVisibility(View.GONE);
        holder.vLine1.setVisibility(View.GONE);
        holder.vLine0.setVisibility(View.GONE);
        holder.linePhoneNumber.setVisibility(View.GONE);
//        holder.etNumber.setText("asdfg");
        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));

//        ProductRecord productRecord = new ProductRecord();
//        productRecord.setCreateDate("34567");
//        productRecord.setNumber("123456789");
//        productRecord.setOrderStatusName("成功");
//        productRecord.setProdOfferName("充值50");
//        productRecords.add(productRecord);

        adapter = new ProductRecordListAdapter(ProductRecordActivity.this, productRecords);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductRecordActivity.this, ProductRecordDetailActivity.class);
                intent.putExtra("data", productRecords.get(i));
                startActivity(intent);
            }
        });
        getRecords();


    }

    private void getRecords() {
        HttpPost<PostRecord> httpPost = new HttpPost<PostRecord>();
        PostRecord postCode = new PostRecord();
        if (!holder.etNumber.getText().toString().equals(""))
            postCode.setNumber(holder.etNumber.getText().toString());
        postCode.setSession_token(Util.getLocalAdmin(ProductRecordActivity.this)[0]);
        postCode.setPage(page + "");
        postCode.setLinage(linage + "");

        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
        httpPost.setParameter(postCode);
        Log.d("aaa", gson.toJson(httpPost));
        new AdminHttp().getOrderProductList(new Subscriber<HttpRequest<RequestOrderProduct>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onNext(HttpRequest<RequestOrderProduct> requestOrderProductHttpRequest) {
                springView.onFinishFreshAndLoad();
                Toast.makeText(ProductRecordActivity.this, requestOrderProductHttpRequest.getMes(), Toast.LENGTH_SHORT).show();

                if (requestOrderProductHttpRequest.getCode() == BaseCom.NORMAL) {
                    if (refresh == 1)
                        productRecords.clear();
                    productRecords.addAll(requestOrderProductHttpRequest.getData().getOrderProductList());
                    adapter.notifyDataSetChanged();
                }
            }
        }, httpPost);

    }

    @OnClick(R.id.tvCollection)
    public void onViewClicked() {
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
        @BindView(R.id.vLine0)
        View vLine0;
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


        @OnClick({R.id.tvReset, R.id.tvInquery})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvReset:
                    etNumber.setText("");
                    break;
                case R.id.tvInquery:
                    ProductRecordActivity.this.page = 1;
                    ProductRecordActivity.this.refresh = 1;
                    mPopupWindow.dismiss();
                    getRecords();
                    break;
            }
        }
    }
}
