package com.test.tworldapplication.inter;

import com.test.tworldapplication.entity.CheckInfo;
import com.test.tworldapplication.entity.PostBkApply;
import com.test.tworldapplication.entity.PostGetAccountPeriod;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.PostUpdatePersonalInfo;
import com.test.tworldapplication.entity.RequestGetAccountPeriod;
import com.test.tworldapplication.entity.PostGetAccountPeriodList;
import com.test.tworldapplication.entity.RequestGetAccountPeriodList;
import com.test.tworldapplication.entity.PostWinningOrder;
import com.test.tworldapplication.entity.RequestLuckNumber;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostAdmin;
import com.test.tworldapplication.entity.PostCaptcha;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostCommissions;
import com.test.tworldapplication.entity.PostFeedback;
import com.test.tworldapplication.entity.PostForgetPwd;
import com.test.tworldapplication.entity.PostIntroduction;
import com.test.tworldapplication.entity.PostLogout;
import com.test.tworldapplication.entity.PostLuckNumber;
import com.test.tworldapplication.entity.PostModify;
import com.test.tworldapplication.entity.PostOrderProduct;
import com.test.tworldapplication.entity.PostPinCreate;
import com.test.tworldapplication.entity.PostPinModify;
import com.test.tworldapplication.entity.PostProduct;
import com.test.tworldapplication.entity.PostRecord;
import com.test.tworldapplication.entity.PostRegister;
import com.test.tworldapplication.entity.PostStatistic;
import com.test.tworldapplication.entity.PostVerification;
import com.test.tworldapplication.entity.RequestAdmin;
import com.test.tworldapplication.entity.RequestCaptcha;
import com.test.tworldapplication.entity.RequestCode;
import com.test.tworldapplication.entity.RequestCommissions;
import com.test.tworldapplication.entity.RequestFeedback;
import com.test.tworldapplication.entity.RequestForgrtPwd;
import com.test.tworldapplication.entity.RequestIntroduction;
import com.test.tworldapplication.entity.RequestLogin;
import com.test.tworldapplication.entity.PostLogin;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.RequestLogout;
import com.test.tworldapplication.entity.RequestLuckeyDraw;
import com.test.tworldapplication.entity.RequestModify;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.entity.RequestOrderProduct;
import com.test.tworldapplication.entity.RequestPinCreate;
import com.test.tworldapplication.entity.RequestPinModify;
import com.test.tworldapplication.entity.RequestProduct;
import com.test.tworldapplication.entity.RequestRegister;
import com.test.tworldapplication.entity.RequestStatistic;
import com.test.tworldapplication.entity.RequestUpdatePersonalInfo;
import com.test.tworldapplication.entity.RequestWinningOrder;
import com.test.tworldapplication.entity.VerificationCode;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by dasiy on 16/10/31.
 */

public interface AdminInterface {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_login")
    Observable<HttpRequest<RequestLogin>> login(@Body HttpPost<PostLogin> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getLuckNumbers")
    Observable<HttpRequest<RequestLuckNumber>> getLuckNumbers(@Body HttpPost<PostLuckNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_luckyDraw")
    Observable<HttpRequest<RequestLuckeyDraw>> luckyDraw(@Body HttpPost<PostLuckNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getWinningOrders")
    Observable<HttpRequest<RequestWinningOrder>> getWinningOrders(@Body HttpPost<PostWinningOrder> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_register")
    Observable<HttpRequest<RequestRegister>> register(@Body HttpPost<PostRegister> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_sendCaptcha")
    Observable<HttpRequest<RequestCode>> getCode(@Body HttpPost<PostCode> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_verifyCaptcha")
    Observable<HttpRequest<RequestCaptcha>> verifyCaptcha(@Body HttpPost<PostCaptcha> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_information")
    Observable<HttpRequest<RequestAdmin>> getAdmin(@Body HttpPost<PostAdmin> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_modifyPwd")
    Observable<HttpRequest<RequestModify>> modifyPwd(@Body HttpPost<PostModify> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_statistic")
    Observable<HttpRequest<RequestStatistic>> statistic(@Body HttpPost<PostStatistic> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_feedback")
    Observable<HttpRequest<RequestFeedback>> feedback(@Body HttpPost<PostFeedback> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_user_pinCreate")
    Observable<HttpRequest<RequestPinCreate>> pinCreate(@Body HttpPost<PostPinCreate> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_user_pinmodifyPwd")
    Observable<HttpRequest<RequestPinModify>> pinModify(@Body HttpPost<PostPinModify> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_logout")
    Observable<HttpRequest<RequestLogout>> logout(@Body HttpPost<PostLogout> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_forgetPwd")
    Observable<HttpRequest<RequestForgrtPwd>> forgetPwd(@Body HttpPost<PostForgetPwd> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_commissionsQuery")
    Observable<HttpRequest<RequestCommissions>> commissionsQuery(@Body HttpPost<PostCommissions> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_introduction")
    Observable<HttpRequest<RequestIntroduction>> introduction(@Body HttpPost<PostIntroduction> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("checkNumber")
    Observable<HttpRequest<VerificationCode>> checkNumber(@Body HttpPost<PostVerification> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("getProduct")
    Observable<HttpRequest<RequestProduct>> getProduct(@Body HttpPost<PostProduct> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("orderProduct")
    Observable<HttpRequest> orderProduct(@Body HttpPost<PostOrderProduct> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("getOrderProductList")
    Observable<HttpRequest<RequestOrderProduct>> getOrderProductList(@Body HttpPost<PostRecord> httpPost);




    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getAccountPeriodList")
    Observable<HttpRequest<RequestGetAccountPeriodList>> getAccountPeriodList(@Body HttpPost<PostGetAccountPeriodList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getAccountPeriod")
    Observable<HttpRequest<RequestGetAccountPeriod>> getAccountPeriod(@Body HttpPost<PostGetAccountPeriod> httpPost);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_upPersonal")
    Observable<HttpRequest<String >> getUpdatePersonalInfo(@Body HttpPost<PostUpdatePersonalInfo> httpPost);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getOpenPower")
    Observable<HttpRequest<RequestOpenPower>>getOpenPower(@Body HttpPost<PostOpenPower>httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_isNeedToRegister")
    Observable<HttpRequest<CheckInfo>>checkInfo(@Body HttpPost<PostOpenPower>httpPost);
}
