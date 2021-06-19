package com.hm.viewdemo.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class MyUtils {


    /**
     * 处理文本，将文本位数限制为maxLen，中文两个字符，英文一个字符
     * <p>
     * 比如 content 是全英文，maxLen = 5
     * <p>
     * 1. content = abcde , 返回结果 abcde
     * 2. content = abcdef , 返回结果 abcde...
     * <p>
     * 比如 content 是全中文，maxLen = 5
     * <p>
     * 1. content = 古道 , 返回结果 古道
     * 2. content = 古道西风瘦马 , 返回结果 古道...
     * <p>
     * 比如 content 是全中文，maxLen = 6
     * <p>
     * 1. content = 古道西 , 返回结果 古道西
     * 2. content = 古道西风瘦马 , 返回结果 古道西...
     * <p>
     * 比如 content 是文中文混合，maxLen = 5
     * * <p>
     * * 1. content = a古道 , 返回结果 a古道
     * * 2. content = a古道西风瘦马 , 返回结果 a古道...
     * <p>
     * 比如 content 是文中文混合，maxLen =6
     * <p>
     * 1. content = a古道西 , 返回结果 a古道西...
     * 2. content = a古道西风瘦马 , 返回结果 a古道西...
     *
     * @param content 要处理的文本
     * @param maxLen  限制文本字符数，中文两个字符，英文一个字符。例如：a啊b吧，则maxLen为6
     * @return
     */
    public static String getTextLimitMaxLength(String content, int maxLen) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        int count = 0;
        int endIndex = 0;
        for (int i = 0; i < content.length(); i++) {
            char item = content.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
            if (count == maxLen || (item >= 128 && count == maxLen + 1)) {
                endIndex = i;
            }
        }
        if (count <= maxLen) {
            return content;
        } else {
            return content.substring(0, endIndex + 1) + "...";//末尾添加省略号
            //return content.substring(0, endIndex + 1);//末尾不添加省略号
        }
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static void executeInThread(Runnable runnable) {
        new Thread(runnable).start();
    }

}
