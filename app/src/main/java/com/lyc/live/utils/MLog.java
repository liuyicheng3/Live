package com.lyc.live.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lyc.live.livelove.BuildConfig;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lyc on 18/5/19.
 */

public class MLog {

    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static final String LOG_DEFAULT_TAG = "Live&Love";
    private static final String LOG_DEFAULT_MESSAGE = "execute";

    private static void printLog(int type, String tag, Object... args) {
        if (!BuildConfig.DEVELOP_MODE || args == null) {
            return;
        }
        String[] contents = wrapperContent(tag, args);
        Log.println(type, contents[0], contents[1] + contents[2]);

    }

    public static void d() {
        printLog(Log.DEBUG, LOG_DEFAULT_TAG, LOG_DEFAULT_MESSAGE);
    }

    public static void d(Object message) {
        printLog(Log.DEBUG, LOG_DEFAULT_TAG, message);
    }

    public static void d(String tag, Object... args) {
        printLog(Log.DEBUG, tag, args);
    }

    public static void i() {
        printLog(Log.INFO, LOG_DEFAULT_TAG, LOG_DEFAULT_MESSAGE);
    }

    public static void i(Object message) {
        printLog(Log.INFO, LOG_DEFAULT_TAG, message);
    }

    public static void i(String tag, Object... args) {
        printLog(Log.INFO, tag, args);
    }

    public static void w() {
        printLog(Log.WARN, LOG_DEFAULT_TAG, LOG_DEFAULT_MESSAGE);
    }

    public static void w(Object message) {
        printLog(Log.WARN, LOG_DEFAULT_TAG, message);
    }

    public static void w(String tag, Object... args) {
        printLog(Log.WARN, tag, args);
    }

    public static void e() {
        printLog(Log.ERROR, LOG_DEFAULT_MESSAGE);
    }

    public static void e(Object message) {
        printLog(Log.ERROR, LOG_DEFAULT_TAG, message);
    }

    public static void e(String tag, Object... args) {
        printLog(Log.ERROR, tag, args);
    }

    /**
     * 以文本的形式打印一条数据流 并且返回原数据流
     */
    public static InputStream PrintAndReturnIs(InputStream is, Context ctx) {
        if (BuildConfig.DEVELOP_MODE) {
            String result = convertStreamToString(is);
            MLog.i(result);
            return getIsFromStr(result);
        } else {
            return is;
        }
    }


    /**
     * 将InputStream流转化为string
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * 将字符串转换为流
     */
    public static InputStream getIsFromStr(String str) {
        InputStream myIn = null;
        try {
            myIn = new ByteArrayInputStream(str.getBytes());
        } catch (Exception e) {
            MLog.d("Error! ");
        }
        return myIn;
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final StackTraceElement ste = stackTrace[5];

        StringBuilder stringBuilder = new StringBuilder();
        String className = ste.getFileName();
        if (!TextUtils.isEmpty(className)) {
            String methodName = ste.getMethodName();
            int lineNumber = ste.getLineNumber();
            stringBuilder.append("(").append(className).append(":").append(lineNumber).append(") #").append(methodName).append(" : ");
        } else {
            stringBuilder.append(" ");
        }

        String tag = (tagStr == null ? LOG_DEFAULT_TAG : tagStr);
        String msg = (objects == null) ? "null" : getObjectsString(objects);
        String headString = stringBuilder.toString();

        return new String[]{tag, headString, msg};
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append("null").append(" ");
                } else {
                    stringBuilder.append(object.toString()).append(" ");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? "null" : object.toString();
        }
    }
}