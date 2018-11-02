package com.lyc.live.common.image;

/**
 * Created by lyc on 18/5/19.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lyc.live.common.image.transform.BlurTransformation;
import com.lyc.live.common.image.transform.RoundedTransformation;
import com.lyc.live.livelove.R;
import com.lyc.live.utils.MLog;
import com.lyc.live.utils.UtilsManager;

import java.lang.reflect.Field;

/**
 *
 * Normal ImageView used for Net Image. No Rounded Mode or Circle Mode.
 * Directly use {@link #setImageUrl(String)} to display image from Network.
 */
public class CustomImageView extends AppCompatImageView {


    public CustomImageView(Context context) {
        this(context.getApplicationContext(), null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context.getApplicationContext(), attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context.getApplicationContext(), attrs, defStyle);
    }

    /** 与 Activity 生命周期联动, 没有 placeholder */
    public void setImageUrl(Activity activity, String url) {
        if (activity.isFinishing()){
            MLog.i(activity+"已经销毁");
            return;
        }
        GlideApp.with(activity).load(url).centerCrop().into(this);
    }

    /** 推荐使用，与 Activity 生命周期联动 */
    public void setImageUrl(Activity activity, String url, int defaultResId) {
        if (activity.isFinishing()){
            MLog.i(activity+"已经销毁");
            return;
        }
        GlideApp.with(activity).load(url).placeholder(defaultResId).centerCrop().into(this);
    }

    /** 简单的正圆形图片，推荐使用，与 Activity 生命周期联动 */
    public void setImageUrlCircle(Activity activity, String url, int defaultResId) {
        if (activity.isFinishing()){
            MLog.i(activity+"已经销毁");
            return;
        }
        GlideApp.with(activity).load(url).placeholder(defaultResId).circleCrop().into(this);
    }

    public void setImageUrl(String url) {
        setImageUrl(url, R.drawable.live__img_default);
    }

    public void setImageUrl(String url, int defaultResId) {
        if (getContext() instanceof Activity && ((Activity) getContext()).isFinishing()) {
            MLog.i(getContext()+"已经销毁");
            return;
        }
        GlideApp.with(getContext())
                .load(url)
                .placeholder(defaultResId)
                .into(this);
    }


    /**
     * 对于在列表里面  同一种Item有不同size的ImageView
     * 由于涉及到ListViewItem的重用，导致glide通过被重用的View的getWidth()获得的宽高不是当前要显示图片的size，一定要用这个方法，手动传入宽高
     */
    public void setImageUrl(String url, int defaultResId, int width, int height) {
        if (getContext() instanceof Activity && ((Activity) getContext()).isFinishing()) {
            MLog.i(getContext()+"已经销毁");
            return;
        }
        GlideApp.with(getContext())
                .load(url)
                .placeholder(defaultResId)
                .override(width, height)
                .into(this);
    }

    public void setImageUrl(String url, int loadingResId, int failResId) {
        if (getContext() instanceof Activity && ((Activity) getContext()).isFinishing()) {
            MLog.i(getContext()+"已经销毁");
            return;
        }
        GlideApp.with(getContext())
                .load(url)
                .placeholder(loadingResId)
                .error(failResId)
                .into(this);
    }

    /** 瀑布流专用 有默认底色 淡出动画 */
    public void setImageUrlInWaterFall(Activity activity, String url, int width, int height, int position) {
        if (activity.isFinishing()){
            MLog.i(activity+"已经销毁");
            return;
        }
        GlideApp.with(activity)
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
//                .placeholder(PlaceHolderHelper.getBackgroundDrawable(position))
                .override(width, height)
                .into(this);
    }

    /** 图片模糊 淡出动画 */
    public void setImageUrlBlur(Activity activity, String url) {
        if (activity.isFinishing()){
            MLog.i(activity+"已经销毁");
            return;
        }
        GlideApp.with(activity)
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .transform(new MultiTransformation<Bitmap>(new CenterCrop(), new BlurTransformation()))
                .into(this);
    }

    public void setImageUrlFade(Activity activity, String url){
        setImageUrlFade(activity, url, null);
    }

    /** 淡出动画 */
    public void setImageUrlFade(Activity activity, String url, final LoadImageCallBack callBack) {
        if (activity.isFinishing()){
            MLog.i(activity+"已经销毁");
            return;
        }
        GlideApp.with(activity)
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callBack != null){
                            callBack.onFailed();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (callBack != null){
                            callBack.onSuccess();
                        }
                        return false;
                    }
                })
                .into(this);
    }

    /** 圆角图片 支持动图 */
    public void setImageUrlRound(String url, int width, int height, int position) {
        if (getContext() instanceof Activity && ((Activity) getContext()).isFinishing()) {
            MLog.i(getContext()+"已经销毁");
            return;
        }
        GlideApp.with(getContext())
                .load(url)
//                .placeholder(PlaceHolderHelper.getBackgroundDrawable(position))
                .transform(new RoundedTransformation(UtilsManager.dip2px(getContext(),4), 0))
                .override(width, height)
                .into(this);
    }


    private int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    public interface LoadImageCallBack{
        void onSuccess();
        void onFailed();
    }
}
