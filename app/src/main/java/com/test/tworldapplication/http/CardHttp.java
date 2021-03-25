package com.test.tworldapplication.http;

import android.util.Log;

import com.test.tworldapplication.activity.PostResult;
import com.test.tworldapplication.activity.order.RequestQrCode;
import com.test.tworldapplication.adapter.PostRegistration;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.DailiAgentsLiangNumber;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.JHOrderInfo;
import com.test.tworldapplication.entity.LiangRecords;
import com.test.tworldapplication.entity.PostAddress;
import com.test.tworldapplication.entity.PostAgentsLiang;
import com.test.tworldapplication.entity.PostAgentsLiangNumber;
import com.test.tworldapplication.entity.PostBkApply;
import com.test.tworldapplication.entity.PostCaution;
import com.test.tworldapplication.entity.PostCautionOrder;
import com.test.tworldapplication.entity.PostCheck;
import com.test.tworldapplication.entity.PostCuteRegular;
import com.test.tworldapplication.entity.PostDs;
import com.test.tworldapplication.entity.PostImsi;
import com.test.tworldapplication.entity.PostJHOrderInfo;
import com.test.tworldapplication.entity.PostJudgeIsLiang;
import com.test.tworldapplication.entity.PostLiangPay;
import com.test.tworldapplication.entity.PostLockNumber;
import com.test.tworldapplication.entity.PostLockNumberNew;
import com.test.tworldapplication.entity.PostMode;
import com.test.tworldapplication.entity.PostMoney;
import com.test.tworldapplication.entity.PostNumberPool;
import com.test.tworldapplication.entity.PostNumberSegment;
import com.test.tworldapplication.entity.PostOpen;
import com.test.tworldapplication.entity.PostOpenInformation;
import com.test.tworldapplication.entity.PostPay;
import com.test.tworldapplication.entity.PostPreNumber;
import com.test.tworldapplication.entity.PostPreNumberDetails;
import com.test.tworldapplication.entity.PostPreNumberList;
import com.test.tworldapplication.entity.PostPreNumberPool;
import com.test.tworldapplication.entity.PostPreOpen;
import com.test.tworldapplication.entity.PostPreRandomNumber;
import com.test.tworldapplication.entity.PostPromotion;
import com.test.tworldapplication.entity.PostQrCode;
import com.test.tworldapplication.entity.PostRandomNumber;
import com.test.tworldapplication.entity.PostReSubmit;
import com.test.tworldapplication.entity.PostRepair;
import com.test.tworldapplication.entity.PostSelectionCheck;
import com.test.tworldapplication.entity.PostSetOpen;
import com.test.tworldapplication.entity.PostSetOpenNew;
import com.test.tworldapplication.entity.PostTips;
import com.test.tworldapplication.entity.PostTransfer;
import com.test.tworldapplication.entity.PostWhiteOpen;
import com.test.tworldapplication.entity.PostWritePreDetails;
import com.test.tworldapplication.entity.RequestCaution;
import com.test.tworldapplication.entity.RequestCautionOrder;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestDs;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestJudgeIsLiang;
import com.test.tworldapplication.entity.RequestLiang;
import com.test.tworldapplication.entity.RequestLiangAgents;
import com.test.tworldapplication.entity.RequestLockNumber;
import com.test.tworldapplication.entity.RequestLockNumberNew;
import com.test.tworldapplication.entity.RequestMode;
import com.test.tworldapplication.entity.RequestMoney;
import com.test.tworldapplication.entity.RequestNumberPool;
import com.test.tworldapplication.entity.RequestNumberSegment;
import com.test.tworldapplication.entity.RequestOpen;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.entity.RequestPreNumber;
import com.test.tworldapplication.entity.RequestPreNumberDetails;
import com.test.tworldapplication.entity.RequestPreNumberList;
import com.test.tworldapplication.entity.RequestPreNumberPool;
import com.test.tworldapplication.entity.RequestPreOpen;
import com.test.tworldapplication.entity.RequestPreRandomNumber;
import com.test.tworldapplication.entity.RequestPromotion;
import com.test.tworldapplication.entity.RequestRandomNumber;
import com.test.tworldapplication.entity.RequestReAdd;
import com.test.tworldapplication.entity.RequestRepair;
import com.test.tworldapplication.entity.RequestSelectionCheck;
import com.test.tworldapplication.entity.RequestSetOpen;
import com.test.tworldapplication.entity.RequestSetOpenNew;
import com.test.tworldapplication.entity.RequestTips;
import com.test.tworldapplication.entity.RequestTransfer;
import com.test.tworldapplication.entity.RequestWhiteOpen;
import com.test.tworldapplication.inter.CardInterface;

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
 * Created by dasiy on 16/11/4.
 */

public class CardHttp {
    private Retrofit retrofit;
    private CardInterface cardInterface;

    public CardHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(BaseCom.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BaseCom.BASE_URL)
                .client(getTokenOkHttpClient())
                .build();

        cardInterface = retrofit.create(CardInterface.class);
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

    /*验证号段信息*/
    public void numberSegment(Subscriber<HttpRequest<RequestNumberSegment>> subscriber, HttpPost<PostNumberSegment> httpPost) {
        cardInterface.numberSegment(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*验证号段信息*/
    public void lockNumberNew(Subscriber<HttpRequest<RequestLockNumberNew>> subscriber, HttpPost<PostLockNumberNew> httpPost) {
        cardInterface.lockNumberNew(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*过户*/
    public void transfer(Subscriber<HttpRequest<RequestTransfer>> subscriber, HttpPost<PostTransfer> httpPost) {
        cardInterface.transfer(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*补卡*/
    public void repair(Subscriber<HttpRequest<RequestRepair>> subscriber, HttpPost<PostRepair> httpPost) {
        cardInterface.repair(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*PUK码验证*/
    public void check(Subscriber<HttpRequest<RequestCheck>> subscriber, HttpPost<PostCheck> httpPost) {
        cardInterface.check(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkSelect(Subscriber<HttpRequest<RequestSelectionCheck>> subscriber, HttpPost<PostSelectionCheck> httpPost) {
        cardInterface.checkSelect(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*活动包信息*/
    public void promotion(Subscriber<HttpRequest<RequestPromotion>> subscriber, HttpPost<PostPromotion> httpPost) {
        cardInterface.promotion(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*金额信息*/
    public void money(Subscriber<HttpRequest<RequestMoney>> subscriber, HttpPost<PostMoney> httpPost) {
        cardInterface.money(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /*成卡开户*/
    public void open(Subscriber<HttpRequest<RequestOpen>> subscriber, HttpPost<PostOpen> httpPost) {
        cardInterface.open(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void setOpen(Subscriber<HttpRequest<RequestSetOpenNew>> subscriber, HttpPost<PostSetOpenNew> httpPost) {
        cardInterface.setOpen(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /*白卡开户*/
    public void whiteSetOpen(Subscriber<HttpRequest<RequestWhiteOpen>> subscriber, HttpPost<PostWhiteOpen> httpPost) {
        cardInterface.whiteSetOpen(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*号码池信息*/
    public void whiteNumberPool(Subscriber<HttpRequest<RequestNumberPool>> subscriber, HttpPost<PostNumberPool> httpPost) {
        cardInterface.whiteNumberPool(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*随机号码*/
    public void randomNumber(Subscriber<HttpRequest<RequestRandomNumber>> subscriber, HttpPost<PostRandomNumber> httpPost) {
        cardInterface.randomNumber(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*号码锁定*/
    public void lockNumber(Subscriber<HttpRequest<RequestLockNumber>> subscriber, HttpPost<PostLockNumber> httpPost) {
        cardInterface.lockNumber(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*获取IMSI信息*/
    public void imsi(Subscriber<HttpRequest<RequestImsi>> subscriber, HttpPost<PostImsi> httpPost) {
        cardInterface.imsi(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void postResult(Subscriber<HttpRequest> subscriber, HttpPost<PostResult> httpPost) {
        cardInterface.postResult(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*获取IMSI信息*/
    public void getImsi(Subscriber<HttpRequest<RequestImsi>> subscriber, HttpPost<PostImsi> httpPost) {
        cardInterface.getImsi(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*获取IMSI信息*/
    public void getImsiAgain(Subscriber<HttpRequest<RequestImsi>> subscriber, HttpPost<PostImsi> httpPost) {
        cardInterface.getImsiAgain(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*白卡开户之预开户*/
    public void preopen(Subscriber<HttpRequest<RequestPreOpen>> subscriber, HttpPost<PostPreOpen> httpPost) {
        cardInterface.preopen(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*成卡开户之开户方式*/
    public void openMode(Subscriber<HttpRequest<RequestMode>> subscriber, HttpPost<PostMode> httpPost) {
        cardInterface.openMode(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*验证是否靓号*/
    public void judgeIsLiang(Subscriber<HttpRequest<RequestJudgeIsLiang>> subscriber, HttpPost<PostJudgeIsLiang> httpPost) {
        cardInterface.judgeIsLiang(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*获取温馨提示*/
    public void getTips(Subscriber<HttpRequest<RequestTips>> subscriber, HttpPost<PostTips> httpPost) {
        cardInterface.getTips(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*靓号平台-靓号规则列表*/
    public void getLiangRule(HttpPost<PostCuteRegular> httpPost, Subscriber<HttpRequest<RequestLiang>> subscriber) {
        cardInterface.getLiangRule(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /*
    * .flatMap(new Func1<HttpRequest<RequestLiang>, Observable<Object>>() {
            @Override
            public Observable<Object> call(HttpRequest<RequestLiang> requestLiangHttpRequest) {
                return Observable.from((requestLiangHttpRequest.getData().getLiangRule()).toArray());
            }
        })*/

    public void getLiangRule_list(HttpPost<PostCuteRegular> httpPost, Subscriber<HttpRequest<RequestLiang>> subscriber) {

        HttpUtils.toSubscribe(cardInterface.getLiangRule(httpPost), subscriber);
    }


    /*靓号平台-代理商靓号列表*/
    public void getAgentsLiangList(HttpPost<PostAgentsLiang> httpPost, Subscriber<HttpRequest<RequestLiangAgents>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.getAgentsLiangList(httpPost), subscriber);
    }

    public void liangRegistration(HttpPost<PostRegistration> httpPost, Subscriber<HttpRequest> subscriber) {
        HttpUtils.toSubscribe(cardInterface.liangRegistration(httpPost), subscriber);
    }


    /*靓号平台-代理商靓号详情*/
    public void getAgentsLiangNumber(HttpPost<PostAgentsLiangNumber> httpPost, Subscriber<HttpRequest<DailiAgentsLiangNumber>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.getAgentsLiangNumber(httpPost), subscriber);
    }

    /*靓号平台-话机世界靓号列表*/
    public void getHjsjLiangList(HttpPost<PostAgentsLiang> httpPost, Subscriber<HttpRequest<RequestLiangAgents>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.getHjsjLiangList(httpPost), subscriber);
    }

    /*靓号平台-话机世界靓号详情*/
    public void getHjsjLiangNumber(HttpPost<PostAgentsLiangNumber> httpPost, Subscriber<HttpRequest<AgentsLiangNumber>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.getHjsjLiangNumber(httpPost), subscriber);
    }

    /*靓号平台-话机世界靓号获取 imsi(写卡、锁定)*/
    public void liangImsi(HttpPost<PostImsi> httpPost, Subscriber<HttpRequest<RequestImsi>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.liangImsi(httpPost), subscriber);
    }

//    /*靓号平台-话机世界靓号获取 imsi(写卡、锁定)*/
//    public void ePreNumberList(HttpPost<PostPreNumber> httpPost, Subscriber<HttpRequest<RequestPreNumber>> subscriber) {
//        HttpUtils.toSubscribe(cardInterface.ePreNumberList(httpPost), subscriber);
//    }

    /*靓号平台-话机世界靓号写卡成功记录表*/
    public void liangRecords(HttpPost<LiangRecords> httpPost, Subscriber<HttpRequest<LiangRecords>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.liangRecords(httpPost), subscriber);
    }

    /*靓号平台-话机世界靓号开户订单提交*/
    public void liangSetOpen(HttpPost<PostSetOpen> httpPost, Subscriber<HttpRequest<RequestSetOpen>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.liangSetOpen(httpPost), subscriber);
    }
    /*靓号平台-白卡预开户之白卡开户*/

    public void openInformation(HttpPost<PostOpenInformation> httpPost, Subscriber<HttpRequest> subscriber) {
        HttpUtils.toSubscribe(cardInterface.openInformation(httpPost), subscriber);
    }

    /*白卡预开户之预开户列表*/
    public void preNumberList(HttpPost<PostPreNumberList> httpPost, Subscriber<HttpRequest<RequestPreNumberList>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.preNumberList(httpPost), subscriber);
    }

    /*白卡预开户之渠道获取号码详情*/
    public void ePreNumber(HttpPost<PostPreNumber> httpPost, Subscriber<HttpRequest<RequestPreNumber>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.ePreNumber(httpPost), subscriber);
    }

    /*白卡预开户之号码池和靓号规则信息*/
    public void getPreWhiteNumberPool(HttpPost<PostPreNumberPool> httpPost, Subscriber<HttpRequest<RequestPreNumberPool>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.getPreWhiteNumberPool(httpPost), subscriber);
    }

    /*白卡预开户之随机号码*/
    public void preRandomNumber(HttpPost<PostPreRandomNumber> httpPost, Subscriber<HttpRequest<RequestPreRandomNumber>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.preRandomNumber(httpPost), subscriber);
    }

    /*白卡预开户之号码信息*/
    public void preNumberDetails(HttpPost<PostPreNumberDetails> httpPost, Subscriber<HttpRequest<RequestPreNumberDetails>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.preNumberDetails(httpPost), subscriber);
    }

    /*白卡预开户之获取预开户 IMSI*/
    public void getPreImsi(HttpPost<PostImsi> httpPost, Subscriber<HttpRequest<RequestImsi>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.getPreImsi(httpPost), subscriber);
    }

    /*白卡预开户之写白卡预开户表*/
    public void writePreDetails(HttpPost<PostWritePreDetails> httpPost, Subscriber<HttpRequest> subscriber) {
        HttpUtils.toSubscribe(cardInterface.writePreDetails(httpPost), subscriber);
    }
//    /*白卡预开户之写白卡预开户表*/
//    public void writePreDetails(HttpPost<> httpPost, Subscriber<HttpRequest> subscriber) {
//        HttpUtils.toSubscribe(cardInterface.liangSetOpen(httpPost), subscriber);
//    }

    /*白卡预开户之预开户列表*/
    public void liangPay(HttpPost<PostLiangPay> httpPost, Subscriber<HttpRequest<RequestLiangPay>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.liangPay(httpPost), subscriber);
    }

    public void isBond(HttpPost<PostCaution> httpPost, Subscriber<HttpRequest<RequestCaution>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.isBond(httpPost), subscriber);
    }

    public void bondAmount(HttpPost<PostCautionOrder> httpPost, Subscriber<HttpRequest<RequestCautionOrder>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.bondAmount(httpPost), subscriber);
    }

    public void bondOrder(HttpPost<PostPay> httpPost, Subscriber<HttpRequest<RequestReAdd>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.bondOrder(httpPost), subscriber);
    }

    public void address(HttpPost<PostAddress> httpPost, Subscriber<HttpRequest<String>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.address(httpPost), subscriber);
    }

    public void preNumberDetailsNew(HttpPost<PostPreNumberDetails> httpPost, Subscriber<HttpRequest<RequestPreNumberDetails>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.preNumberDetailsNew(httpPost), subscriber);
    }

    public void preNumberOrderInfo(HttpPost<PostJHOrderInfo> httpPost, Subscriber<HttpRequest<JHOrderInfo>> subscriber) {
        HttpUtils.toSubscribe(cardInterface.preNumberOrderInfo(httpPost), subscriber);
    }


    public void writeIn(Subscriber<HttpRequest<String>> subscriber, HttpPost<PostReSubmit> httpPost) {
        cardInterface.getReSubmit(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//    public void BkApply(HttpPost<PostBkApply> httpPost, Subscriber<HttpRequest<RequestOpenPower>>subscriber){
//        HttpUtils.toSubscribe( cardInterface.getBkApply( httpPost ),subscriber );
//    }

    public void BkApply(Subscriber<HttpRequest<RequestOpenPower>> subscriber, HttpPost<PostBkApply> httpPost) {
        cardInterface.getBkApply(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkDs(Subscriber<HttpRequest<RequestDs>> subscriber, HttpPost<PostDs> httpPost) {
        cardInterface.checkDs(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getQr(Subscriber<HttpRequest<RequestQrCode>> subscriber, HttpPost<PostQrCode> httpPost) {
        cardInterface.getQr(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
