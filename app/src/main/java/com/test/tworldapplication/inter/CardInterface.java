package com.test.tworldapplication.inter;

import com.test.tworldapplication.activity.PostResult;
import com.test.tworldapplication.activity.order.RequestQrCode;
import com.test.tworldapplication.adapter.PostRegistration;
import com.test.tworldapplication.entity.JHOrderInfo;
import com.test.tworldapplication.entity.PostAddress;
import com.test.tworldapplication.entity.PostBkApply;
import com.test.tworldapplication.entity.PostDs;
import com.test.tworldapplication.entity.PostIntroduction;
import com.test.tworldapplication.entity.PostJHOrderInfo;
import com.test.tworldapplication.entity.PostLockNumberNew;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.PostQrCode;
import com.test.tworldapplication.entity.PostReSubmit;
import com.test.tworldapplication.entity.PostSelectionCheck;
import com.test.tworldapplication.entity.PostSetOpenNew;
import com.test.tworldapplication.entity.RequestDs;
import com.test.tworldapplication.entity.RequestIntroduction;
import com.test.tworldapplication.entity.RequestLockNumber;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.DailiAgentsLiangNumber;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangRecords;
import com.test.tworldapplication.entity.PostAgentsLiang;
import com.test.tworldapplication.entity.PostAgentsLiangNumber;
import com.test.tworldapplication.entity.PostCaution;
import com.test.tworldapplication.entity.PostCautionOrder;
import com.test.tworldapplication.entity.PostCheck;
import com.test.tworldapplication.entity.PostCuteRegular;
import com.test.tworldapplication.entity.PostImsi;
import com.test.tworldapplication.entity.PostJudgeIsLiang;
import com.test.tworldapplication.entity.PostLiangPay;
import com.test.tworldapplication.entity.PostLockNumber;
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
import com.test.tworldapplication.entity.PostRandomNumber;
import com.test.tworldapplication.entity.PostRepair;
import com.test.tworldapplication.entity.PostSetOpen;
import com.test.tworldapplication.entity.PostTips;
import com.test.tworldapplication.entity.PostTransfer;
import com.test.tworldapplication.entity.PostWhiteOpen;
import com.test.tworldapplication.entity.PostWritePreDetails;
import com.test.tworldapplication.entity.RequestCaution;
import com.test.tworldapplication.entity.RequestCautionOrder;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestJudgeIsLiang;
import com.test.tworldapplication.entity.RequestLiang;
import com.test.tworldapplication.entity.RequestLiangAgents;
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
import com.test.tworldapplication.http.RequestLiangPay;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by dasiy on 16/11/4.
 */

public interface CardInterface {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_numberSegment")
    Observable<HttpRequest<RequestNumberSegment>> numberSegment(@Body HttpPost<PostNumberSegment> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019lockNumber")
    Observable<HttpRequest<RequestLockNumberNew>> lockNumberNew(@Body HttpPost<PostLockNumberNew> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_transfer")
    Observable<HttpRequest<RequestTransfer>> transfer(@Body HttpPost<PostTransfer> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_repairCard")
    Observable<HttpRequest<RequestRepair>> repair(@Body HttpPost<PostRepair> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_check")
    Observable<HttpRequest<RequestCheck>> check(@Body HttpPost<PostCheck> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_selection_check")
    Observable<HttpRequest<RequestSelectionCheck>> checkSelect(@Body HttpPost<PostSelectionCheck> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_promotion")
    Observable<HttpRequest<RequestPromotion>> promotion(@Body HttpPost<PostPromotion> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_money")
    Observable<HttpRequest<RequestMoney>> money(@Body HttpPost<PostMoney> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_liangRegistrationOrder")
    Observable<HttpRequest> liangRegistration(@Body HttpPost<PostRegistration> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_setOpen")
    Observable<HttpRequest<RequestOpen>> open(@Body HttpPost<PostOpen> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019setOpen")
    Observable<HttpRequest<RequestSetOpenNew>> setOpen(@Body HttpPost<PostSetOpenNew> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_whiteSetOpen")
    Observable<HttpRequest<RequestWhiteOpen>> whiteSetOpen(@Body HttpPost<PostWhiteOpen> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_whiteNumberPool")
    Observable<HttpRequest<RequestNumberPool>> whiteNumberPool(@Body HttpPost<PostNumberPool> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_randomNumber")
    Observable<HttpRequest<RequestRandomNumber>> randomNumber(@Body HttpPost<PostRandomNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_lockNumber")
    Observable<HttpRequest<RequestLockNumber>> lockNumber(@Body HttpPost<PostLockNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_imsi")
    Observable<HttpRequest<RequestImsi>> imsi(@Body HttpPost<PostImsi> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019Result")
    Observable<HttpRequest> postResult(@Body HttpPost<PostResult> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019GetPreImsi")
    Observable<HttpRequest<RequestImsi>> getImsi(@Body HttpPost<PostImsi> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agencyGetPreImsiAgain")
    Observable<HttpRequest<RequestImsi>> getImsiAgain(@Body HttpPost<PostImsi> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_preopen")
    Observable<HttpRequest<RequestPreOpen>> preopen(@Body HttpPost<PostPreOpen> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_cardOpenMode")
    Observable<HttpRequest<RequestMode>> openMode(@Body HttpPost<PostMode> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_judgeIsLiang")
    Observable<HttpRequest<RequestJudgeIsLiang>> judgeIsLiang(@Body HttpPost<PostJudgeIsLiang> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getTips")
    Observable<HttpRequest<RequestTips>> getTips(@Body HttpPost<PostTips> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getLiangRule")
    Observable<HttpRequest<RequestLiang>> getLiangRule(@Body HttpPost<PostCuteRegular> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019preNumberDetails")
    Observable<HttpRequest<RequestPreNumberDetails>> preNumberDetailsNew(@Body HttpPost<PostPreNumberDetails> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019preNumberOrderInfo")
    Observable<HttpRequest<JHOrderInfo>> preNumberOrderInfo(@Body HttpPost<PostJHOrderInfo> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getAgentsLiangList")
    Observable<HttpRequest<RequestLiangAgents>> getAgentsLiangList(@Body HttpPost<PostAgentsLiang> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getAgentsLiangNumber")
    Observable<HttpRequest<DailiAgentsLiangNumber>> getAgentsLiangNumber(@Body HttpPost<PostAgentsLiangNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getHjsjLiangList")
    Observable<HttpRequest<RequestLiangAgents>> getHjsjLiangList(@Body HttpPost<PostAgentsLiang> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getHjsjLiangNumber")
    Observable<HttpRequest<AgentsLiangNumber>> getHjsjLiangNumber(@Body HttpPost<PostAgentsLiangNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_liangImsi")
    Observable<HttpRequest<RequestImsi>> liangImsi(@Body HttpPost<PostImsi> httpPost);

//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST("agency_ePreNumberList")
//    Observable<HttpRequest<RequestPreNumber>> ePreNumberList(@Body HttpPost<PostPreNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_liangRecords")
    Observable<HttpRequest<LiangRecords>> liangRecords(@Body HttpPost<LiangRecords> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_liangSetOpen")
    Observable<HttpRequest<RequestSetOpen>> liangSetOpen(@Body HttpPost<PostSetOpen> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_openInformation")
    Observable<HttpRequest> openInformation(@Body HttpPost<PostOpenInformation> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_ePreNumberList")
    Observable<HttpRequest<RequestPreNumberList>> preNumberList(@Body HttpPost<PostPreNumberList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_ePreNumber")
    Observable<HttpRequest<RequestPreNumber>> ePreNumber(@Body HttpPost<PostPreNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getPreWhiteNumberPool")
    Observable<HttpRequest<RequestPreNumberPool>> getPreWhiteNumberPool(@Body HttpPost<PostPreNumberPool> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_preRandomNumber")
    Observable<HttpRequest<RequestPreRandomNumber>> preRandomNumber(@Body HttpPost<PostPreRandomNumber> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_preNumberDetails")
    Observable<HttpRequest<RequestPreNumberDetails>> preNumberDetails(@Body HttpPost<PostPreNumberDetails> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getPreImsi")
    Observable<HttpRequest<RequestImsi>> getPreImsi(@Body HttpPost<PostImsi> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_writePreDetails")
    Observable<HttpRequest> writePreDetails(@Body HttpPost<PostWritePreDetails> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_liangPay")
    Observable<HttpRequest<RequestLiangPay>> liangPay(@Body HttpPost<PostLiangPay> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_isBond")
    Observable<HttpRequest<RequestCaution>> isBond(@Body HttpPost<PostCaution> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_bondAmount")
    Observable<HttpRequest<RequestCautionOrder>> bondAmount(@Body HttpPost<PostCautionOrder> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_bondOrder")
    Observable<HttpRequest<RequestReAdd>> bondOrder(@Body HttpPost<PostPay> httpPost);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getAgreementAddress")
    Observable<HttpRequest<String>> address(@Body HttpPost<PostAddress> httpPost);


//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST("agency_getOpenPower")
//    Observable<HttpRequest<RequestOpenPower>>getOpenPower(@Body HttpPost<PostOpenPower>httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getEcard")
    Observable<HttpRequest<RequestOpenPower>> getBkApply(@Body HttpPost<PostBkApply> httpPost);

//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST("agency_getOpenPowerList")
//    Observable<HttpRequest<RequestOpenPower>>getOpenPowerList(@Body HttpPost<PostOpenPowerList>httpPost);

//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST("agency_getOpenPower")
//    Observable<HttpRequest<PostBkSelect>>getBkSelect(@Body HttpPost<RequestBkSelect>httpPost);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_dataSupplement")
    Observable<HttpRequest<String>> getReSubmit(@Body HttpPost<PostReSubmit> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_checkUserDS")
    Observable<HttpRequest<RequestDs>> checkDs(@Body HttpPost<PostDs> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_selection_qrcode")
    Observable<HttpRequest<RequestQrCode>> getQr(@Body HttpPost<PostQrCode> httpPost);


}
