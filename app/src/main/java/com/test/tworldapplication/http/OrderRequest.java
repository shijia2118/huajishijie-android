package com.test.tworldapplication.http;

import android.app.Activity;
import android.util.Log;

import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.RequestAgencyGetBill;
import com.test.tworldapplication.entity.RequestBkSelect;
import com.test.tworldapplication.entity.RequestChannelOpenCount;
import com.test.tworldapplication.entity.RequestChannelsList;
import com.test.tworldapplication.entity.RequestGetSonOrder;
import com.test.tworldapplication.entity.RequestGetSonOrderList;
import com.test.tworldapplication.entity.RequestOrderInfo;
import com.test.tworldapplication.entity.RequestOrderList;
import com.test.tworldapplication.entity.RequestRechargeList;
import com.test.tworldapplication.entity.RequestRemarkOrderInfo;
import com.test.tworldapplication.entity.RequestSelectionOrderInfo;
import com.test.tworldapplication.entity.RequestTransferList;
import com.test.tworldapplication.entity.RequestTransferOrderInfo;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import rx.Subscriber;

/**
 * Created by dasiy on 16/11/7.
 */

public class OrderRequest {
    public static Subscriber<HttpRequest<RequestOrderList>> orderKhList(final Activity activity, final CheckResultDialog dialog, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestOrderList>> subscriber = new Subscriber<HttpRequest<RequestOrderList>>() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onCompleted() {
                Log.d("aaa", "onCompleted");
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("aaa", "error");
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestOrderList> requestOrderListHttpRequest) {
                Log.d("aaa", requestOrderListHttpRequest.getMes());
//                onSuccess.OnSuccess(requestOrderListHttpRequest);
                Util.createToast(activity, requestOrderListHttpRequest.getMes());
                if (requestOrderListHttpRequest.getCode() == BaseCom.NORMAL)
                    onSuccess.OnSuccess(requestOrderListHttpRequest.getData());
                else if (requestOrderListHttpRequest.getCode() == BaseCom.LOSELOG || requestOrderListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }

            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestTransferList>> orderTransferList(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestTransferList> onSuccess) {
        Subscriber<HttpRequest<RequestTransferList>> subscriber = new Subscriber<HttpRequest<RequestTransferList>>() {
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
            public void onNext(HttpRequest<RequestTransferList> requestCardTransferListHttpRequest) {
                Log.d("aaa", requestCardTransferListHttpRequest.getMes());
                Util.createToast(activity, requestCardTransferListHttpRequest.getMes());
                if (requestCardTransferListHttpRequest.getCode() == BaseCom.NORMAL)
                    onSuccess.OnSuccess(requestCardTransferListHttpRequest.getData());
                else if (requestCardTransferListHttpRequest.getCode() == BaseCom.LOSELOG||requestCardTransferListHttpRequest.getCode()==BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestRechargeList>> orderRechargeList(final Activity context, final CheckResultDialog dialog, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestRechargeList>> subscriber = new Subscriber<HttpRequest<RequestRechargeList>>() {
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
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpRequest<RequestRechargeList> requestRechargeListHttpRequest) {
                Util.createToast(context, requestRechargeListHttpRequest.getMes());
                if (requestRechargeListHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestRechargeListHttpRequest.getData());
                } else if (requestRechargeListHttpRequest.getCode() == BaseCom.LOSELOG || requestRechargeListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(context, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestOrderInfo>> orderInfo(final Activity activity, final CheckResultDialog dialog, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestOrderInfo>> subscriber = new Subscriber<HttpRequest<RequestOrderInfo>>() {
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
            public void onNext(HttpRequest<RequestOrderInfo> requestOrderInfoHttpRequest) {
                Log.d("aaa", requestOrderInfoHttpRequest.getMes());
                Util.createToast(activity, requestOrderInfoHttpRequest.getMes());
                if (requestOrderInfoHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestOrderInfoHttpRequest.getData());
                } else if (requestOrderInfoHttpRequest.getCode() == BaseCom.LOSELOG || requestOrderInfoHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestSelectionOrderInfo>> selectOrderInfo(final Activity activity, final CheckResultDialog dialog, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestSelectionOrderInfo>> subscriber = new Subscriber<HttpRequest<RequestSelectionOrderInfo>>() {
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
            public void onNext(HttpRequest<RequestSelectionOrderInfo> requestSelectionOrderInfoHttpRequest) {
                Log.d("aaa", requestSelectionOrderInfoHttpRequest.getMes());
                Util.createToast(activity, requestSelectionOrderInfoHttpRequest.getMes());
                if (requestSelectionOrderInfoHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestSelectionOrderInfoHttpRequest.getData());
                } else if (requestSelectionOrderInfoHttpRequest.getCode() == BaseCom.LOSELOG || requestSelectionOrderInfoHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestTransferOrderInfo>> transferOrderInfo(final Activity activity, final CheckResultDialog dialog, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestTransferOrderInfo>> subscriber = new Subscriber<HttpRequest<RequestTransferOrderInfo>>() {
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
            public void onNext(HttpRequest<RequestTransferOrderInfo> requestTransferOrderInfoHttpRequest) {
                Log.d("aaa", requestTransferOrderInfoHttpRequest.getMes());
                Util.createToast(activity, requestTransferOrderInfoHttpRequest.getMes());
                if (requestTransferOrderInfoHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestTransferOrderInfoHttpRequest.getData());
                } else if (requestTransferOrderInfoHttpRequest.getCode() == BaseCom.LOSELOG||requestTransferOrderInfoHttpRequest.getCode()==BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestRemarkOrderInfo>> remarkOrderInfo(final Activity activity, final CheckResultDialog dialog, final SuccessValue onSuccess) {
        Subscriber<HttpRequest<RequestRemarkOrderInfo>> subscriber = new Subscriber<HttpRequest<RequestRemarkOrderInfo>>() {
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
            public void onNext(HttpRequest<RequestRemarkOrderInfo> requestRemarkOrderInfoHttpRequest) {
                Log.d("aaa", requestRemarkOrderInfoHttpRequest.getMes());
                Util.createToast(activity, requestRemarkOrderInfoHttpRequest.getMes());
                if (requestRemarkOrderInfoHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestRemarkOrderInfoHttpRequest.getData());
                } else if (requestRemarkOrderInfoHttpRequest.getCode() == BaseCom.LOSELOG||requestRemarkOrderInfoHttpRequest.getCode()==BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestChannelsList>> channelsList(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestChannelsList> onSuccess) {
        Subscriber<HttpRequest<RequestChannelsList>> subscriber = new Subscriber<HttpRequest<RequestChannelsList>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestChannelsList> requestChannelsListHttpRequest) {
                Log.d("aaa", requestChannelsListHttpRequest.getMes());
                Util.createToast(activity, requestChannelsListHttpRequest.getMes());
                if (requestChannelsListHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestChannelsListHttpRequest.getData());
                } else if (requestChannelsListHttpRequest.getCode() == BaseCom.LOSELOG || requestChannelsListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestChannelOpenCount>> channelsOpenCount(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestChannelOpenCount> onSuccess) {
        Subscriber<HttpRequest<RequestChannelOpenCount>> subscriber = new Subscriber<HttpRequest<RequestChannelOpenCount>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestChannelOpenCount> requestChannelOpenCountHttpRequest) {
                Log.d("aaa", requestChannelOpenCountHttpRequest.getMes());
                Util.createToast(activity, requestChannelOpenCountHttpRequest.getMes());
                if (requestChannelOpenCountHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestChannelOpenCountHttpRequest.getData());
                } else if (requestChannelOpenCountHttpRequest.getCode() == BaseCom.LOSELOG || requestChannelOpenCountHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }




    public static Subscriber<HttpRequest<RequestGetSonOrderList>> getSonOrderList(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestGetSonOrderList> onSuccess) {
        Subscriber<HttpRequest<RequestGetSonOrderList>> subscriber = new Subscriber<HttpRequest<RequestGetSonOrderList>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestGetSonOrderList> requestGetSonOrderListHttpRequest) {
                Log.d("aaa", requestGetSonOrderListHttpRequest.getMes());
                Util.createToast(activity, requestGetSonOrderListHttpRequest.getMes());
                if (requestGetSonOrderListHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestGetSonOrderListHttpRequest.getData());
                } else if (requestGetSonOrderListHttpRequest.getCode() == BaseCom.LOSELOG || requestGetSonOrderListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    //Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }



    public static Subscriber<HttpRequest<RequestGetSonOrder>> getSonOrder(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestGetSonOrder> onSuccess) {
        Subscriber<HttpRequest<RequestGetSonOrder>> subscriber = new Subscriber<HttpRequest<RequestGetSonOrder>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestGetSonOrder> requestChannelOpenCountHttpRequest) {
                Log.d("aaa", requestChannelOpenCountHttpRequest.getMes());
                Util.createToast(activity, requestChannelOpenCountHttpRequest.getMes());
                if (requestChannelOpenCountHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestChannelOpenCountHttpRequest.getData());
                } else if (requestChannelOpenCountHttpRequest.getCode() == BaseCom.LOSELOG || requestChannelOpenCountHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    //Util.gotoActy(activity, LoginActivity.class);
                    //AppManager.getAppManager().finishActivity();
                }
            }
        };
        return subscriber;
    }




    public static Subscriber<HttpRequest<RequestAgencyGetBill>> agencyGetBill(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestAgencyGetBill> onSuccess) {
        Subscriber<HttpRequest<RequestAgencyGetBill>> subscriber = new Subscriber<HttpRequest<RequestAgencyGetBill>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestAgencyGetBill> request) {
                Log.d("aaa", request.getMes());
                Util.createToast(activity, request.getMes());
                if (request.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(request.getData());
                } else if (request.getCode() == BaseCom.LOSELOG || request.getCode() == BaseCom.VERSIONINCORRENT) {

                }
            }
        };
        return subscriber;
    }



    public static Subscriber<HttpRequest<RequestBkSelect>> getBkApplyDetail(final Activity activity, final SuccessValue<RequestBkSelect> onSuccess) {
        Subscriber<HttpRequest<RequestBkSelect>> subscriber = new Subscriber<HttpRequest<RequestBkSelect>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestBkSelect> requestBkSelectHttpRequest) {
                Util.createToast(activity, requestBkSelectHttpRequest.getMes());
                Log.d("aaa", requestBkSelectHttpRequest.getMes());
                if (requestBkSelectHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestBkSelectHttpRequest.getData());
                } else if (requestBkSelectHttpRequest.getCode() == BaseCom.LOSELOG || requestBkSelectHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<String>> sendOrCancel(final Activity activity,final SuccessNull onSuccess) {
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


}
