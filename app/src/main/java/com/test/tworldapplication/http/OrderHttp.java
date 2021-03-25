package com.test.tworldapplication.http;

import android.util.Log;

import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.JudgeLocationResponse;
import com.test.tworldapplication.entity.NumberPoolNew;
import com.test.tworldapplication.entity.PostAgencyGetBill;
import com.test.tworldapplication.entity.PostAuditList;
import com.test.tworldapplication.entity.PostBkApplyList;
import com.test.tworldapplication.entity.PostBkSelect;
import com.test.tworldapplication.entity.PostChannelsList;
import com.test.tworldapplication.entity.PostChannelsOpenCount;
import com.test.tworldapplication.entity.PostGetSonOrder;
import com.test.tworldapplication.entity.PostGetSonOrderList;
import com.test.tworldapplication.entity.PostLocationEntity;
import com.test.tworldapplication.entity.PostOrderEntity;
import com.test.tworldapplication.entity.PostOrderInfo;
import com.test.tworldapplication.entity.PostOrderList;
import com.test.tworldapplication.entity.PostPreNumberListNew;
import com.test.tworldapplication.entity.PostRechargeList;
import com.test.tworldapplication.entity.PostRemarkOrderInfo;
import com.test.tworldapplication.entity.PostSelectOrderInfo;
import com.test.tworldapplication.entity.PostSendOrCancel;
import com.test.tworldapplication.entity.PostSessionToken;
import com.test.tworldapplication.entity.PostTransferList;
import com.test.tworldapplication.entity.PostTransferOrderInfo;
import com.test.tworldapplication.entity.PreRandomNumberNew;
import com.test.tworldapplication.entity.RequestAgencyGetBill;
import com.test.tworldapplication.entity.RequestAuditList;
import com.test.tworldapplication.entity.RequestBkApplyList;
import com.test.tworldapplication.entity.RequestBkSelect;
import com.test.tworldapplication.entity.RequestChannelOpenCount;
import com.test.tworldapplication.entity.RequestChannelsList;
import com.test.tworldapplication.entity.RequestGetSonOrder;
import com.test.tworldapplication.entity.RequestGetSonOrderList;
import com.test.tworldapplication.entity.RequestOrderEntity;
import com.test.tworldapplication.entity.RequestOrderInfo;
import com.test.tworldapplication.entity.RequestOrderList;
import com.test.tworldapplication.entity.RequestPreNumberNew;
import com.test.tworldapplication.entity.RequestRechargeList;
import com.test.tworldapplication.entity.RequestRemarkOrderInfo;
import com.test.tworldapplication.entity.RequestSelectionOrderInfo;
import com.test.tworldapplication.entity.RequestTransferList;
import com.test.tworldapplication.entity.RequestTransferOrderInfo;
import com.test.tworldapplication.inter.OrderInterface;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dasiy on 16/11/7.
 */

public class OrderHttp {
    private Retrofit retrofit;
    private OrderInterface orderInterface;

    public OrderHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(0, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BaseCom.BASE_URL)
                .client(getTokenOkHttpClient())
                .build();

        orderInterface = retrofit.create(OrderInterface.class);
    }

    public static OkHttpClient getTokenOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(0, TimeUnit.SECONDS);
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

    /*订单列表*/
    public void orderKhList(Subscriber<HttpRequest<RequestOrderList>> subscriber, HttpPost<PostOrderList> httpPost) {
        orderInterface.orderKhList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*订单列表*/
    public void ePreNumberList(Subscriber<HttpRequest<RequestPreNumberNew>> subscriber, HttpPost<PostPreNumberListNew> httpPost) {
        orderInterface.ePreNumberList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*订单列表查询*/
    public void _2019QueryOrders(Subscriber<HttpRequest<RequestOrderEntity>> subscriber, HttpPost<PostOrderEntity> httpPost) {
        orderInterface._2019QueryOrders(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void orderAuditList(Subscriber<HttpRequest<RequestAuditList>> subscriber, HttpPost<PostAuditList> httpPost) {
        orderInterface.orderAuditList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*过户补卡列表*/
    public void orderTransferList(Subscriber<HttpRequest<RequestTransferList>> subscriber, HttpPost<PostTransferList> httpPost) {
        orderInterface.orderTransferList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*充值列表*/
    public void orderRechargeList(Subscriber<HttpRequest<RequestRechargeList>> subscriber, HttpPost<PostRechargeList> httpPost) {
        orderInterface.orderRechargeList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*开户详情*/
    public void orderInfo(String from, Subscriber<HttpRequest<RequestOrderInfo>> subscriber, HttpPost<PostOrderInfo> httpPost) {
        switch (from) {
            case "0":
                orderInterface.orderInfo(httpPost).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case "1":
                orderInterface.openEOrderInfo(httpPost).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
        }
    }

    public void selectOrderInfo(Subscriber<HttpRequest<RequestSelectionOrderInfo>> subscriber, HttpPost<PostSelectOrderInfo> httpPost) {
        orderInterface.selectOrderInfo(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*过户详情*/
    public void transferOrderInfo(Subscriber<HttpRequest<RequestTransferOrderInfo>> subscriber, HttpPost<PostTransferOrderInfo> httpPost) {
        orderInterface.transferOrderInfo(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*补卡详情*/
    public void remarkOrderInfo(Subscriber<HttpRequest<RequestRemarkOrderInfo>> subscriber, HttpPost<PostRemarkOrderInfo> httpPost) {
        orderInterface.remarkOrderInfo(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /*上传定位*/
    public void judgeLocation(Subscriber<HttpRequest<JudgeLocationResponse>> subscriber, HttpPost<PostLocationEntity> httpPost) {
        orderInterface.judgeLocation(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*渠道列表*/
    public void channelsList(Subscriber<HttpRequest<RequestChannelsList>> subscriber, HttpPost<PostChannelsList> httpPost) {
        orderInterface.channelsList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*渠道开户量*/
    public void channelsOpenCount(Subscriber<HttpRequest<RequestChannelOpenCount>> subscriber, HttpPost<PostChannelsOpenCount> httpPost) {
        orderInterface.channelsOpenCount(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /*代理查询子帐号开户列表*/
    public void getSonOrderList(Subscriber<HttpRequest<RequestGetSonOrderList>> subscriber, HttpPost<PostGetSonOrderList> httpPost) {
        orderInterface.getSonOrderList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*代理查询子帐号开户详情*/
    public void getSonOrder(Subscriber<HttpRequest<RequestGetSonOrder>> subscriber, HttpPost<PostGetSonOrder> httpPost) {
        orderInterface.getSonOrder(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /*账单查询*/
    public void agencyGetBill(Subscriber<HttpRequest<RequestAgencyGetBill>> subscriber, HttpPost<PostAgencyGetBill> httpPost) {
        orderInterface.agencyGetBill(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void orderBkList(Subscriber<HttpRequest<RequestBkApplyList>> subscriber, HttpPost<PostBkApplyList> httpPost) {
        orderInterface.orderBkList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void whiteNumberPool(Subscriber<HttpRequest<List<NumberPoolNew>>> subscriber, HttpPost<PostSessionToken> httpPost) {
        orderInterface.whiteNumberPool(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void preRandomNumber(Subscriber<HttpRequest<PreRandomNumberNew>> subscriber, HttpPost httpPost) {
        orderInterface.preRandomNumber(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getBkApplyDetail(Subscriber<HttpRequest<RequestBkSelect>> subscriber, HttpPost<PostBkSelect> httpPost) {
        orderInterface.getBkSelect(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void sendOrCancel(Subscriber<HttpRequest<String>> subscriber, HttpPost<PostSendOrCancel> httpPost) {
        orderInterface.sendOrCancel(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

