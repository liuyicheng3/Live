package com.lyc.live.net;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.lyc.live.common.base.LLApplication;
import com.lyc.live.utils.UtilsManager;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;

/**
 * Created by lyc on 18/5/19.
 */

public class MockInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (request.url().toString().startsWith("test://")){
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //取参数
            Uri uri = Uri.parse(request.url().toString());
            String name = uri.getHost();
            name ="home_feeds";
            String page = uri.getQueryParameter("page");
            if (!TextUtils.isEmpty(page)) {
                if (!page.equals("0") && !page.equals("1")) {
                    name = name + page;
                }
            }
            final String key = name+".json";
            ResponseBody body = ResponseBody.create(null,getTestInfoFromRaw(LLApplication.getInstance().getApplicationContext(),key));
            Response mockResponse = new Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("It is Fake Data")
                    .body(body)
                    .build();
            return mockResponse;
        }else {
            return chain.proceed(request);
        }
    }

    private  String getTestInfoFromRaw(Context ctx, String name) throws IOException {
        InputStream is = ctx.getAssets().open(name);
        String result = UtilsManager.convertStreamToString(is);
        return result;
    }
}