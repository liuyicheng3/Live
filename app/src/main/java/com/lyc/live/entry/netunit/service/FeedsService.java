package com.lyc.live.entry.netunit.service;

import com.lyc.live.common.bean.net.FeedDetailBean;
import com.lyc.live.constant.SysParams;
import com.lyc.live.entry.bean.net.FeedListRespBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lyc on 18/5/18.
 */

public interface FeedsService {

    @GET(SysParams.Main_Feeds)
    Observable<FeedListRespBean> getFeeds(
            @Query("page") int page
    );

    @GET(SysParams.Main_Feeds)
    Call<FeedListRespBean> getFeeds2(
            @Query("page") int page
    );

    @GET(SysParams.Feeds_Details)
    Observable<FeedDetailBean> getFeedDetail(
            @Path("id") String feed_id
    );

}
