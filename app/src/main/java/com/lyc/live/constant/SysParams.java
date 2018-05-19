package com.lyc.live.constant;

/**
 * Created by lyc on 18/5/18.
 */

public interface SysParams {

    String APP_KEY ="*****";
    String APP_SECRET ="**********";

    String BaseUrl = Config.TestApiMode?"":"";

//    String Main_Feeds = BaseUrl+"/feeds";
    String Main_Feeds = "/get";


    String  Feeds_Details = BaseUrl+"/feeds/{id}/detail";
}
