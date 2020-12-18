package com.test.tworldapplication.inter;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dasiy on 16/11/8.
 */

public interface ImageInterface {
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);
}
