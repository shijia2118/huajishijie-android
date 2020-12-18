package com.test.tworldapplication.http;

import android.app.Activity;
import android.util.Log;

import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.order.RequestQrCode;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.RequestDs;
import com.test.tworldapplication.entity.RequestLockNumber;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestJudgeIsLiang;
import com.test.tworldapplication.entity.RequestMode;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.entity.RequestPreOpen;
import com.test.tworldapplication.entity.RequestSelectionCheck;
import com.test.tworldapplication.entity.RequestTips;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestMoney;
import com.test.tworldapplication.entity.RequestNumberPool;
import com.test.tworldapplication.entity.RequestNumberSegment;
import com.test.tworldapplication.entity.RequestOpen;
import com.test.tworldapplication.entity.RequestPromotion;
import com.test.tworldapplication.entity.RequestRandomNumber;
import com.test.tworldapplication.entity.RequestRepair;
import com.test.tworldapplication.entity.RequestTransfer;
import com.test.tworldapplication.entity.RequestWhiteOpen;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import rx.Subscriber;

/**
 * Created by dasiy on 16/11/4.
 */

public class CardRequest {
    public static Subscriber<HttpRequest<RequestNumberSegment>> numberSegment(final SuccessValue<HttpRequest<RequestNumberSegment>> successValue) {
        Subscriber<HttpRequest<RequestNumberSegment>> subscriber = new Subscriber<HttpRequest<RequestNumberSegment>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestNumberSegment> requestNumberSegmentHttpRequest) {
                Log.d("aaa", requestNumberSegmentHttpRequest.getMes());
                successValue.OnSuccess(requestNumberSegmentHttpRequest);


            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestTransfer>> transfer(final SuccessValue<HttpRequest<RequestTransfer>> successValue, final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestTransfer>> subscriber = new Subscriber<HttpRequest<RequestTransfer>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestTransfer> requestTranferHttpRequest) {
                successValue.OnSuccess(requestTranferHttpRequest);

            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestRepair>> repair(final SuccessValue<HttpRequest<RequestRepair>> successValue, final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestRepair>> subscriber = new Subscriber<HttpRequest<RequestRepair>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestRepair> requestRepairHttpRequest) {
                Log.d("aaa", requestRepairHttpRequest.getMes());
                successValue.OnSuccess(requestRepairHttpRequest);

            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestCheck>> check(final SuccessValue<HttpRequest<RequestCheck>> successValue, final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestCheck>> subscriber = new Subscriber<HttpRequest<RequestCheck>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestCheck> requestCheckHttpRequest) {
                successValue.OnSuccess(requestCheckHttpRequest);


            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestSelectionCheck>> checkSelect(final Activity activity, final SuccessValue<RequestSelectionCheck> onSuccess) {
        Subscriber<HttpRequest<RequestSelectionCheck>> subscriber = new Subscriber<HttpRequest<RequestSelectionCheck>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpRequest<RequestSelectionCheck> requestSelectionCheckHttpRequest) {
//                successValue.OnSuccess(requestSelectionCheckHttpRequest);
                Log.d("aaa", requestSelectionCheckHttpRequest.getMes());
                Util.createToast(activity, requestSelectionCheckHttpRequest.getMes());
                if (requestSelectionCheckHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestSelectionCheckHttpRequest.getData());
                } else if (requestSelectionCheckHttpRequest.getCode() == BaseCom.LOSELOG) {
                    Util.gotoActy( activity, LoginActivity.class );
                }else if( requestSelectionCheckHttpRequest.getCode() == BaseCom.VERSIONINCORRENT){
                    Util.createToast(activity, requestSelectionCheckHttpRequest.getMes());
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestPromotion>> promotion(final Activity activity, final CheckResultDialog dialog, final Activity context, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestPromotion>> subscriber = new Subscriber<HttpRequest<RequestPromotion>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestPromotion> requestPromotionHttpRequest) {
                Log.d("aaa", requestPromotionHttpRequest.getMes());
                Util.createToast(context, requestPromotionHttpRequest.getMes());
                if (requestPromotionHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestPromotionHttpRequest.getData());
                } else if (requestPromotionHttpRequest.getCode() == BaseCom.LOSELOG || requestPromotionHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestMoney>> money(final Activity context, final SuccessValue<RequestMoney> onSuccess) {
        Subscriber<HttpRequest<RequestMoney>> subscriber = new Subscriber<HttpRequest<RequestMoney>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.getMessage();

            }

            @Override
            public void onNext(HttpRequest<RequestMoney> requestMoneyHttpRequest) {
                Util.createToast(context, requestMoneyHttpRequest.getMes());
                if (requestMoneyHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestMoneyHttpRequest.getData());
                } else if (requestMoneyHttpRequest.getCode() == BaseCom.LOSELOG || requestMoneyHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(context, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestOpen>> open(final Activity activity, final CheckResultDialog dialog, final SuccessValue<HttpRequest<RequestOpen>> onSuccess) {
        Subscriber<HttpRequest<RequestOpen>> subscriber = new Subscriber<HttpRequest<RequestOpen>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestOpen> requestOpenHttpRequest) {
                Log.d("aaa", requestOpenHttpRequest.getMes());
                onSuccess.OnSuccess(requestOpenHttpRequest);
//                Util.createToast(activity, requestOpenHttpRequest.getMes());
//                if (requestOpenHttpRequest.getCode() == BaseCom.NORMAL) {
//
//                } else if (requestOpenHttpRequest.getCode() == BaseCom.LOSELOG)
//                    Util.gotoActy(activity, LoginActivity.class);
//
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestWhiteOpen>> whiteSetOpen(final Activity activity, final CheckResultDialog dialog, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<RequestWhiteOpen>> subscriber = new Subscriber<HttpRequest<RequestWhiteOpen>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestWhiteOpen> requestWhiteOpenHttpRequest) {
                Util.createToast(activity, requestWhiteOpenHttpRequest.getMes());
                if (requestWhiteOpenHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                } else if (requestWhiteOpenHttpRequest.getCode() == BaseCom.LOSELOG || requestWhiteOpenHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }

        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestNumberPool>> whiteNumberPool(final Activity activity, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestNumberPool>> subscriber = new Subscriber<HttpRequest<RequestNumberPool>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestNumberPool> requestNumberPoolHttpRequest) {
                Log.d("aaa", requestNumberPoolHttpRequest.getMes());
                if (requestNumberPoolHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestNumberPoolHttpRequest.getData());
                } else if (requestNumberPoolHttpRequest.getCode() == BaseCom.LOSELOG || requestNumberPoolHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<RequestRandomNumber>> randomNumber(final Activity activity, final CheckResultDialog dialog, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestRandomNumber>> subscriber = new Subscriber<HttpRequest<RequestRandomNumber>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestRandomNumber> requestRandomNumberHttpRequest) {
                Log.d("aaa", requestRandomNumberHttpRequest.getMes());
                Log.d("aaa", requestRandomNumberHttpRequest.getData().getNumbers().length + "");
                Util.createToast(activity, requestRandomNumberHttpRequest.getMes());
                if (requestRandomNumberHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestRandomNumberHttpRequest.getData());
                } else if (requestRandomNumberHttpRequest.getCode() == BaseCom.LOSELOG || requestRandomNumberHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestLockNumber>> lockNumber(final Activity activity, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<RequestLockNumber>> subscriber = new Subscriber<HttpRequest<RequestLockNumber>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLockNumber> requestLockNumberHttpRequest) {
                Log.d("aaa", requestLockNumberHttpRequest.getMes());
                Util.createToast(activity, requestLockNumberHttpRequest.getMes());
                if (requestLockNumberHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                } else if (requestLockNumberHttpRequest.getCode() == BaseCom.LOSELOG || requestLockNumberHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestImsi>> imsi(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestImsi> onSuccess) {
        Subscriber<HttpRequest<RequestImsi>> subscriber = new Subscriber<HttpRequest<RequestImsi>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpRequest<RequestImsi> requestImsiHttpRequest) {
                Log.d("aaa", requestImsiHttpRequest.getMes());
                Util.createToast(activity, requestImsiHttpRequest.getMes());
                if (requestImsiHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestImsiHttpRequest.getData());
                } else if (requestImsiHttpRequest.getCode() == BaseCom.LOSELOG | requestImsiHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestPreOpen>> preopen(final Activity activity, final CheckResultDialog dialog, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<RequestPreOpen>> subscriber = new Subscriber<HttpRequest<RequestPreOpen>>() {
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
            public void onNext(HttpRequest<RequestPreOpen> requestPreOpenHttpRequest) {
                Util.createToast(activity, requestPreOpenHttpRequest.getMes());
                if (requestPreOpenHttpRequest.getCode() == BaseCom.NORMAL)
                    onSuccess.onSuccess();
                else if (requestPreOpenHttpRequest.getCode() == BaseCom.LOSELOG || requestPreOpenHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestMode>> openMode(final SuccessValue<HttpRequest<RequestMode>> successValue) {
        Subscriber<HttpRequest<RequestMode>> subscriber = new Subscriber<HttpRequest<RequestMode>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestMode> requestModeHttpRequest) {
                successValue.OnSuccess(requestModeHttpRequest);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestJudgeIsLiang>> judgeIsLiang(final SuccessValue<HttpRequest<RequestJudgeIsLiang>> successValue) {
        Subscriber<HttpRequest<RequestJudgeIsLiang>> subscriber = new Subscriber<HttpRequest<RequestJudgeIsLiang>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestJudgeIsLiang> requestJudgeIsLiangHttpRequest) {
                successValue.OnSuccess(requestJudgeIsLiangHttpRequest);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestTips>> getTips(final SuccessValue<HttpRequest<RequestTips>> successValue) {
        Subscriber<HttpRequest<RequestTips>> subscriber = new Subscriber<HttpRequest<RequestTips>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestTips> requestTipsHttpRequest) {
                successValue.OnSuccess(requestTipsHttpRequest);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<String>>writeIn(final SuccessValue<HttpRequest<String>>successValue) {
        Subscriber<HttpRequest<String>> subscriber = new Subscriber<HttpRequest<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<String> stringHttpRequest) {
                successValue.OnSuccess( stringHttpRequest );
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestOpenPower>>bkApply(final SuccessValue<HttpRequest<RequestOpenPower>>successValue){
        Subscriber<HttpRequest<RequestOpenPower>> subscriber=new Subscriber<HttpRequest<RequestOpenPower>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestOpenPower> requestOpenPowerHttpRequest) {
                Log.d("aaa", requestOpenPowerHttpRequest.getMes());
                successValue.OnSuccess( requestOpenPowerHttpRequest );
//                AppManager.getAppManager().finishActivity();
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<String>> reSubmit(final Activity activity,final SuccessNull onSuccess) {
        Subscriber<HttpRequest<String>> subscriber = new Subscriber<HttpRequest<String>>() {


            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<String> stringHttpRequest) {
                Log.d("aaa", stringHttpRequest.getMes());
                Util.createToast(activity, stringHttpRequest.getMes());
                if (stringHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                } else if (stringHttpRequest.getCode() == BaseCom.LOSELOG || stringHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<RequestDs>> checkDs(final Activity activity, final SuccessValue<RequestDs> onSuccess) {
        Subscriber<HttpRequest<RequestDs>> subscriber = new Subscriber<HttpRequest<RequestDs>>() {


            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestDs> requestDsHttpRequest) {
                Log.d("aaa", requestDsHttpRequest.getMes());
                Util.createToast(activity, requestDsHttpRequest.getMes());
                if (requestDsHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestDsHttpRequest.getData());
                } else if (requestDsHttpRequest.getCode() == BaseCom.LOSELOG ){
                    Util.gotoActy(activity, LoginActivity.class);
                }else if( requestDsHttpRequest.getCode() == BaseCom.VERSIONINCORRENT){
                    Util.createToast(activity, requestDsHttpRequest.getMes());
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestQrCode>> getQr(final Activity activity, final SuccessValue<RequestQrCode> onSuccess) {
        Subscriber<HttpRequest<RequestQrCode>> subscriber = new Subscriber<HttpRequest<RequestQrCode>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestQrCode> requestQrCodeHttpRequest) {
                Util.createToast(activity, requestQrCodeHttpRequest.getMes());
                if (requestQrCodeHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestQrCodeHttpRequest.getData());
                } else if (requestQrCodeHttpRequest.getCode() == BaseCom.LOSELOG || requestQrCodeHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


}
