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

/**
 * Created by lyc on 18/5/19.
 *
 * 构造假数据的 Interceptor，如果判断url是http://0.0.0.0开头的就使用path的名字来找假数据
 *
 * 这个添加到ApplicationInteceptor上面不要添加到 NetIntercepter上面
 */

public class MockInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (request.url().toString().startsWith("http://0.0.0.0")){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //取参数
            Uri uri = Uri.parse(request.url().toString());
            String name = uri.getPath().split("/")[1];
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