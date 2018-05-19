package com.lyc.live.net;

import android.net.Uri;

import com.lyc.live.utils.MLog;

import java.io.IOException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lyc on 18/5/19.
 */


public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //首先取到Request
        Request request = chain.request();
        Response response = null;
        Request requestProcess = null;
        long startTime = System.currentTimeMillis();
        MLog.d(request.method()+" -> "+request.url());
        if ("GET".equals(request.method())) {
            String url = request.url().toString() + "&source=android";
            Request.Builder builder = request.newBuilder();
            builder.get().url(url);
            requestProcess = builder.build();
            response = chain.proceed(requestProcess);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                for (int i = 0; i < formBody.size(); i++) {
                    builder.add(formBody.encodedName(i), formBody.encodedValue(i));
                }
                builder.add("source", "android");
            }
            requestProcess = request.newBuilder().url(request.url().toString()).post(builder.build()).build();
            response = chain.proceed(requestProcess);
        }
        MLog.d(request.url().uri().getPath()+" -> "+response.body().string());
        MLog.d(request.url().uri().getPath()+" 耗时 "+(System.currentTimeMillis()-startTime)+"ms");
        return response;
    }
}
