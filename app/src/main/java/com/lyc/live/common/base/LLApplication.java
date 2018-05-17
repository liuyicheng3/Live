package com.lyc.live.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;

/**
 * Created by lyc on 18/5/17.
 */

public class LLApplication  extends Application{

    private static LLApplication llApplication;

    private LinkedList<Activity> activityList = new LinkedList<Activity>();

    public static LLApplication getInstance(){
        return llApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        llApplication = this;
    }


    /** 添加Activity到容器中 */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public Activity getCurrentActivity(){
        if(activityList.size()>0){
            return activityList.get(activityList.size()-1);
        }else{
            return null;
        }
    }

    /**检查Activity是否在栈顶*/
    public boolean isActivityInTop(Activity activity) {
        int top = activityList.size() - 1;
        if (top >= 0) {
            return activityList.get(top) == activity;
        }
        return false;
    }

    /** 移除activity */
    public void removeActivity(Activity activity) {
        int size = activityList.size();
        if (size > 0) {
            for (int i = size - 1; i >= 0; i--) {
                if (activityList.get(i) == activity) {
                    activityList.remove(i);
                    break;
                }
            }
        }

    }
}
