package com.lyc.live.net;

import android.content.Context;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiManager {

    private static Retrofit retrofit = null;
    private static LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
    private static MockInterceptor mockInterceptor = new MockInterceptor();
    private  static BasicParamsInterceptor interceptor=null;
    public static Retrofit getInstance(Context context) {
        if (retrofit == null || interceptor == null){
                if (interceptor == null || interceptor == null) {

                    interceptor  =new BasicParamsInterceptor.Builder().build();
                    interceptor.initCommonParams(context);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).
                            addInterceptor(loggingInterceptor).
                            addInterceptor(mockInterceptor).
                            build();
                    retrofit = new Retrofit.Builder()
                            /**
                             * 这里可以写任意一个合法的url，但是不可不写，会报错的
                             * 建议写个官网就行，以后所有的都可以这么写死
                             * 官方对于多基地址有两种方案，一种是在接口注解时候写全地址
                             * 另外一种通过 @Url
                             */
                            .baseUrl("http://test.org")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
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
