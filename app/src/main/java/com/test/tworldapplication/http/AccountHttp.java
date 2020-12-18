package com.test.tworldapplication.http;

import android.util.Log;

import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostBalanceQuery;
import com.test.tworldapplication.entity.PostOtherDiscount;
import com.test.tworldapplication.entity.PostPsdCheck;
import com.test.tworldapplication.entity.PostQueryBalance;
import com.test.tworldapplication.entity.PostQueryProduct;
import com.test.tworldapplication.entity.PostReAdd;
import com.test.tworldapplication.entity.PostRecharge;
import com.test.tworldapplication.entity.PostRechargeDiscount;
import com.test.tworldapplication.entity.PostServiceProduct;
import com.test.tworldapplication.entity.PostServiceProductDetail;
import com.test.tworldapplication.entity.ProductListEntity;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestOtherDiscount;
import com.test.tworldapplication.entity.RequestPsdCheck;
import com.test.tworldapplication.entity.RequestQueryBalance;
import com.test.tworldapplication.entity.RequestReAdd;
import com.test.tworldapplication.entity.RequestRecharge;
import com.test.tworldapplication.entity.RequestRechargeDiscount;
import com.test.tworldapplication.entity.RequestServiceProduct;
import com.test.tworldapplication.entity.RequestServiceProductDetail;
import com.test.tworldapplication.inter.AccountInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dasiy on 16/11/3.
 */

public class AccountHttp {
    private Retrofit retrofit;
    private AccountInterface accountInterface;
    Subscription subscription;

    public AccountHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(BaseCom.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getTokenOkHttpClient())
                .baseUrl(BaseCom.BASE_URL)
                .build();

        accountInterface = retrofit.create(AccountInterface.class);
    }

    public static OkHttpClient getTokenOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient client = builder.build();
        return client;
    }

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
            new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("test", "收到响应: " + message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY);

    /*号码充值*/
    public void recharge(Subscriber<HttpRequest<RequestRecharge>> subscriber, HttpPost<PostRecharge> httpPost) {
        subscription = accountInterface.recharge(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /*查询账户余额*/
    public void balanceQuery(Subscriber<HttpRequest<RequestBalanceQuery>> subscriber, HttpPost<PostBalanceQuery> httpPost) {
        accountInterface.balanceQuery(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*查询话费余额*/
    public void queryBalance(Subscriber<HttpRequest<RequestQueryBalance>> subscriber, HttpPost<PostQueryBalance> httpPost) {
        accountInterface.queryBalance(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*充值优惠接口*/
    public void rechargeDiscount(Subscriber<HttpRequest<RequestRechargeDiscount>> subscriber, HttpPost<PostRechargeDiscount> httpPost) {
        accountInterface.rechargeDiscount(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*获取活动接口*/
    public void _2019QueryProducts(Subscriber<HttpRequest<ProductListEntity>> subscriber, HttpPost<PostQueryProduct> httpPost) {
        accountInterface._2019QueryProducts(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*产品订购/退订*/
    public void _2019ServiceProduct(Subscriber<HttpRequest<RequestServiceProduct>> subscriber, HttpPost<PostServiceProduct> httpPost) {
        accountInterface._2019ServiceProduct(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*产品详情查询*/
    public void _2019ServiceProductDetails(Subscriber<HttpRequest<RequestServiceProductDetail>> subscriber, HttpPost<PostServiceProductDetail> httpPost) {
        accountInterface._2019ServiceProductDetails(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*支付密码验证*/
    public void initialPsdCheck(Subscriber<HttpRequest<RequestPsdCheck>> subscriber, HttpPost<PostPsdCheck> httpPost) {
        accountInterface.initialPsdCheck(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*支付预订单*/
    public void reAddRecharge(Subscriber<HttpRequest<RequestReAdd>> subscriber, HttpPost<PostReAdd> httpPost) {
        accountInterface.reAddRecharge(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*其他优惠金额*/
    public void otherRechargeDiscount(Subscriber<HttpRequest<RequestOtherDiscount>> subscriber, HttpPost<PostOtherDiscount> httpPost) {
        accountInterface.otherRechargeDiscount(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void cancelSubscriber() {
        subscription.unsubscribe();
    }

}
