package com.lyc.live.entry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyc.live.common.base.BaseActivity;
import com.lyc.live.common.bean.FeedItemItemBean;
import com.lyc.live.common.bean.net.FeedDetailBean;
import com.lyc.live.common.image.CustomImageView;
import com.lyc.live.livelove.R;

/**
 * Created by lyc on 18/5/17.
 */

public class MainActivity extends BaseActivity implements IMainView {
    private RecyclerView rv_feed;
    public RecyclerView.LayoutManager layoutManager;
    private FeedAdapter adapter;
    private MainViewPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
        initData();
    }

    private void initView() {
        rv_feed= findViewById(R.id.rv_feed);
        layoutManager = new LinearLayoutManager(mActivity);
        rv_feed.setLayoutManager(layoutManager);
    }

    private void initData(){
        presenter = new MainViewPresenter(this,this);
        presenter.getPage(0);
    }

    @Override
    public void showAdapter() {
        if (adapter == null) {
            adapter = new FeedAdapter();
            rv_feed.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showNetError() {

    }

    private class  FeedAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_view,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder feedHolder = (ViewHolder) holder;
            feedHolder.initData(presenter.getFeedAtPos(position));
        }

        @Override
        public int getItemCount() {
            return presenter.feedsSize();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private CustomImageView iv_cover;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
        }

        public void initData(FeedItemItemBean itemItemBean){
            iv_cover.setImageUrl(itemItemBean.room_src);
//            iv_cover.setImageUrl("http://img03.tooopen.com/uploadfile/downs/images/20110714/sy_20110714135215645030.jpg");
        }
    }
}
