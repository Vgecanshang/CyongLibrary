package com.cy.cylibrary.utils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

/**
 * Created by cy on 2018/6/5.
 */
public class CLogger {
    /* 是否写日志 */
    public static boolean DEBUG = true;
    private static int LENGTH = 4000;//Log底层的最长长度，
    private static Settings mLoggerSetting;

    public static void setDEBUG(boolean isDebug){
        DEBUG = isDebug;
    }

    static {
        mLoggerSetting = Logger.init("CLOGGER");
    }

    public static void v(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.v(message);
            }
        }
    }

    public static void d(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.d(message);
            }
        }
    }

    public static void i(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.i(message);
            }
        }
    }

    public static void w(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.w(message);
            }
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.e(message);
            }
        }
    }

    public static void e(String message, int methodCount) {
        if (DEBUG) {
            mLoggerSetting.methodCount(methodCount);
            if (!TextUtils.isEmpty(message)) {
                Logger.e(message);
            }
            mLoggerSetting.methodCount(2);
        }
    }

    public static void println(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.i(message);
            }
        }
    }

    public static void println(String message, int methodCount) {
        if (DEBUG) {
            mLoggerSetting.methodCount(methodCount);
            if (!TextUtils.isEmpty(message)) {
                Logger.i(message);
            }
            mLoggerSetting.methodCount(2);
        }
    }

    public static void json(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.json(message);
            }
        }
    }

    public static void wtf(String message) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Logger.wtf(message);
            }
        }
    }

    public static void printObj(Object obj) {
        if(DEBUG){
            if(obj!=null){
                Logger.d(obj);
            }
        }
    }

    private void parseMessage(String tag , String message){
        // 当Log超过4000时，分段打印(Android的Log底层有4*1024的长度限制，即4000的限制，导致如果日志过长就打印不全)
        if(DEBUG){
            if (message.length() > LENGTH) {
                for (int i = 0; i < message.length(); i += LENGTH) {
                    if (i + LENGTH < message.length()) {
                        android.util.Log.i(tag, message.substring(i, i + LENGTH));
                    } else {
                        android.util.Log.i(tag, message.substring(i, message.length()));
                    }
                }
            } else {
                android.util.Log.i(tag, message);
            }
        }

    }


}
