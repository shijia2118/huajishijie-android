package com.test.tworldapplication.inter;

import com.test.tworldapplication.entity.Carousel;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostFunction;
import com.test.tworldapplication.entity.PostMessage;
import com.test.tworldapplication.entity.PostNoticeList;
import com.test.tworldapplication.entity.PostNumberCheck;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.PostRechargeList;
import com.test.tworldapplication.entity.RequestCarousel;
import com.test.tworldapplication.entity.RequestMessage;
import com.test.tworldapplication.entity.RequestNoticeList;
import com.test.tworldapplication.entity.RequestNumberCheck;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.entity.RequestRechargeList;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by dasiy on 16/11/7.
 */

public interface OtherInterface {
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_NoticeList")
    Observable<HttpRequest<RequestNoticeList>> noticeList(@Body HttpPost<PostNoticeList> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_Carousel")
    Observable<HttpRequest<RequestCarousel>> carousel(@Body HttpPost httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_getNotice")
    Observable<HttpRequest<RequestMessage>> getNotice(@Body HttpPost<PostMessage> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_numberCheck")
    Observable<HttpRequest<RequestNumberCheck>> numberCheck(@Body HttpPost<PostNumberCheck> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_pictureUpload")
    Observable<HttpRequest<RequestPictureUpload>> pictureUpload(@Body HttpPost<PostPictureUpload> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_FunctionStatistics")
    Observable<HttpRequest> functionStatistics(@Body HttpPost<PostFunction> httpPost);
}
