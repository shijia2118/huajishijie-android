package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostProduct;
import com.test.tworldapplication.entity.PostVerification;
import com.test.tworldapplication.entity.Product;
import com.test.tworldapplication.entity.RequestProduct;
import com.test.tworldapplication.entity.VerificationCode;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.utils.CountDownTimerUtils;
import com.test.tworldapplication.utils.SPUtil;
import com.test.tworldapplication.utils.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

public class ProductBusinessActivity extends BaseActivity {
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    Subscription subscription;
//    String code;
//    @BindView(R.id.etSearch)
//    EditText etSearch;
//    @BindView(R.id.productListView)
//    ListView productListView;
//    List<Product> list = new ArrayList<>();
//    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_business);
        ButterKnife.bind(this);
        setBackGroundTitle("手机号验证", true);


//        etSearch.clearFocus();
//        list.add(new Product("500M流量包", "啊啊啊啊啊啊啊啊啊啊啊啊啊啊", "100"));
//        list.add(new Product("500M流量包", "啊啊啊啊啊啊啊啊啊啊啊啊啊啊", "100"));
//        myAdapter = new MyAdapter();
//        productListView.setAdapter(myAdapter);
//        myAdapter.setOnSelectListener(new OnSelectListener() {
//            @Override
//            public void onSelect(int selection) {
//                Intent intent = new Intent(ProductBusinessActivity.this, ProductInputActivity.class);
//                intent.putExtra("data", list.get(selection));
//                startActivity(intent);
//                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        etPhone.requestFocus();
        if (subscription != null)
            subscription.unsubscribe();
        tvGetCode.setText("获取验证码");
        tvGetCode.setClickable(true);

    }

    @OnClick({R.id.tvGetCode, R.id.tvGetProduct})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetCode:
                if (etPhone.getText().toString().equals(""))
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                else if (etPhone.getText().toString().length() != 11)
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                else {

                    HttpPost<PostVerification> httpPost = new HttpPost<PostVerification>();
                    PostVerification postCode = new PostVerification();
                    postCode.setNumber(etPhone.getText().toString());
                    postCode.setSession_token(Util.getLocalAdmin(ProductBusinessActivity.this)[0]);
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
                    httpPost.setParameter(postCode);
                    Log.d("aaa", gson.toJson(httpPost));
                    new AdminHttp().checkNumber(new Subscriber<HttpRequest<VerificationCode>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (e.getClass().getName().equals("java.net.SocketTimeoutException")) {
                                Toast.makeText(ProductBusinessActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onNext(HttpRequest<VerificationCode> verificationCodeHttpRequest) {
                            //32001未查询到相关数据 31002 查询短信 记录别表数据异常 31004  插入短信记录异常
//                            code = verificationCodeHttpRequest.getData().getVerificationCode();
//                            SPUtil.put(ProductBusinessActivity.this, "code", code);
                            Toast.makeText(ProductBusinessActivity.this, verificationCodeHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
                            if (verificationCodeHttpRequest.getCode() == BaseCom.NORMAL) {
                                subscription = Util.countdown(90).subscribe(new Subscriber<Integer>() {
                                    @Override
                                    public void onCompleted() {
                                        tvGetCode.setText("获取验证码");
                                        tvGetCode.setClickable(true);

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Integer integer) {
                                        tvGetCode.setText(integer + "s");
                                        tvGetCode.setClickable(false);
                                    }
                                });
                            }

                        }
                    }, httpPost);
                }
                break;
            case R.id.tvGetProduct:
                if (etPhone.getText().toString().equals("") || etCode.getText().toString().equals(""))
                    Toast.makeText(ProductBusinessActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                else {

                    HttpPost<PostProduct> httpPost = new HttpPost<PostProduct>();
                    PostProduct postCode = new PostProduct();
                    postCode.setNumber(etPhone.getText().toString());
//                    postCode.setVerificationCode(SPUtil.get(ProductBusinessActivity.this, "code", "").toString());
                    postCode.setVerificationCode(etCode.getText().toString());
                    postCode.setSession_token(Util.getLocalAdmin(ProductBusinessActivity.this)[0]);
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
                    httpPost.setParameter(postCode);
                    Log.d("ooo", gson.toJson(httpPost));
                    new AdminHttp().getProduct(new Subscriber<HttpRequest<RequestProduct>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpRequest<RequestProduct> requestProductHttpRequest) {

                            Toast.makeText(ProductBusinessActivity.this, requestProductHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
                            if (requestProductHttpRequest.getCode() == BaseCom.NORMAL && requestProductHttpRequest.getData() != null) {

                                Intent intent = new Intent(ProductBusinessActivity.this, ProductInputActivity.class);
                                intent.putExtra("list", (Serializable) requestProductHttpRequest.getData().getProductList());
                                intent.putExtra("number", etPhone.getText().toString());
                                intent.putExtra("code", etCode.getText().toString());
                                startActivity(intent);
                                etPhone.setText("");
                                etCode.setText("");
                            }

                        }
                    }, httpPost);
                }

                break;
        }
    }

//    @OnClick(R.id.rl_search)
//    public void onClick() {
//        etSearch.setFocusable(true);
//    }

//    class MyAdapter extends BaseAdapter {
//        private OnSelectListener onSelectListener;
//
//        public void setOnSelectListener(OnSelectListener onSelectListener) {
//            this.onSelectListener = onSelectListener;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder = null;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(ProductBusinessActivity.this).inflate(R.layout.inflate_product_list, null);
//                viewHolder = new ViewHolder(convertView);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//            viewHolder.tvTitle.setText(list.get(position).getTitle());
//            viewHolder.tvDetail.setText("产品描述:  " + list.get(position).getDetail());
//            viewHolder.tvValue.setText(list.get(position).getMoney() + "元");
//            viewHolder.tvBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onSelectListener.onSelect(position);
//                }
//            });
//            return convertView;
//        }
//
//
//    }
//
//    class ViewHolder {
//        @BindView(R.id.imgProduct)
//        ImageView imgProduct;
//        @BindView(R.id.tvTitle)
//        TextView tvTitle;
//        @BindView(R.id.tvDetail)
//        TextView tvDetail;
//        @BindView(R.id.tvValue)
//        TextView tvValue;
//        @BindView(R.id.tvBuy)
//        TextView tvBuy;
//
//        public ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
}
