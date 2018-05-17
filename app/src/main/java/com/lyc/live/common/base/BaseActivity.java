package com.lyc.live.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by lyc on 18/5/17.
 */

public class BaseActivity extends FragmentActivity {

    protected Context mContext;
    protected BaseActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = this;
        this.mContext = this.getApplicationContext();
        LLApplication.getInstance().addActivity(mActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LLApplication.getInstance().removeActivity(mActivity);
    }
}
