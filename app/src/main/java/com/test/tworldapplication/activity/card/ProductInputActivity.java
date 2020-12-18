package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.order.OrderKhListActivity;
import com.test.tworldapplication.adapter.ProductListAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Admin;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostOrderProduct;
import com.test.tworldapplication.entity.PostProduct;
import com.test.tworldapplication.entity.Product;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.utils.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ProductInputActivity extends BaseActivity {


    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.product_listview)
    ListView productListview;
    List<Product> productList = new ArrayList<>();
    ProductListAdapter adapter;
    String number;
    String code;

    public interface OnPositionClick {
        void clickCallback(int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_input);
        ButterKnife.bind(this);
        setBackGroundTitle("流量包列表", true);
        List<Product> list = (List<Product>) getIntent().getSerializableExtra("list");
        number = getIntent().getStringExtra("number");
        code = getIntent().getStringExtra("code");
        tvNumber.setText(number);
        productList.addAll(list);


        adapter = new ProductListAdapter(ProductInputActivity.this, productList, new OnPositionClick() {
            @Override
            public void clickCallback(final int position) {

                Intent intent = new Intent(ProductInputActivity.this, ProductConfirmActivity.class);
                intent.putExtra("product", (Serializable) productList.get(position));
                intent.putExtra("number", number);
                intent.putExtra("code", code);
                startActivity(intent);


            }
        });
        productListview.setAdapter(adapter);


    }


    @OnClick(R.id.ll_number)
    public void onViewClicked() {
        Intent intent = new Intent(ProductInputActivity.this, ProductBusinessActivity.class);
        startActivity(intent);
    }

}
