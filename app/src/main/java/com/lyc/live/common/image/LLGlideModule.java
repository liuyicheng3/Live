package com.lyc.live.common.image;

import android.content.Context;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.lyc.live.constant.MidData;
import com.lyc.live.utils.MLog;
import com.lyc.live.utils.UtilsManager;

import java.io.InputStream;

/**
 * Created by Lyc on 2018/5/21.
 */
@GlideModule
public class LLGlideModule extends AppGlideModule{

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        final int diskCacheSizeBytes = 200 * 1024 * 1024; // 200 MB

//        UtilsManager.mkdir(MidData.ImageDir);
//        builder.setDiskCache(new DiskLruCacheFactory(MidData.ImageDir, diskCacheSizeBytes));
        judgeIfIsLowDevice(context,builder);
        super.applyOptions(context, builder);
    }

    /**
     * 判断是否是低性能设备，因为发现在低版本SDK 经常oom
     * @param context
     * @param builder
     */
    private void judgeIfIsLowDevice(Context context, GlideBuilder builder) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP ) {
            MLog.e("低内存设备");
            //默认使用 PREFER_ARGB_8888  ，在第性能设备里面使用 PREFER_RGB_565
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.format(DecodeFormat.PREFER_RGB_565);
            builder.setDefaultRequestOptions(requestOptions);

            //glide 实际上自己有一套 针对低内存设备的计算方法，但是发现不准，即使七八百M内存的设备也不归为低内存设备，所以在它的计算基础上减半缓存
            MemorySizeCalculator.Builder memoryCacheBuilder = new MemorySizeCalculator.Builder(context);
            MemorySizeCalculator calculator = memoryCacheBuilder.build();
            int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
            int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

            int customMemoryCacheSize = (int) (0.4 * defaultMemoryCacheSize);
            int customBitmapPoolSize = (int) (0.4 * defaultBitmapPoolSize);

            builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
            builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.append(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
