package com.lyc.live.entry.bean.net;

import com.lyc.live.common.bean.FeedItemItemBean;
import com.lyc.live.net.RespStatusBean;

import java.util.ArrayList;

/**
 * Created by lyc on 18/5/18.
 */

public class FeedListRespBean extends RespStatusBean {

    public static class Data {
        public ArrayList<FeedItemItemBean> list = new ArrayList<>();
        public int hasNext;
        public int page;
    }
}
