package com.lyc.live.net;

import android.net.Uri;

import com.lyc.live.constant.Config;
import com.lyc.live.livelove.BuildConfig;
import com.lyc.live.utils.MLog;
import com.lyc.live.utils.UtilsManager;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

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
        if (BuildConfig.DEVELOP_MODE){
            /**
             * 这个地方注意读的方式 source.readUtf8() 是从buffer里面读取的 读完后buffer会清空，所以需要再次向buffer里面写入数据
             */
            BufferedSource source = response.body().source();
            try {
                String content = source.readUtf8();
                MLog.d(request.url().uri().getPath()+" -> "+content);
                source.buffer().writeString(content, Charset.defaultCharset());

            }catch (Exception e){
                MLog.e(e);
            }
        }
        long timeConsume = System.currentTimeMillis()-startTime;
        if (timeConsume> Config.MaxNetRequestWarning){
            MLog.e(request.url().uri().getPath()+" 耗时 "+(System.currentTimeMillis()-startTime)+"ms");
        }
        return response;
    }
}
