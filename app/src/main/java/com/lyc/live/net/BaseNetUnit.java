package com.lyc.live.net;

import android.content.Context;


import java.util.LinkedList;

import retrofit2.Call;


public abstract class BaseNetUnit {

	/** 请求的TAG 在页面退出是取消该请求 */
	public String TAG = "";
	private LinkedList<Call> bindUIRequest;



    /** 抽象方法，子类必须要重载的方法，一般情况下都要在退出界面时调用 */
    public  void cancelRequest(Context context){

	}

	public void setBindUIRequest(LinkedList<Call> bindUIRequest) {
		this.bindUIRequest = bindUIRequest;
	}



    /**
	 * @date 2013-8-7下午3:04:00
	 * @version 1.0 该参数接口主要用于获取带分页的列表数据
	 *
	 * 建议参考GetAllTopicNetUnit 写分页的网络请求
	 *
	 */
	public interface MultiPageListRequestListener {


		void onStart(Object obj);

		/** 第一页加载成功 需要影藏正在加载中的页面 并清空list数据 并显示 */
		void onFirstSuccess(Object obj);

		/** 第一页加载失败 需要显示失败以及重试的按钮 */
		void onFirstFail(Object obj);

		/** 第一次加载返回为空 需要显示为搜索到结果的页面 */
		void onFirstReturnNull(Object obj);

		/** 非第一次加载返回为空 需要显示为搜索到结果的页面 */
		void onOtherReturnNull(Object obj);

		/** 非第一次加载成功 要根据当前页数进行判断是否清空list数据 */
		void onOtherSuccess(Object obj);

		/** 非第一次加载失败 只弹出toast提示失败 */
		void onOtherFail(Object obj);

		/**任务取消*/
		void onTaskCancel();

	}

	/**
	 * 建议参考GetAllCycleNetUnit 写单页的网络请求
	 */
	public interface SingePageListRequestListener {

		void onStart(Object obj);

		/** 第一页加载成功 需要影藏正在加载中的页面 并清空list数据 并显示 */
		void onSuccess(Object obj);

		/** 第一页加载失败 需要显示失败以及重试的按钮 */
		void onFail(Object obj);

		/** 第一次加载返回为空 需要显示为搜索到结果的页面 */
		void onReturnNull(Object obj);
		
		/**任务取消*/
		void onTaskCancel();
	}

	/**
	 * 建议参考FocusAndCancelFocusNetUnit 写普通的状态请求
	 */
	public interface StateRequestListener {

		void onStart(Object obj);

		void onSuccess(Object obj);

		void onFail(Object obj);
		
		void onTaskCancel();

	}

    /**
     * 网络返回时简单需要操作 UI
     */
    public interface DoneRequestListener {

        void onRequestDone(Object obj);

    }

    public void bindRequestWithUI(Call request){
        if (bindUIRequest !=null){
            bindUIRequest.add(request);
        }
    }


}
