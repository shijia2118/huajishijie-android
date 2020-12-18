package com.test.tworldapplication.http;

import android.app.Activity;
import android.util.Log;

import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.RequestCarousel;
import com.test.tworldapplication.entity.RequestMessage;
import com.test.tworldapplication.entity.RequestNoticeList;
import com.test.tworldapplication.entity.RequestNumberCheck;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import rx.Subscriber;

/**
 * Created by dasiy on 16/11/3.
 */

public class OtherRequest {
    public static Subscriber<HttpRequest<RequestNoticeList>> noticeList(final int flag, final Activity activity, final CheckResultDialog dialog, final SuccessValue onSucess) {
        Subscriber<HttpRequest<RequestNoticeList>> subscriber = new Subscriber<HttpRequest<RequestNoticeList>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestNoticeList> requestNoticeListHttpRequest) {
                Util.createToast(activity, requestNoticeListHttpRequest.getMes());
                if (requestNoticeListHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSucess.OnSuccess(requestNoticeListHttpRequest.getData());
                } else if (requestNoticeListHttpRequest.getCode() == BaseCom.LOSELOG || requestNoticeListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    if (flag != 0) {
                        Util.gotoActy(activity, LoginActivity.class);
                        //AppManager.getAppManager().finishActivity();
                        Log.d("ggg", "0");
                    }
                }
            }

        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestCarousel>> carousel(final Activity activity, final SuccessValue<RequestCarousel> onSuccess) {
        Subscriber<HttpRequest<RequestCarousel>> subscriber = new Subscriber<HttpRequest<RequestCarousel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestCarousel> requestCarouselHttpRequest) {
                Log.d("rrr", requestCarouselHttpRequest.getMes());
                if (requestCarouselHttpRequest.getCode() == BaseCom.NORMAL)
                    onSuccess.OnSuccess(requestCarouselHttpRequest.getData());
//                else if (requestCarouselHttpRequest.getCode() == BaseCom.LOSELOG)
//                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestMessage>> getNotice(final Activity activity, final SuccessValue<RequestMessage> onSuccess) {
        Subscriber<HttpRequest<RequestMessage>> subscriber = new Subscriber<HttpRequest<RequestMessage>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestMessage> requestMessageHttpRequest) {
                Log.d("aaa", requestMessageHttpRequest.getMes());
                Util.createToast(activity, requestMessageHttpRequest.getMes());
                if (requestMessageHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestMessageHttpRequest.getData());
                } else if (requestMessageHttpRequest.getCode() == BaseCom.LOSELOG || requestMessageHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }

            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestNumberCheck>> numberCheck(final Activity context, final CheckResultDialog dialog, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<RequestNumberCheck>> subscriber = new Subscriber<HttpRequest<RequestNumberCheck>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestNumberCheck> requestNumberCheckHttpRequest) {
                Util.createToast(context, requestNumberCheckHttpRequest.getMes());
                if (requestNumberCheckHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                } else if (requestNumberCheckHttpRequest.getCode() == BaseCom.LOSELOG || requestNumberCheckHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(context, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestPictureUpload>> pictureUpload(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestPictureUpload> onSuccess) {
        Subscriber<HttpRequest<RequestPictureUpload>> subscriber = new Subscriber<HttpRequest<RequestPictureUpload>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                Log.d("ccc", e.toString());
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestPictureUpload> requestPictureUploadHttpRequest) {
                Log.d("aaa", requestPictureUploadHttpRequest.getMes());
//                Util.createToast(activity, requestPictureUploadHttpRequest.getMes());
                if (requestPictureUploadHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestPictureUploadHttpRequest.getData());
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest> functionStatistics(final SuccessNull onSuccess) {
        Subscriber<HttpRequest> subscriber = new Subscriber<HttpRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest httpRequest) {
                if (httpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                }
            }
        };
        return subscriber;
    }
}
