package com.lyc.live.mac;


import com.lyc.live.mac.bean.net.UploadMacRespBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface UploadMacService {
    @FormUrlEncoded
    @POST("http://****/mac/upload.htm")
    Observable<UploadMacRespBean> sendMac2Server(
            @Field("data") String data
    );

    @GET("http://****/mac/view.htm")
    Observable<String> getUploadedMac(
            @Query("page") int page
    );
}
