package com.lyc.live.entry;

import com.lyc.live.common.base.BaseActivity;
import com.lyc.live.common.bean.FeedItemItemBean;
import com.lyc.live.entry.bean.net.FeedListRespBean;
import com.lyc.live.entry.netunit.FeedsNetUnit;
import com.lyc.live.net.BaseNetUnit;

/**
 * Created by lyc on 18/5/20.
 */

public class MainViewPresenter {

    private BaseActivity mActivity;
    private IMainView iMainView;

    private FeedListRespBean feedListRespBean = new FeedListRespBean();
    private FeedsNetUnit feedsNetUnit;

    public MainViewPresenter(BaseActivity mActivity, IMainView iMainView) {
        this.mActivity = mActivity;
        this.iMainView = iMainView;
        initNetUnit();
    }

    private void initNetUnit(){
        feedsNetUnit = new FeedsNetUnit(mActivity);
        feedsNetUnit.setFeedsListener(new BaseNetUnit.SingePageListRequestListener() {
            @Override
            public void onStart(Object obj) {

            }

            @Override
            public void onSuccess(Object obj) {
                if (obj instanceof  FeedListRespBean){
                    FeedListRespBean result = (FeedListRespBean) obj;
                    feedListRespBean.copy2self(result);
                    iMainView.showAdapter();
                }

            }

            @Override
            public void onFail(Object obj) {
                iMainView.showNetError();
            }

            @Override
            public void onReturnNull(Object obj) {
                iMainView.showEmpty();
            }

            @Override
            public void onTaskCancel() {

            }
        });
    }

    public void getPage(int page){
        feedsNetUnit.getFeeds(page);
    }

    public int feedsSize(){
        return feedListRespBean.result.size();
    }

    public FeedItemItemBean getFeedAtPos(int pos){
        return feedListRespBean.result.get(pos);
    }

}
