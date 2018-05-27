package com.lyc.live.constant;

import android.os.Environment;

/**
 * Created by lyc on 18/5/18.
 */

public interface MidData {

    String rootDir = Environment.getExternalStorageDirectory().getPath();

    String appDir = rootDir + "/Live/";

    /**
     * 应用缓存目录 ☆
     */
    String tempDir = appDir + ".temp/";
    /**
     * 网络图片的缓存目录 .temp/image/
     */
    String ImageDir = tempDir + "image";
}
