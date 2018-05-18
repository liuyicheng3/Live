package com.lyc.live.entry.netunit;

import com.lyc.live.common.base.BaseActivity;
import com.lyc.live.common.bean.net.FeedDetailBean;
import com.lyc.live.entry.bean.net.FeedListRespBean;
import com.lyc.live.entry.netunit.service.FeedsService;
import com.lyc.live.net.RetrofitApiManager;

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

    public void getFeeds(int page) {
        FeedsService service = RetrofitApiManager.getInstance(mActivity.getApplicationContext()).create(FeedsService.class);
        service.getFeeds(page)               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<FeedListRespBean>() {
                    @Override
                    public void call(FeedListRespBean userInfo) {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<FeedListRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                    }

                    @Override
                    public void onNext(FeedListRespBean userInfo) {
                        //请求成功
                    }
                });
    }

    public void getFeedsDetail(String feedId, Subscriber<FeedDetailBean> callBack) {
        FeedsService service = RetrofitApiManager.getInstance(mActivity.getApplicationContext()).create(FeedsService.class);
        service.getFeedDetail(feedId)               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(callBack);
    }
}
