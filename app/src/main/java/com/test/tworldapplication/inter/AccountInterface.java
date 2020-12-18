package com.test.tworldapplication.inter;

import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostBalanceQuery;
import com.test.tworldapplication.entity.PostLogin;
import com.test.tworldapplication.entity.PostOtherDiscount;
import com.test.tworldapplication.entity.PostPsdCheck;
import com.test.tworldapplication.entity.PostQueryBalance;
import com.test.tworldapplication.entity.PostQueryProduct;
import com.test.tworldapplication.entity.PostReAdd;
import com.test.tworldapplication.entity.PostRecharge;
import com.test.tworldapplication.entity.PostRechargeDiscount;
import com.test.tworldapplication.entity.PostServiceProduct;
import com.test.tworldapplication.entity.PostServiceProductDetail;
import com.test.tworldapplication.entity.ProductListEntity;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestLogin;
import com.test.tworldapplication.entity.RequestOtherDiscount;
import com.test.tworldapplication.entity.RequestPsdCheck;
import com.test.tworldapplication.entity.RequestQueryBalance;
import com.test.tworldapplication.entity.RequestReAdd;
import com.test.tworldapplication.entity.RequestRecharge;
import com.test.tworldapplication.entity.RequestRechargeDiscount;
import com.test.tworldapplication.entity.RequestServiceProduct;
import com.test.tworldapplication.entity.RequestServiceProductDetail;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by dasiy on 16/11/3.
 */

public interface AccountInterface {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_recharge")
    Observable<HttpRequest<RequestRecharge>> recharge(@Body HttpPost<PostRecharge> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_balanceQuery")
    Observable<HttpRequest<RequestBalanceQuery>> balanceQuery(@Body HttpPost<PostBalanceQuery> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_queryBalance")
    Observable<HttpRequest<RequestQueryBalance>> queryBalance(@Body HttpPost<PostQueryBalance> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_rechargeDiscount")
    Observable<HttpRequest<RequestRechargeDiscount>> rechargeDiscount(@Body HttpPost<PostRechargeDiscount> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019QueryProducts")
    Observable<HttpRequest<ProductListEntity>> _2019QueryProducts(@Body HttpPost<PostQueryProduct> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019ServiceProduct")
    Observable<HttpRequest<RequestServiceProduct>> _2019ServiceProduct(@Body HttpPost<PostServiceProduct> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_2019QueryProductDetails")
    Observable<HttpRequest<RequestServiceProductDetail>> _2019ServiceProductDetails(@Body HttpPost<PostServiceProductDetail> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_initialPsdCheck")
    Observable<HttpRequest<RequestPsdCheck>> initialPsdCheck(@Body HttpPost<PostPsdCheck> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_reAddRechargeRecord")
    Observable<HttpRequest<RequestReAdd>> reAddRecharge(@Body HttpPost<PostReAdd> httpPost);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("agency_otherRechargeDiscount")
    Observable<HttpRequest<RequestOtherDiscount>> otherRechargeDiscount(@Body HttpPost<PostOtherDiscount> httpPost);

}
