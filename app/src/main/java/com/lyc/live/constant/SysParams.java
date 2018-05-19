package com.lyc.live.constant;

/**
 * Created by lyc on 18/5/18.
 */

public interface SysParams {

    String APP_KEY ="*****";
    String APP_SECRET ="**********";

    String BaseUrl = Config.TestApiMode?"":"";

//    String Main_Feeds = BaseUrl+"/feeds";
//    String Main_Feeds = "http://httpbin.org/get";
    //因为会有一个判断 是否是正常的Url 所以只能在path下面做文章，来使用假数据
    String Main_Feeds = "http://0.0.0.0/home_feeds";

    String  Feeds_Details = BaseUrl+"/feeds/{id}/detail";
}
