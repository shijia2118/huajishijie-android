package com.test.tworldapplication.http;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Carousel;
import com.test.tworldapplication.entity.CarouselBitmap;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostCarousel;
import com.test.tworldapplication.entity.PostFunction;
import com.test.tworldapplication.entity.PostMessage;
import com.test.tworldapplication.entity.PostNoticeList;
import com.test.tworldapplication.entity.PostNumberCheck;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.RequestCarousel;
import com.test.tworldapplication.entity.RequestCarouselBitmap;
import com.test.tworldapplication.entity.RequestMessage;
import com.test.tworldapplication.entity.RequestNoticeList;
import com.test.tworldapplication.entity.RequestNumberCheck;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.inter.OtherInterface;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dasiy on 16/11/3.
 */

public class OtherHttp {
    private Retrofit retrofit;
    private OtherInterface otherInterface;

    public OtherHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(BaseCom.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BaseCom.BASE_URL)
                .client(getTokenOkHttpClient())
                .build();

        otherInterface = retrofit.create(OtherInterface.class);
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

    /*消息列表*/
    public void noticeList(Subscriber<HttpRequest<RequestNoticeList>> subscriber, HttpPost<PostNoticeList> httpPost) {
        otherInterface.noticeList(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*轮播图*/
    public void carouselbitmap(Subscriber<HttpRequest<RequestCarouselBitmap>> subscriber, final HttpPost<PostCarousel> httpPost) {
        otherInterface.carousel(httpPost).map(new Func1<HttpRequest<RequestCarousel>, HttpRequest<RequestCarouselBitmap>>() {

            @Override
            public HttpRequest<RequestCarouselBitmap> call(HttpRequest<RequestCarousel> requestCarouselHttpRequest) {
                Carousel[] carousels = requestCarouselHttpRequest.getData().getCarousel_Picture();
                CarouselBitmap[] carouselBitmaps = null;
                for (int i = 0; i < carousels.length; i++) {
                    try {
                        //创建一个url对象
                        URL url = new URL("http://www.baidu.com/img/bdlogo.png");
                        //打开URL对应的资源输入流
                        InputStream is = new URL(carousels[i].getUrl()).openStream();
                        //从InputStream流中解析出图片
                        carouselBitmaps[i].setBitmap(BitmapFactory.decodeStream(is));
                        carouselBitmaps[i].setJumpUrl(carousels[i].getJumpUrl());
                        //  imageview.setImageBitmap(bitmap);
                        //发送消息，通知UI组件显示图片
                        //关闭输入流
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                RequestCarouselBitmap requestCarouselBitmap = new RequestCarouselBitmap();
                requestCarouselBitmap.setCount(requestCarouselHttpRequest.getData().getCount());
                requestCarouselBitmap.setCarousel_Picture(carouselBitmaps);
                HttpRequest<RequestCarouselBitmap> httpRequest = new HttpRequest<RequestCarouselBitmap>();
                httpRequest.setCode(requestCarouselHttpRequest.getCode());
                httpRequest.setMes(requestCarouselHttpRequest.getMes());
                httpRequest.setData(requestCarouselBitmap);
                return httpRequest;
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void carousel(Subscriber<HttpRequest<RequestCarousel>> subscriber, final HttpPost<PostCarousel> httpPost) {
        otherInterface.carousel(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*消息具体内容*/
    public void getNotice(Subscriber<HttpRequest<RequestMessage>> subscriber, HttpPost<PostMessage> httpPost) {
        otherInterface.getNotice(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*手机号校验*/
    public void numberCheck(Subscriber<HttpRequest<RequestNumberCheck>> subscriber, HttpPost<PostNumberCheck> httpPost) {
        otherInterface.numberCheck(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*图片上传*/
    public void pictureUpload(Subscriber<HttpRequest<RequestPictureUpload>> subscriber, HttpPost<PostPictureUpload> httpPost) {
        otherInterface.pictureUpload(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*功能统计*/
    public void functionStatistics(Subscriber<HttpRequest> subscriber, HttpPost<PostFunction> httpPost) {
        otherInterface.functionStatistics(httpPost).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
