package com.test.tworldapplication.http;

import android.util.Log;

import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.CheckInfo;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostAdmin;
import com.test.tworldapplication.entity.PostCaptcha;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostCommissions;
import com.test.tworldapplication.entity.PostFeedback;
import com.test.tworldapplication.entity.PostForgetPwd;
import com.test.tworldapplication.entity.PostGetAccountPeriod;
import com.test.tworldapplication.entity.PostGetAccountPeriodList;
import com.test.tworldapplication.entity.PostIntroduction;
import com.test.tworldapplication.entity.PostLogin;
import com.test.tworldapplication.entity.PostLogout;
import com.test.tworldapplication.entity.PostLuckNumber;
import com.test.tworldapplication.entity.PostModify;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.PostOrderProduct;
import com.test.tworldapplication.entity.PostPinCreate;
import com.test.tworldapplication.entity.PostPinModify;
import com.test.tworldapplication.entity.PostProduct;
import com.test.tworldapplication.entity.PostRecord;
import com.test.tworldapplication.entity.PostRegister;
import com.test.tworldapplication.entity.PostStatistic;
import com.test.tworldapplication.entity.PostUpdatePersonalInfo;
import com.test.tworldapplication.entity.PostVerification;
import com.test.tworldapplication.entity.PostWinningOrder;
import com.test.tworldapplication.entity.RequestAdmin;
import com.test.tworldapplication.entity.RequestCaptcha;
import com.test.tworldapplication.entity.RequestCode;
import com.test.tworldapplication.entity.RequestCommissions;
import com.test.tworldapplication.entity.RequestFeedback;
import com.test.tworldapplication.entity.RequestForgrtPwd;
import com.test.tworldapplication.entity.RequestGetAccountPeriod;
import com.test.tworldapplication.entity.RequestGetAccountPeriodList;
import com.test.tworldapplication.entity.RequestIntroduction;
import com.test.tworldapplication.entity.RequestLogin;
import com.test.tworldapplication.entity.RequestLogout;
import com.test.tworldapplication.entity.RequestLuckNumber;
import com.test.tworldapplication.entity.RequestLuckeyDraw;
import com.test.tworldapplication.entity.RequestModify;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.entity.RequestOrderProduct;
import com.test.tworldapplication.entity.RequestPinCreate;
import com.test.tworldapplication.entity.RequestPinModify;
import com.test.tworldapplication.entity.RequestProduct;
import com.test.tworldapplication.entity.RequestRegister;
import com.test.tworldapplication.entity.RequestStatistic;
import com.test.tworldapplication.entity.RequestWinningOrder;
import com.test.tworldapplication.entity.VerificationCode;
import com.test.tworldapplication.inter.AdminInterface;

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
 * Created by dasiy on 16/10/31.
 */

public class AdminHttp {
    private Retrofit retrofit;
    private AdminInterface adminInterface;

    public AdminHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(BaseCom.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BaseCom.BASE_URL)
                .client(getTokenOkHttpClient())
                .build();

        adminInterface = retrofit.create(AdminInterface.class);
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

    /*登录*/
    public void login(Subscriber<HttpRequest<RequestLogin>> subscriber, HttpPost<PostLogin> httpPost) {
        adminInterface.login(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*登录*/
    public void getLuckNumbers(Subscriber<HttpRequest<RequestLuckNumber>> subscriber, HttpPost<PostLuckNumber> httpPost) {
        adminInterface.getLuckNumbers(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void luckyDraw(Subscriber<HttpRequest<RequestLuckeyDraw>> subscriber, HttpPost<PostLuckNumber> httpPost) {
        adminInterface.luckyDraw(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getWinningOrders(Subscriber<HttpRequest<RequestWinningOrder>> subscriber, HttpPost<PostWinningOrder> httpPost) {
        adminInterface.getWinningOrders(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*注册*/
    public void register(Subscriber<HttpRequest<RequestRegister>> subscriber, HttpPost<PostRegister> httpPost) {
        adminInterface.register(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*获取短信验证码*/
    public void getCode(Subscriber<HttpRequest<RequestCode>> subscriber, HttpPost<PostCode> httpPost) {
        adminInterface.getCode(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*验证短信验证码*/
    public void verifyCaptcha(Subscriber<HttpRequest<RequestCaptcha>> subscriber, HttpPost<PostCaptcha> httpPost) {
        adminInterface.verifyCaptcha(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*获取个人信息*/
    public void getAdmin(Subscriber<HttpRequest<RequestAdmin>> subscriber, HttpPost<PostAdmin> httpPost) {
        adminInterface.getAdmin(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*修改登录密码*/
    public void modifyPwd(Subscriber<HttpRequest<RequestModify>> subscriber, HttpPost<PostModify> httpPost) {
        adminInterface.modifyPwd(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*开户统计*/
    public void statistic(Subscriber<HttpRequest<RequestStatistic>> subscriber, HttpPost<PostStatistic> httpPost) {
        adminInterface.statistic(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*佣金统计*/
    public void commissionsQuery(Subscriber<HttpRequest<RequestCommissions>> subscriber, HttpPost<PostCommissions> httpPost) {
        adminInterface.commissionsQuery(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*意见反馈*/
    public void feedback(Subscriber<HttpRequest<RequestFeedback>> subscriber, HttpPost<PostFeedback> httpPost) {
        adminInterface.feedback(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*支付密码创建*/
    public void pinCreate(Subscriber<HttpRequest<RequestPinCreate>> subscriber, HttpPost<PostPinCreate> httpPost) {
        adminInterface.pinCreate(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*支付密码修改*/
    public void pinModify(Subscriber<HttpRequest<RequestPinModify>> subscriber, HttpPost<PostPinModify> httpPost) {
        adminInterface.pinModify(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*登出注销*/
    public void logout(Subscriber<HttpRequest<RequestLogout>> subscriber, HttpPost<PostLogout> httpPost) {
        adminInterface.logout(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*忘记密码*/
    public void forgetPwd(Subscriber<HttpRequest<RequestForgrtPwd>> subscriber, HttpPost<PostForgetPwd> httpPost) {
        adminInterface.forgetPwd(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*关于我们*/
    public void introduction(Subscriber<HttpRequest<RequestIntroduction>> subscriber, HttpPost<PostIntroduction> httpPost) {
        adminInterface.introduction(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkNumber(Subscriber<HttpRequest<VerificationCode>> subscriber, HttpPost<PostVerification> httpPost) {
        adminInterface.checkNumber(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProduct(Subscriber<HttpRequest<RequestProduct>> subscriber, HttpPost<PostProduct> httpPost) {
        adminInterface.getProduct(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void orderProduct(Subscriber<HttpRequest> subscriber, HttpPost<PostOrderProduct> httpPost) {
        adminInterface.orderProduct(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getOrderProductList(Subscriber<HttpRequest<RequestOrderProduct>> subscriber, HttpPost<PostRecord> httpPost) {
        adminInterface.getOrderProductList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getAccountPeriodList(Subscriber<HttpRequest<RequestGetAccountPeriodList>> subscriber, HttpPost<PostGetAccountPeriodList> httpPost) {
        adminInterface.getAccountPeriodList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAccountPeriod(Subscriber<HttpRequest<RequestGetAccountPeriod>> subscriber, HttpPost<PostGetAccountPeriod> httpPost) {
        adminInterface.getAccountPeriod(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getUpdatePersonalInfo(Subscriber<HttpRequest<String>> subscriber, HttpPost<PostUpdatePersonalInfo> httpPost) {
        adminInterface.getUpdatePersonalInfo(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getOpenPower(Subscriber<HttpRequest<RequestOpenPower>> subscriber, HttpPost<PostOpenPower> httpPost) {
        adminInterface.getOpenPower(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void checkInfo(Subscriber<HttpRequest<CheckInfo>> subscriber, HttpPost<PostOpenPower> httpPost) {
        adminInterface.checkInfo(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
