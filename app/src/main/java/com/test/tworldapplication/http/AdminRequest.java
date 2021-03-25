package com.test.tworldapplication.http;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.test.tworldapplication.activity.admin.WriteInActivity;
import com.test.tworldapplication.activity.admin.WriteInNewActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.CheckInfo;
import com.test.tworldapplication.entity.HttpRequest;
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
import com.test.tworldapplication.entity.RequestModify;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.entity.RequestPinCreate;
import com.test.tworldapplication.entity.RequestPinModify;
import com.test.tworldapplication.entity.RequestRegister;
import com.test.tworldapplication.entity.RequestStatistic;
import com.test.tworldapplication.entity.VerificationCode;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dasiy on 16/11/1.
 */

public class AdminRequest {
    public static Subscriber<HttpRequest<RequestLogin>> Login(final String userName, final String passWord, final Activity context, final SuccessNull onSuccess, final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestLogin>> subscriber = new Subscriber<HttpRequest<RequestLogin>>() {

            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestLogin> loginResult) {
                int code = loginResult.getCode();
                Util.createToast(context, loginResult.getMes());
                Log.d("aaa", loginResult.getMes());
                if (loginResult.getCode() == BaseCom.NORMAL) {
                    BaseCom.login = loginResult.getData();
                    SharedPreferences share = context.getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
                    SharedPreferences.Editor edit = share.edit(); //编辑文件
                    edit.clear();
                    edit.putString("session_token", loginResult.getData().getSession_token());
                    edit.putString("gride", loginResult.getData().getGrade());
                    edit.commit();  //保存数据信息

                    SharedPreferences sharedPreferences = context.getSharedPreferences(BaseCom.ADMIN, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit(); //编辑文件
                    editor.putString("user", userName);
                    editor.putString("password", passWord);

                    editor.commit();  //保存数据信息

                    if (loginResult.getData().getIsLogin().equals("Y")) {
                        AppManager.getAppManager().finishActivity();
                        Intent intent = new Intent();
//                        intent.putExtra("from", "0");
                        intent.setClass(context, WriteInNewActivity.class);
                        context.startActivity(intent);
//                        onSuccess.onSuccess();
                    } else if (loginResult.getData().getIsLogin().equals("N")) {
//                        SharedPreferences sharedPreferences0 = context.getSharedPreferences("mySP", MODE_PRIVATE);
//                        SharedPreferences.Editor editor0 = sharedPreferences0.edit(); //编辑文件
//                        editor0.putString("YorN", "0");
//                        editor0.commit();  //保存数据信息
                        onSuccess.onSuccess();
//                        AppManager.getAppManager().finishActivity();
//                        Util.gotoActy( context,WriteInActivity.class );
                    }
                }
//                if (BaseCom.OPERATION_LOWER < code && BaseCom.OPERATION_UPER > code) {
//                    Util.createToast(context, "用户名或密码不正确!");
//                } else {
//                    BaseCom.login = loginResult.getData();
//                    Util.createToast(context, "登录成功!");
//                    onSuccess.onSuccess();
//
//                }

            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestRegister>> register(final SuccessValue<HttpRequest<RequestRegister>> successValue, final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestRegister>> subscriber = new Subscriber<HttpRequest<RequestRegister>>() {

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
            public void onNext(HttpRequest<RequestRegister> requestRegisterHttpRequest) {
                successValue.OnSuccess(requestRegisterHttpRequest);


            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestCode>> getCode(final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestCode>> subscriber = new Subscriber<HttpRequest<RequestCode>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestCode> requestCodeHttpRequest) {

                Log.d("aaa", requestCodeHttpRequest.getMes());
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestCode>> getCode(Activity activity, final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestCode>> subscriber = new Subscriber<HttpRequest<RequestCode>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestCode> requestCodeHttpRequest) {
                if (!TextUtils.isEmpty(requestCodeHttpRequest.getMes())) {
                    Util.createToast(activity, requestCodeHttpRequest.getMes());
                    Log.d("aaa", requestCodeHttpRequest.getMes());
                }
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<VerificationCode>> checkNumber(final CheckResultDialog dialog) {
        Subscriber<HttpRequest<VerificationCode>> subscriber = new Subscriber<HttpRequest<VerificationCode>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<VerificationCode> requestCodeHttpRequest) {
                Log.d("aaa", requestCodeHttpRequest.getMes());
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestCaptcha>> verifyCaptcha(final Activity activity, final CheckResultDialog dialog, final SuccessNull OnSuccess) {
        Subscriber<HttpRequest<RequestCaptcha>> subscriber = new Subscriber<HttpRequest<RequestCaptcha>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestCaptcha> requestCaptchaHttpRequest) {
                Log.d("aaa", requestCaptchaHttpRequest.getMes());
                Util.createToast(activity, requestCaptchaHttpRequest.getMes());
                if (requestCaptchaHttpRequest.getCode() == BaseCom.NORMAL) {
                    OnSuccess.onSuccess();
                }



            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestAdmin>> getAdmin(final Activity activity, final SuccessValue<RequestAdmin> onSuccess) {
        Subscriber<HttpRequest<RequestAdmin>> subscriber = new Subscriber<HttpRequest<RequestAdmin>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestAdmin> requestAdminHttpRequest) {
                Util.createToast(activity, requestAdminHttpRequest.getMes());
                Log.d("aaa", requestAdminHttpRequest.getMes());
                if (requestAdminHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestAdminHttpRequest.getData());
                } else if (requestAdminHttpRequest.getCode() == BaseCom.LOSELOG || requestAdminHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestModify>> modifyPwd(final SuccessValue<HttpRequest<RequestModify>> successValue, final CheckResultDialog dialog) {
        Subscriber<HttpRequest<RequestModify>> subscriber = new Subscriber<HttpRequest<RequestModify>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestModify> requestAdminHttpRequest) {
                successValue.OnSuccess(requestAdminHttpRequest);

            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestStatistic>> statistic(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestStatistic> onSuccess) {
        Subscriber<HttpRequest<RequestStatistic>> subscriber = new Subscriber<HttpRequest<RequestStatistic>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestStatistic> requestStatisticHttpRequest) {
//                Util.createToast(activity, requestStatisticHttpRequest.getMes());
                Log.d("aaa", requestStatisticHttpRequest.getMes());
                if (requestStatisticHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestStatisticHttpRequest.getData());
                } else if (requestStatisticHttpRequest.getCode() == BaseCom.LOSELOG || requestStatisticHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
                Log.d("ggg", "0");
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestFeedback>> feedback(final Activity activity, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<RequestFeedback>> subscriber = new Subscriber<HttpRequest<RequestFeedback>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestFeedback> requestFeedbackHttpRequest) {
                Log.d("aaa", requestFeedbackHttpRequest.getMes());
                Util.createToast(activity, requestFeedbackHttpRequest.getMes());
                if (requestFeedbackHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                } else if (requestFeedbackHttpRequest.getCode() == BaseCom.LOSELOG || requestFeedbackHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestPinCreate>> pinCreate(final Activity context, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<RequestPinCreate>> subscriber = new Subscriber<HttpRequest<RequestPinCreate>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPinCreate> requestPinCreateHttpRequest) {
                Log.d("aaa", requestPinCreateHttpRequest.getMes());
                Util.createToast(context, requestPinCreateHttpRequest.getMes());
                if (requestPinCreateHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                } else if (requestPinCreateHttpRequest.getCode() == BaseCom.LOSELOG || requestPinCreateHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(context, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestPinModify>> pinModeify(final SuccessValue<HttpRequest<RequestPinModify>> successValue) {
        Subscriber<HttpRequest<RequestPinModify>> subscriber = new Subscriber<HttpRequest<RequestPinModify>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPinModify> requestPinModifyHttpRequest) {
                Log.d("aaa", requestPinModifyHttpRequest.getMes());
                successValue.OnSuccess(requestPinModifyHttpRequest);


            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestLogout>> logout(final Activity activity, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<RequestLogout>> subscriber = new Subscriber<HttpRequest<RequestLogout>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestLogout> requestLogoutHttpRequest) {
                Log.d("aaa", requestLogoutHttpRequest.getMes());
                Util.createToast(activity, requestLogoutHttpRequest.getMes());
                if (requestLogoutHttpRequest.getCode() == BaseCom.NORMAL)
                    onSuccess.onSuccess();
                else if (requestLogoutHttpRequest.getCode() == BaseCom.LOSELOG || requestLogoutHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestForgrtPwd>> forgetPwd(final CheckResultDialog dialog, final SuccessValue<HttpRequest<RequestForgrtPwd>> successValue) {
        Subscriber<HttpRequest<RequestForgrtPwd>> subscriber = new Subscriber<HttpRequest<RequestForgrtPwd>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestForgrtPwd> requestForgrtPwdHttpRequest) {
                successValue.OnSuccess(requestForgrtPwdHttpRequest);

            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<RequestCommissions>> commissionsQuery(final Activity activity, final CheckResultDialog dialog, final SuccessValue<RequestCommissions> onSuccess) {
        Subscriber<HttpRequest<RequestCommissions>> subscriber = new Subscriber<HttpRequest<RequestCommissions>>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
            }

            @Override
            public void onNext(HttpRequest<RequestCommissions> requestCommissionsHttpRequest) {
                Log.d("aaa", requestCommissionsHttpRequest.getMes());
                Util.createToast(activity, requestCommissionsHttpRequest.getMes());
                if (requestCommissionsHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestCommissionsHttpRequest.getData());
                } else if (requestCommissionsHttpRequest.getCode() == BaseCom.LOSELOG || requestCommissionsHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;

    }

    public static Subscriber<HttpRequest<RequestIntroduction>> introduction(final Activity activity, final SuccessValue<RequestIntroduction> onSuccess) {
        Subscriber<HttpRequest<RequestIntroduction>> subscriber = new Subscriber<HttpRequest<RequestIntroduction>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestIntroduction> requestIntroductionHttpRequest) {
                Util.createToast(activity, requestIntroductionHttpRequest.getMes());
                if (requestIntroductionHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestIntroductionHttpRequest.getData());
                } else if (requestIntroductionHttpRequest.getCode() == BaseCom.LOSELOG || requestIntroductionHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<RequestGetAccountPeriodList>> getAccountPeriodList(final Activity activity, final SuccessValue<RequestGetAccountPeriodList> onSuccess) {
        Subscriber<HttpRequest<RequestGetAccountPeriodList>> subscriber = new Subscriber<HttpRequest<RequestGetAccountPeriodList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestGetAccountPeriodList> requestIntroductionHttpRequest) {
                Util.createToast(activity, requestIntroductionHttpRequest.getMes());
                if (requestIntroductionHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestIntroductionHttpRequest.getData());
                } else if (requestIntroductionHttpRequest.getCode() == BaseCom.LOSELOG || requestIntroductionHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<RequestGetAccountPeriod>> getAccountPeriod(final Activity activity, final SuccessValue<RequestGetAccountPeriod> onSuccess) {
        Subscriber<HttpRequest<RequestGetAccountPeriod>> subscriber = new Subscriber<HttpRequest<RequestGetAccountPeriod>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpRequest<RequestGetAccountPeriod> requestIntroductionHttpRequest) {
                Util.createToast(activity, requestIntroductionHttpRequest.getMes());
                if (requestIntroductionHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestIntroductionHttpRequest.getData());
                } else if (requestIntroductionHttpRequest.getCode() == BaseCom.LOSELOG || requestIntroductionHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<String>> updateInfo(final Activity activity, final SuccessNull onSuccess) {
        Subscriber<HttpRequest<String>> subscriber = new Subscriber<HttpRequest<String>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<String> requestUpdatePersonalInfoHttpRequest) {
                Log.d("aaa", requestUpdatePersonalInfoHttpRequest.getMes());
                Util.createToast(activity, requestUpdatePersonalInfoHttpRequest.getMes());
                if (requestUpdatePersonalInfoHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.onSuccess();
                } else if (requestUpdatePersonalInfoHttpRequest.getCode() == BaseCom.LOSELOG || requestUpdatePersonalInfoHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }


    public static Subscriber<HttpRequest<RequestOpenPower>> getOpenPower(final Activity activity, final SuccessValue<RequestOpenPower> onSuccess) {
        Subscriber<HttpRequest<RequestOpenPower>> subscriber = new Subscriber<HttpRequest<RequestOpenPower>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestOpenPower> requestOpenPowerHttpRequest) {
                Log.d("aaa", requestOpenPowerHttpRequest.getMes());
                Util.createToast(activity, requestOpenPowerHttpRequest.getMes());
                if (requestOpenPowerHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(requestOpenPowerHttpRequest.getData());
                } else if (requestOpenPowerHttpRequest.getCode() == BaseCom.LOSELOG || requestOpenPowerHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

    public static Subscriber<HttpRequest<CheckInfo>> checkInfo(final Activity activity, final SuccessValue<CheckInfo> onSuccess) {
        Subscriber<HttpRequest<CheckInfo>> subscriber = new Subscriber<HttpRequest<CheckInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<CheckInfo> checkInfoHttpRequest) {
                Log.d("aaa", checkInfoHttpRequest.getMes());
                Util.createToast(activity, checkInfoHttpRequest.getMes());
                if (checkInfoHttpRequest.getCode() == BaseCom.NORMAL) {
                    onSuccess.OnSuccess(checkInfoHttpRequest.getData());
                } else if (checkInfoHttpRequest.getCode() == BaseCom.LOSELOG || checkInfoHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(activity, LoginActivity.class);
            }
        };
        return subscriber;
    }

}
