package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostOrderProduct;
import com.test.tworldapplication.entity.Product;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

public class ProductConfirmActivity extends BaseActivity {

    @BindView(R.id.tvContent)
    TextView tvContent;
    Product product;
    String number;
    String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_confirm);
        ButterKnife.bind(this);
        product = (Product) getIntent().getSerializableExtra("product");
        tvContent.setText(product.getProdOfferName());

    }

    @OnClick({R.id.tvCancel, R.id.tvSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tvSure:
                if (!Util.isFastDoubleClick()) {
                    Log.d("vvv", "click");
                    final HttpPost<PostOrderProduct> httpPost = new HttpPost<PostOrderProduct>();
                    PostOrderProduct postCode = new PostOrderProduct();
                    postCode.setNumber(getIntent().getStringExtra("number"));
                    postCode.setVerificationCode(getIntent().getStringExtra("code"));
                    postCode.setSession_token(Util.getLocalAdmin(ProductConfirmActivity.this)[0]);
                    postCode.setProdOfferDesc(product.getProdOfferDesc());
                    postCode.setProdOfferId(product.getProdOfferId());
                    postCode.setProdOfferName(product.getProdOfferName());
                    postCode.setProductId(product.getProductId());
                    postCode.setProductName(product.getProductName());
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
                    httpPost.setParameter(postCode);
                    new AdminHttp().orderProduct(new Subscriber<HttpRequest>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpRequest httpRequest) {
                            Toast.makeText(ProductConfirmActivity.this, httpRequest.getMes(), Toast.LENGTH_SHORT).show();
                            if (httpRequest.getCode() == BaseCom.NORMAL) {
                                Intent intent = new Intent(ProductConfirmActivity.this, OrderSuccessActivity.class);
                                startActivity(intent);
                                AppManager.getAppManager().finishActivity();
                            } else {
                                Intent intent = new Intent(ProductConfirmActivity.this, OrderFailedActivity.class);
                                intent.putExtra("msg", httpRequest.getMes());
                                startActivity(intent);
                                AppManager.getAppManager().finishActivity();

                            }


                        }
                    }, httpPost);


                }


                break;
        }
    }
}
