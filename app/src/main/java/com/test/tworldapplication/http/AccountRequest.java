package com.test.tworldapplication.http;

import android.app.Activity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PayRequest;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestOtherDiscount;
import com.test.tworldapplication.entity.RequestPsdCheck;
import com.test.tworldapplication.entity.RequestQueryBalance;
import com.test.tworldapplication.entity.RequestReAdd;
import com.test.tworldapplication.entity.RequestRecharge;
import com.test.tworldapplication.entity.RequestRechargeDiscount;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import rx.Subscriber;

/**
 * Created by dasiy on 16/11/3.
 */

public class AccountRequest {
    public static Subscriber<HttpRequest<RequestRecharge>> recharge(final CheckResultDialog dialog, final SuccessValue<HttpRequest<RequestRecharge>> onSuccess) {
        Subscriber<HttpRequest<RequestRecharge>> subscriber = new Subscriber<HttpRequest<RequestRecharge>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpRequest<RequestRecharge> requestRechargeHttpRequest) {
                Log.d("aaa", requestRechargeHttpRequest.getMes());
                Log.d("aaa", requestRechargeHttpRequest.getCode() + "");
                onSuccess.OnSuccess(requestRechargeHttpRequest);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestBalanceQuery>> balanceQuery(final SuccessValue<HttpRequest<RequestBalanceQuery>> onSuccess) {
        Subscriber<HttpRequest<RequestBalanceQuery>> subscriber = new Subscriber<HttpRequest<RequestBalanceQuery>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestBalanceQuery> requestBalanceQueryHttpRequest) {
                onSuccess.OnSuccess(requestBalanceQueryHttpRequest);

            }
        };
        return subscriber;

    }

    public static Subscriber<HttpRequest<RequestQueryBalance>> queryBalance(final Activity activity, final CheckResultDialog dialog, final SuccessValue<HttpRequest<RequestQueryBalance>> onSuccess) {
        Subscriber<HttpRequest<RequestQueryBalance>> subscriber = new Subscriber<HttpRequest<RequestQueryBalance>>() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onCompleted() {
                dialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();

            }

            @Override
            public void onNext(HttpRequest<RequestQueryBalance> requestQueryBalanceHttpRequest) {
                Log.d("aaa", requestQueryBalanceHttpRequest.getMes());
                onSuccess.OnSuccess(requestQueryBalanceHttpRequest);
//                if (requestQueryBalanceHttpRequest.getCode() == BaseCom.NORMAL) {
//
//                    onSuccess.OnSuccess(requestQueryBalanceHttpRequest.getData());
//                } else if (requestQueryBalanceHttpRequest.getCode() == BaseCom.LOSELOG)
//                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;

    }

    public static Subscriber<HttpRequest<RequestRechargeDiscount>> rechargeDiscount(final Activity activity, final SuccessValue<RequestRechargeDiscount> onSuccess) {
        Subscriber<HttpRequest<RequestRechargeDiscount>> subscriber = new Subscriber<HttpRequest<RequestRechargeDiscount>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestRechargeDiscount> requestRechargeDiscountHttpRequest) {
                Util.createToast(activity, requestRechargeDiscountHttpRequest.getMes());
                if (requestRechargeDiscountHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestRechargeDiscountHttpRequest.getData());
                } else if (requestRechargeDiscountHttpRequest.getCode() == BaseCom.LOSELOG || requestRechargeDiscountHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestPsdCheck>> initialPsdCheck(final Activity activity, final SuccessValue<HttpRequest<RequestPsdCheck>> onSuccess) {
        Subscriber<HttpRequest<RequestPsdCheck>> subscriber = new Subscriber<HttpRequest<RequestPsdCheck>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPsdCheck> requestPsdCheckHttpRequest) {
                Util.createToast(activity, requestPsdCheckHttpRequest.getMes());
//                if (requestPsdCheckHttpRequest.getCode() == BaseCom.NORMAL) {
                onSuccess.OnSuccess(requestPsdCheckHttpRequest);
//                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestReAdd>> reAddRecharge(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestReAdd> onSuccess) {
        Subscriber<HttpRequest<RequestReAdd>> subscriber = new Subscriber<HttpRequest<RequestReAdd>>() {

            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
                Log.d("ccc", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpRequest<RequestReAdd> requestReAddHttpRequest) {
                Util.createToast(activity, requestReAddHttpRequest.getMes());
                if (requestReAddHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestReAddHttpRequest.getData());
                } else if (requestReAddHttpRequest.getCode() == BaseCom.LOSELOG || requestReAddHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestOtherDiscount>> otherRechargeDiscount(final Activity activity, final SuccessValue<RequestOtherDiscount> onSuccess) {
        Subscriber<HttpRequest<RequestOtherDiscount>> subscriber = new Subscriber<HttpRequest<RequestOtherDiscount>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestOtherDiscount> requestOtherDiscountHttpRequest) {
                Log.d("aaa", requestOtherDiscountHttpRequest.getMes());
                Util.createToast(activity, requestOtherDiscountHttpRequest.getMes());
                if (requestOtherDiscountHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestOtherDiscountHttpRequest.getData());
                } else if (requestOtherDiscountHttpRequest.getCode() == BaseCom.LOSELOG || requestOtherDiscountHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }
}
