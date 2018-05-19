package com.lyc.live.entry.bean.net;

import com.lyc.live.common.bean.FeedItemItemBean;
import com.lyc.live.net.RespStatusBean;

import java.util.ArrayList;

/**
 * Created by lyc on 18/5/18.
 */

public class FeedListRespBean extends RespStatusBean {


    /**
     * pageCount : 1600
     * cate2Id : 0
     * nowPage : 1
     * cate2Name :
     * shortName :
     */

    public int pageCount;
    public int cate2Id;
    public int nowPage;
    public String cate2Name;
    public String shortName;

    public ArrayList<FeedItemItemBean> result = new ArrayList<>();


    public void copy2self(FeedListRespBean resp){
        this.status = resp.status;
        this.desc = resp.desc;
        this.pageCount = resp.pageCount;
        this.cate2Id = resp.cate2Id;
        this.nowPage = resp.nowPage;
        this.cate2Name = resp.cate2Name;
        this.shortName = resp.shortName;
        this.result.clear();
        this.result.addAll(resp.result);
    }
}
