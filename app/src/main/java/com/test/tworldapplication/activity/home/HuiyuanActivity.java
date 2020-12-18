package com.test.tworldapplication.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostQueryProduct;
import com.test.tworldapplication.entity.PostServiceProduct;
import com.test.tworldapplication.entity.PostServiceProductDetail;
import com.test.tworldapplication.entity.ProdBean;
import com.test.tworldapplication.entity.ProdsEntity;
import com.test.tworldapplication.entity.ProductBeanDetail;
import com.test.tworldapplication.entity.ProductListEntity;
import com.test.tworldapplication.entity.RequestServiceProduct;
import com.test.tworldapplication.entity.RequestServiceProductDetail;
import com.test.tworldapplication.http.AccountHttp;
import com.test.tworldapplication.utils.EventBusCarrier;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.GridViewForScrollView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class HuiyuanActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    ListView recyclerView;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    List<ProdsEntity> prodBeans = new ArrayList<>();
    List<ProductBeanDetail> productBeanDetails = new ArrayList<>();
    RecyclerAdapter adapter;
    String phone = "";
    Dialog checkBuyDialog, retreatToastDialog, buyInfoDialog;
    ProductBeanDetail chooseProductBeanDetail;
    String strNo = "";
    String huiyuanName = "";
    Integer memo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huiyuan);
        ButterKnife.bind(this);
        phone = getIntent().getStringExtra("phone");
//        phone = "17091500119";
        tvPhone.setText(phone);
        tvTitle.setText("会员");
        adapter = new RecyclerAdapter(new Callback() {
            @Override
            public void callback(int groupPosition, int childPosition) {
                huiyuanName = prodBeans.get(groupPosition).getProds().get(childPosition).getName();
                chooseProductBeanDetail = prodBeans.get(groupPosition).getProds().get(childPosition);

                HttpPost<PostServiceProductDetail> httpPost = new HttpPost<>();
                PostServiceProductDetail serviceProduct = new PostServiceProductDetail();
                serviceProduct.setSession_token(Util.getLocalAdmin(HuiyuanActivity.this)[0]);
                serviceProduct.setProductId(chooseProductBeanDetail.getProductId());

                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(serviceProduct);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(serviceProduct) + BaseCom.APP_PWD));
                new AccountHttp()._2019ServiceProductDetails(new Subscriber<HttpRequest<RequestServiceProductDetail>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpRequest<RequestServiceProductDetail> httpRequest) {
                        if (httpRequest.getCode() == BaseCom.NORMAL) {
                            memo = httpRequest.getData().getProducts().getMemo1();
                            initCheckBuyDialog();

                        } else
                            Toast.makeText(HuiyuanActivity.this, httpRequest.getMes(), Toast.LENGTH_SHORT).show();

                    }
                }, httpPost);


            }
        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(LiuliangActivity.this));
        recyclerView.setAdapter(adapter);
        getData();
    }

    @OnClick({R.id.imgBack, R.id.tvPhone, R.id.imgNext, R.id.llPhone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tvPhone:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.imgNext:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.llPhone:
                AppManager.getAppManager().finishActivity();
                break;
        }
    }

    public void initCheckBuyDialog() {
        checkBuyDialog = new Dialog(HuiyuanActivity.this);
        checkBuyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = checkBuyDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0); //没有边距
        //layoutParams.dimAmount = 0.0f;  //背景不变暗
        layoutParams.dimAmount = 0.7f;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //背景透明
        window.setAttributes(layoutParams);
        checkBuyDialog.setContentView(R.layout.dialog_buy_huiyuan);
        checkBuyDialog.setCanceledOnTouchOutside(true);
        TextView tvTitle = (TextView) checkBuyDialog.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) checkBuyDialog.findViewById(R.id.tvContent);
        EditText etNo = (EditText) checkBuyDialog.findViewById(R.id.etNo);
        TextView refuse = (TextView) checkBuyDialog.findViewById(R.id.refuse);
        TextView agree = (TextView) checkBuyDialog.findViewById(R.id.agree);
        tvTitle.setText(huiyuanName);
        tvContent.setText(chooseProductBeanDetail.getProductDetails());
        switch (memo) {
            case 0:
                etNo.setVisibility(View.GONE);
                break;
            case 1:
                etNo.setVisibility(View.VISIBLE);
                break;
        }

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBuyDialog.dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((memo == 1 && !etNo.getText().toString().equals("")) || memo == 0) {
                    strNo = etNo.getText().toString();
                    checkBuyDialog.dismiss();
                    initBuyInfoDialog();
                } else
                    Toast.makeText(HuiyuanActivity.this, "请输入会员账号", Toast.LENGTH_SHORT).show();

            }
        });
        checkBuyDialog.show();
    }

    public void initBuyInfoDialog() {
        buyInfoDialog = new Dialog(HuiyuanActivity.this);
        buyInfoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        buyInfoDialog.setContentView(R.layout.dialog_buy_huiyuan_info);
        buyInfoDialog.setCanceledOnTouchOutside(true);
        Window window = buyInfoDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0); //没有边距

        //layoutParams.dimAmount = 0.0f;  //背景不变暗
        layoutParams.dimAmount = 0.7f;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //背景透明
        window.setAttributes(layoutParams);
        TextView tvTitle = (TextView) buyInfoDialog.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) buyInfoDialog.findViewById(R.id.tvContent);
        TextView tvName = (TextView) buyInfoDialog.findViewById(R.id.tvName);
        TextView tvAccount = (TextView) buyInfoDialog.findViewById(R.id.tvAccount);
        TextView tvToast = (TextView) buyInfoDialog.findViewById(R.id.tvToast);
        TextView refuse = (TextView) buyInfoDialog.findViewById(R.id.refuse);
        TextView agree = (TextView) buyInfoDialog.findViewById(R.id.agree);
        tvTitle.setText("订购信息");
        tvName.setText(chooseProductBeanDetail.getName());
        tvContent.setText("价格：" + chooseProductBeanDetail.getOrderAmount() + "元");
        if (strNo.equals("")) {
            tvAccount.setVisibility(View.GONE);
        } else {
            tvAccount.setVisibility(View.VISIBLE);
        }
        tvAccount.setText("会员账号：" + strNo);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyInfoDialog.dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpPost<PostServiceProduct> httpPost = new HttpPost<>();
                PostServiceProduct serviceProduct = new PostServiceProduct();
                serviceProduct.setSession_token(Util.getLocalAdmin(HuiyuanActivity.this)[0]);
                serviceProduct.setType(3);
                serviceProduct.setProductId(chooseProductBeanDetail.getProductId());
                serviceProduct.setNumber(phone);
                if (strNo.equals(""))
                    serviceProduct.setMemberAccount("-1");
                else
                    serviceProduct.setMemberAccount(strNo);
                serviceProduct.setOperateType(1);
                serviceProduct.setProductAmount(chooseProductBeanDetail.getProductAmount() + "");
                serviceProduct.setOrderAmount(chooseProductBeanDetail.getOrderAmount() + "");

                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(serviceProduct);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(serviceProduct) + BaseCom.APP_PWD));
                new AccountHttp()._2019ServiceProduct(new Subscriber<HttpRequest<RequestServiceProduct>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpRequest<RequestServiceProduct> httpRequest) {
                        Toast.makeText(HuiyuanActivity.this, httpRequest.getMes(), Toast.LENGTH_SHORT).show();

                        if (httpRequest.getCode() == BaseCom.NORMAL) {
                            buyInfoDialog.dismiss();
                            AppManager.getAppManager().finishActivity();
                            EventBusCarrier carrier = new EventBusCarrier();
                            carrier.setEventType("444");
                            EventBus.getDefault().post(carrier);
                        }

                    }
                }, httpPost);


//
//                buyInfoDialog.dismiss();
//                HttpPost<PostServiceProduct> httpPost = new HttpPost<>();
//                PostServiceProduct serviceProduct = new PostServiceProduct();
//                serviceProduct.setSession_token(Util.getLocalAdmin(HuiyuanActivity.this)[0]);
//                serviceProduct.setType(3);
//                serviceProduct.setProductId(chooseProductBeanDetail.getProductId());
//                serviceProduct.setNumber(phone);
//                serviceProduct.setOperateType(1);
////                serviceProduct.setMemberAccount(strNo);
//                serviceProduct.setProductAmount(chooseProductBeanDetail.getProductAmount());
//                serviceProduct.setOrderAmount(chooseProductBeanDetail.getOrderAmount());
//
//                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//                httpPost.setParameter(serviceProduct);
//                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(serviceProduct) + BaseCom.APP_PWD));
//
//                new AccountHttp()._2019ServiceProduct(new Subscriber<HttpRequest<RequestServiceProduct>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(HttpRequest<RequestServiceProduct> httpRequest) {
//                        retreatToastDialog.dismiss();
//                        if (httpRequest.getCode() == BaseCom.NORMAL) {
//                            Toast.makeText(HuiyuanActivity.this, "订购成功", Toast.LENGTH_SHORT).show();
//                        } else
//                            Toast.makeText(HuiyuanActivity.this, httpRequest.getMes(), Toast.LENGTH_SHORT).show();
//
//                    }
//                }, httpPost);

            }
        });
        buyInfoDialog.show();
    }


    private void getData() {
        HttpPost<PostQueryProduct> httpPost = new HttpPost<>();
        PostQueryProduct postQueryProduct = new PostQueryProduct();
        postQueryProduct.setSession_token(Util.getLocalAdmin(HuiyuanActivity.this)[0]);
        postQueryProduct.setType(3);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postQueryProduct);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postQueryProduct) + BaseCom.APP_PWD));
        new AccountHttp()._2019QueryProducts(new Subscriber<HttpRequest<ProductListEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<ProductListEntity> productListEntityHttpRequest) {
                Util.createToast(HuiyuanActivity.this, productListEntityHttpRequest.getMes());
                prodBeans.clear();
                if (productListEntityHttpRequest.getCode() == BaseCom.NORMAL) {
                    if (productListEntityHttpRequest.getData().getProducts().getPrivilegeProds().size() > 0) {
                        prodBeans.addAll(productListEntityHttpRequest.getData().getProducts().getPrivilegeProds());

                    }
                    adapter.notifyDataSetChanged();
                } else if (productListEntityHttpRequest.getCode() == BaseCom.LOSELOG || productListEntityHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(HuiyuanActivity.this, LoginActivity.class);
            }
        }, httpPost);
    }

    class RecyclerAdapter extends BaseAdapter {
        List<GvAdapter> adapterList = new ArrayList<>();
        Callback callback;

        public RecyclerAdapter(Callback callback) {
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return prodBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return prodBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(HuiyuanActivity.this).inflate(R.layout.item_liuliang, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.d("vvv", prodBeans.get(position).getMemo2() + "");
            viewHolder.tvTitle.setText(prodBeans.get(position).getMemo2());
            productBeanDetails.clear();
            productBeanDetails.addAll(prodBeans.get(position).getProds());
            GvAdapter gvAdapter = new GvAdapter(callback, position);
            adapterList.add(gvAdapter);
            viewHolder.gvGrid.setAdapter(gvAdapter);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tvTitle)
            TextView tvTitle;
            @BindView(R.id.gvGrid)
            GridViewForScrollView gvGrid;


            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    interface Callback {
        void callback(int groupPosition, int childPosition);
    }


    class GvAdapter extends BaseAdapter {
        int groupPosition;
        Callback callback;

        public GvAdapter(Callback callback, int groupPosition) {
            this.callback = callback;
            this.groupPosition = groupPosition;
        }

        @Override
        public int getCount() {
            return productBeanDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return productBeanDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GvAdapter.ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(HuiyuanActivity.this).inflate(R.layout.adapter_account_recharge, null);
                holder = new GvAdapter.ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (GvAdapter.ViewHolder) convertView.getTag();
            }
            holder.tvPrime.setText(productBeanDetails.get(position).getName());
            holder.tvCurrent.setText(productBeanDetails.get(position).getOrderAmount() + "元");
            switch (productBeanDetails.get(position).getIsSelect()) {
                case 0:
                    holder.llClick.setBackgroundResource(R.drawable.shape_recharge);
                    holder.tvPrime.setTextColor(getResources().getColor(R.color.colorBlue3));
                    holder.tvCurrent.setTextColor(getResources().getColor(R.color.colorBlue3));
                    break;
                case 1:
                    holder.llClick.setBackgroundResource(R.drawable.shape_recharge_click);
                    holder.tvPrime.setTextColor(getResources().getColor(R.color.colorWhite));
                    holder.tvCurrent.setTextColor(getResources().getColor(R.color.colorWhite));
                    break;
            }
            holder.llClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.callback(groupPosition, position);

                }
            });

            return convertView;
        }


        class ViewHolder {
            @BindView(R.id.tvPrime)
            TextView tvPrime;
            @BindView(R.id.tvCurrent)
            TextView tvCurrent;
            @BindView(R.id.ll_click)
            LinearLayout llClick;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }
}
