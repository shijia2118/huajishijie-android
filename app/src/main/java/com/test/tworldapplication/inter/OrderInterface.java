package com.test.tworldapplication.inter;

import com.test.tworldapplication.entity.JudgeLocationResponse;
import com.test.tworldapplication.entity.LiangRule;
import com.test.tworldapplication.entity.NumberPoolNew;
import com.test.tworldapplication.entity.Phone;
import com.test.tworldapplication.entity.PostAgencyGetBill;
import com.test.tworldapplication.entity.PostAuditList;
import com.test.tworldapplication.entity.PostBkApplyList;
import com.test.tworldapplication.entity.PostLocationEntity;
import com.test.tworldapplication.entity.PostOrderEntity;
import com.test.tworldapplication.entity.PostPreNumberListNew;
import com.test.tworldapplication.entity.PostReSubmit;
import com.test.tworldapplication.entity.PostSelectOrderInfo;
import com.test.tworldapplication.entity.PostSendOrCancel;
import com.test.tworldapplication.entity.PostSessionToken;
import com.test.tworldapplication.entity.PreRandomNumberNew;
import com.test.tworldapplication.entity.RequestAuditList;
import com.test.tworldapplication.entity.RequestBkSelect;
import com.test.tworldapplication.entity.PostGetSonOrder;
import com.test.tworldapplication.entity.PostGetSonOrderList;
import com.test.tworldapplication.entity.RequestAgencyGetBill;
import com.test.tworldapplication.entity.RequestBkApplyList;
import com.test.tworldapplication.entity.PostBkSelect;
import com.test.tworldapplication.entity.RequestGetSonOrder;
import com.test.tworldapplication.entity.RequestGetSonOrderList;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostChannelsList;
import com.test.tworldapplication.entity.PostChannelsOpenCount;
import com.test.tworldapplication.entity.PostOrderInfo;
import com.test.tworldapplication.entity.PostRechargeList;
import com.test.tworldapplication.entity.PostRemarkOrderInfo;
import com.test.tworldapplication.entity.PostTransferList;
import com.test.tworldapplication.entity.PostOrderList;
import com.test.tworldapplication.entity.PostTransferOrderInfo;
import com.test.tworldapplication.entity.RequestChannelOpenCount;
import com.test.tworldapplication.entity.RequestChannelsList;
import com.test.tworldapplication.entity.RequestOrderEntity;
import com.test.tworldapplication.entity.RequestOrderInfo;
import com.test.tworldapplication.entity.RequestPreNumberNew;
import com.test.tworldapplication.entity.RequestRechargeList;
import com.test.tworldapplication.entity.RequestRemarkOrderInfo;
import com.test.tworldapplication.entity.RequestSelectionOrderInfo;
import com.test.tworldapplication.entity.RequestTransferList;
import com.test.tworldapplication.entity.RequestOrderList;
import com.test.tworldapplication.entity.RequestTransferOrderInfo;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by dasiy on 16/11/7.
 */

public interface OrderInterface {
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_orderList")
    Observable<HttpRequest<RequestOrderList>> orderKhList(@Body HttpPost<PostOrderList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_selection_auditList")
    Observable<HttpRequest<RequestAuditList>> orderAuditList(@Body HttpPost<PostAuditList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_cardTransferList")
    Observable<HttpRequest<RequestTransferList>> orderTransferList(@Body HttpPost<PostTransferList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_rechargeList")
    Observable<HttpRequest<RequestRechargeList>> orderRechargeList(@Body HttpPost<PostRechargeList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_openOrderInfo")
    Observable<HttpRequest<RequestOrderInfo>> orderInfo(@Body HttpPost<PostOrderInfo> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_openEOrderInfo")
    Observable<HttpRequest<RequestOrderInfo>> openEOrderInfo(@Body HttpPost<PostOrderInfo> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_transferOrderInfo")
    Observable<HttpRequest<RequestTransferOrderInfo>> transferOrderInfo(@Body HttpPost<PostTransferOrderInfo> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_selection_details")
    Observable<HttpRequest<RequestSelectionOrderInfo>> selectOrderInfo(@Body HttpPost<PostSelectOrderInfo> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_remakecardOrderInfo")
    Observable<HttpRequest<RequestRemarkOrderInfo>> remarkOrderInfo(@Body HttpPost<PostRemarkOrderInfo> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_judgeLocation")
    Observable<HttpRequest<JudgeLocationResponse>> judgeLocation(@Body HttpPost<PostLocationEntity> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019ePreNumberList")
    Observable<HttpRequest<RequestPreNumberNew>> ePreNumberList(@Body HttpPost<PostPreNumberListNew> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019QueryProductOrders")
    Observable<HttpRequest<RequestOrderEntity>> _2019QueryOrders(@Body HttpPost<PostOrderEntity> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_channelsList")
    Observable<HttpRequest<RequestChannelsList>> channelsList(@Body HttpPost<PostChannelsList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_channelsOpenCount")
    Observable<HttpRequest<RequestChannelOpenCount>> channelsOpenCount(@Body HttpPost<PostChannelsOpenCount> httpPost);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getSonOrderList")
    Observable<HttpRequest<RequestGetSonOrderList>> getSonOrderList(@Body HttpPost<PostGetSonOrderList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getSonOrder")
    Observable<HttpRequest<RequestGetSonOrder>> getSonOrder(@Body HttpPost<PostGetSonOrder> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getBill")
    Observable<HttpRequest<RequestAgencyGetBill>> agencyGetBill(@Body HttpPost<PostAgencyGetBill> httpPost);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getEcardList")
    Observable<HttpRequest<RequestBkApplyList>> orderBkList(@Body HttpPost<PostBkApplyList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019whiteNumberPool")
    Observable<HttpRequest<List<NumberPoolNew>>> whiteNumberPool(@Body HttpPost<PostSessionToken> httpPost);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019preRandomNumber")
    Observable<HttpRequest<PreRandomNumberNew>> preRandomNumber(@Body HttpPost httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getEcardNo")
    Observable<HttpRequest<RequestBkSelect>> getBkSelect(@Body HttpPost<PostBkSelect> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_selection_audit")
    Observable<HttpRequest<String>> sendOrCancel(@Body HttpPost<PostSendOrCancel> httpPost);
}
