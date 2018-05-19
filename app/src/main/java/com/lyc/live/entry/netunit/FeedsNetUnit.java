package com.lyc.live.entry.netunit;

import com.lyc.live.common.base.BaseActivity;
import com.lyc.live.common.bean.net.FeedDetailBean;
import com.lyc.live.entry.bean.net.FeedListRespBean;
import com.lyc.live.entry.netunit.service.FeedsService;
import com.lyc.live.net.BaseNetUnit;
import com.lyc.live.net.RetrofitApiManager;
import com.lyc.live.utils.MLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lyc on 18/5/18.
 */

public class FeedsNetUnit {

    public BaseActivity mActivity;


    public FeedsNetUnit(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    private BaseNetUnit.SingePageListRequestListener feedsListener;

    public void setFeedsListener(BaseNetUnit.SingePageListRequestListener feedsListener) {
        this.feedsListener = feedsListener;
    }

    public void getFeeds(int page) {
        if (feedsListener == null){
            MLog.e("feedsListener = null");
            return;
        }
        FeedsService service = RetrofitApiManager.getInstance(mActivity.getApplicationContext()).create(FeedsService.class);
        Observable<FeedListRespBean> observable = service.getFeeds(page);           //获取Observable对象
        observable.subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<FeedListRespBean>() {
                    @Override
                    public void call(FeedListRespBean feedList) {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<FeedListRespBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        feedsListener.onFail(null);
                    }

                    @Override
                    public void onNext(FeedListRespBean feedList) {
                        feedsListener.onSuccess(feedList);
                    }
                });
    }

    public void getFeedsList2() {
        FeedsService service = RetrofitApiManager.getInstance(mActivity.getApplicationContext()).create(FeedsService.class);
        Call<FeedListRespBean> call = service.getFeeds2(1);
        call.enqueue(new Callback<FeedListRespBean>() {
            @Override
            public void onResponse(Call<FeedListRespBean> call, Response<FeedListRespBean> response) {
                MLog.d("onResponse----->" + response.body().status);

            }

            @Override
            public void onFailure(Call<FeedListRespBean> call, Throwable t) {
                MLog.d("onFailure----->" + t.toString());

            }

        });
    }

    public void getFeedsDetail(String feedId, Subscriber<FeedDetailBean> callBack) {
        /*FeedsService service = RetrofitApiManager.getInstance(mActivity.getApplicationContext()).create(FeedsService.class);
        service.getFeedDetail(feedId)               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(callBack);*/
    }
}
