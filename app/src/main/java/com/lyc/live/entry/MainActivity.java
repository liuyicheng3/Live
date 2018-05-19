package com.lyc.live.entry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.lyc.live.common.base.BaseActivity;
import com.lyc.live.entry.netunit.FeedsNetUnit;
import com.lyc.live.livelove.R;

/**
 * Created by lyc on 18/5/17.
 */

public class MainActivity extends BaseActivity {

    private Button btn_get;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
    }

    private void initView() {
        btn_get = findViewById(R.id.btn_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainData();
            }
        });

    }

    private void getMainData(){
        FeedsNetUnit feedsNetUnit = new FeedsNetUnit(mActivity);
        feedsNetUnit.getFeeds(0);
    }
}
