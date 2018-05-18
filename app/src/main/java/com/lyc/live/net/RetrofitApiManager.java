package com.lyc.live.net;

import android.content.Context;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiManager {

    private static Retrofit retrofit = null;
    private  static BasicParamsInterceptor interceptor=null;
    public static Retrofit getInstance(Context context) {
        if (retrofit == null || interceptor == null){
                if (interceptor == null || interceptor == null) {

                    interceptor  =new BasicParamsInterceptor.Builder().build();
                    interceptor.initCommonParams(context);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                    retrofit = new Retrofit.Builder()
                            //这里可以不写基地址 然后请求时候写完整地址
//                            .baseUrl("http://***.cn/")
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
        }
        interceptor.initCommonParams(context);
        return retrofit;
    }


    public static void refreshCommonParams(Context context){
        if (interceptor!=null){
            interceptor.initCommonParams(context);

        }
    }

}
