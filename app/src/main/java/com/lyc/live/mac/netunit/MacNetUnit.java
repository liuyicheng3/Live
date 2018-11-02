package com.lyc.live.mac.netunit;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.lyc.live.common.base.BaseActivity;
import com.lyc.live.mac.UploadMacService;
import com.lyc.live.mac.bean.net.UploadMacRespBean;
import com.lyc.live.net.RetrofitApiManager;
import com.lyc.live.utils.MLog;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lyc on 18/5/18.
 */

public class MacNetUnit {

    public BaseActivity mActivity;

    public MacNetUnit(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void sendData(String data) {
        UploadMacService service = RetrofitApiManager.getInstance(mActivity.getApplicationContext())
                .create(UploadMacService.class);
        service.sendMac2Server(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<UploadMacRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UploadMacRespBean s) {
                        if (s.success) {
                            MLog.e(new Gson().toJson(s));
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences
                                    (mActivity);
                            sp.edit().putInt("upload_2", 1).apply();
                        }
                    }
                });

    }

    public void getData() {
        UploadMacService service = RetrofitApiManager.getInstance(mActivity.getApplicationContext()).create(UploadMacService.class);
        service.getUploadedMac(1)               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        MLog.e(s);
                    }
                });
    }
}
