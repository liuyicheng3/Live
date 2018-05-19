package com.lyc.live.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lyc on 18/5/19.
 */

public class CommonParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        //&source=android这个是get带参数
        //?source=android 这个是只能get不带传参,还有就是post
        String url = request.url().toString()+"?source=android";

        Request request1 = request.newBuilder().url(url).build();

        Response response = chain.proceed(request1);
        return response;
    }
}